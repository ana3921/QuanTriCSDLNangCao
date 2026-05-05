USE banking_db;
GO

IF OBJECT_ID(N'dbo.sp_transfer_money', N'P') IS NOT NULL DROP PROCEDURE dbo.sp_transfer_money;
IF OBJECT_ID(N'dbo.sp_deposit', N'P') IS NOT NULL DROP PROCEDURE dbo.sp_deposit;
IF OBJECT_ID(N'dbo.sp_withdraw', N'P') IS NOT NULL DROP PROCEDURE dbo.sp_withdraw;
GO

CREATE PROCEDURE dbo.sp_transfer_money
    @from_account_number NVARCHAR(20),
    @to_account_number NVARCHAR(20),
    @amount DECIMAL(18,2),
    @description NVARCHAR(255),
    @result_code INT OUTPUT,
    @result_message NVARCHAR(255) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        DECLARE @from_id INT;
        DECLARE @to_id INT;
        DECLARE @from_balance DECIMAL(18,2);
        DECLARE @daily_limit DECIMAL(18,2);
        DECLARE @spent_today DECIMAL(18,2);

        SELECT
            @from_id = account_id,
            @from_balance = balance,
            @daily_limit = daily_limit
        FROM dbo.accounts WITH (UPDLOCK, ROWLOCK)
        WHERE account_number = @from_account_number
          AND status = N'ACTIVE';

        IF @from_id IS NULL
        BEGIN
            SET @result_code = -1;
            SET @result_message = N'Tài khoản nguồn không tồn tại hoặc đã bị khóa';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        IF @amount <= 0
        BEGIN
            SET @result_code = -2;
            SET @result_message = N'Số tiền không hợp lệ';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        IF @from_balance < @amount
        BEGIN
            SET @result_code = -3;
            SET @result_message = N'Số dư không đủ';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        SELECT @spent_today = ISNULL(SUM(amount), 0)
        FROM dbo.transactions
        WHERE from_account_id = @from_id
          AND transaction_type IN (N'TRANSFER', N'WITHDRAWAL', N'PAYMENT')
          AND status = N'SUCCESS'
          AND CONVERT(DATE, created_at) = CONVERT(DATE, GETDATE());

        IF (@spent_today + @amount) > @daily_limit
        BEGIN
            SET @result_code = -4;
            SET @result_message = N'Vượt hạn mức giao dịch trong ngày';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        SELECT @to_id = account_id
        FROM dbo.accounts WITH (UPDLOCK, ROWLOCK)
        WHERE account_number = @to_account_number
          AND status = N'ACTIVE';

        IF @to_id IS NULL
        BEGIN
            SET @result_code = -5;
            SET @result_message = N'Tài khoản đích không tồn tại hoặc đã bị khóa';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        IF @from_id = @to_id
        BEGIN
            SET @result_code = -6;
            SET @result_message = N'Không thể chuyển tiền vào chính tài khoản này';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        UPDATE dbo.accounts
        SET balance = balance - @amount
        WHERE account_id = @from_id;

        UPDATE dbo.accounts
        SET balance = balance + @amount
        WHERE account_id = @to_id;

        INSERT INTO dbo.transactions (
            transaction_code,
            from_account_id,
            to_account_id,
            amount,
            fee,
            transaction_type,
            status,
            description,
            completed_at
        )
        VALUES (
            N'TXN' + FORMAT(GETDATE(), 'yyyyMMddHHmmss') + RIGHT(REPLACE(CONVERT(NVARCHAR(36), NEWID()), N'-', N''), 6),
            @from_id,
            @to_id,
            @amount,
            0.00,
            N'TRANSFER',
            N'SUCCESS',
            @description,
            SYSUTCDATETIME()
        );

        SET @result_code = 0;
        SET @result_message = N'Chuyển tiền thành công';
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        SET @result_code = -99;
        SET @result_message = ERROR_MESSAGE();
    END CATCH
END;
GO

CREATE PROCEDURE dbo.sp_deposit
    @account_number NVARCHAR(20),
    @amount DECIMAL(18,2),
    @description NVARCHAR(255),
    @result_code INT OUTPUT,
    @result_message NVARCHAR(255) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        DECLARE @account_id INT;

        SELECT @account_id = account_id
        FROM dbo.accounts WITH (UPDLOCK, ROWLOCK)
        WHERE account_number = @account_number
          AND status = N'ACTIVE';

        IF @account_id IS NULL
        BEGIN
            SET @result_code = -1;
            SET @result_message = N'Tài khoản không tồn tại hoặc đã bị khóa';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        IF @amount <= 0
        BEGIN
            SET @result_code = -2;
            SET @result_message = N'Số tiền không hợp lệ';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        UPDATE dbo.accounts
        SET balance = balance + @amount
        WHERE account_id = @account_id;

        INSERT INTO dbo.transactions (
            transaction_code,
            from_account_id,
            to_account_id,
            amount,
            fee,
            transaction_type,
            status,
            description,
            completed_at
        )
        VALUES (
            N'DEP' + FORMAT(GETDATE(), 'yyyyMMddHHmmss') + RIGHT(REPLACE(CONVERT(NVARCHAR(36), NEWID()), N'-', N''), 6),
            NULL,
            @account_id,
            @amount,
            0.00,
            N'DEPOSIT',
            N'SUCCESS',
            @description,
            SYSUTCDATETIME()
        );

        SET @result_code = 0;
        SET @result_message = N'Nạp tiền thành công';
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        SET @result_code = -99;
        SET @result_message = ERROR_MESSAGE();
    END CATCH
END;
GO

CREATE PROCEDURE dbo.sp_withdraw
    @account_number NVARCHAR(20),
    @amount DECIMAL(18,2),
    @description NVARCHAR(255),
    @result_code INT OUTPUT,
    @result_message NVARCHAR(255) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        DECLARE @account_id INT;
        DECLARE @balance DECIMAL(18,2);
        DECLARE @daily_limit DECIMAL(18,2);
        DECLARE @spent_today DECIMAL(18,2);

        SELECT
            @account_id = account_id,
            @balance = balance,
            @daily_limit = daily_limit
        FROM dbo.accounts WITH (UPDLOCK, ROWLOCK)
        WHERE account_number = @account_number
          AND status = N'ACTIVE';

        IF @account_id IS NULL
        BEGIN
            SET @result_code = -1;
            SET @result_message = N'Tài khoản không tồn tại hoặc đã bị khóa';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        IF @amount <= 0
        BEGIN
            SET @result_code = -2;
            SET @result_message = N'Số tiền không hợp lệ';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        IF @balance < @amount
        BEGIN
            SET @result_code = -3;
            SET @result_message = N'Số dư không đủ';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        SELECT @spent_today = ISNULL(SUM(amount), 0)
        FROM dbo.transactions
        WHERE from_account_id = @account_id
          AND transaction_type IN (N'TRANSFER', N'WITHDRAWAL', N'PAYMENT')
          AND status = N'SUCCESS'
          AND CONVERT(DATE, created_at) = CONVERT(DATE, GETDATE());

        IF (@spent_today + @amount) > @daily_limit
        BEGIN
            SET @result_code = -4;
            SET @result_message = N'Vượt hạn mức giao dịch trong ngày';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        UPDATE dbo.accounts
        SET balance = balance - @amount
        WHERE account_id = @account_id;

        INSERT INTO dbo.transactions (
            transaction_code,
            from_account_id,
            to_account_id,
            amount,
            fee,
            transaction_type,
            status,
            description,
            completed_at
        )
        VALUES (
            N'WDR' + FORMAT(GETDATE(), 'yyyyMMddHHmmss') + RIGHT(REPLACE(CONVERT(NVARCHAR(36), NEWID()), N'-', N''), 6),
            @account_id,
            NULL,
            @amount,
            0.00,
            N'WITHDRAWAL',
            N'SUCCESS',
            @description,
            SYSUTCDATETIME()
        );

        SET @result_code = 0;
        SET @result_message = N'Rút tiền thành công';
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        SET @result_code = -99;
        SET @result_message = ERROR_MESSAGE();
    END CATCH
END;
GO
