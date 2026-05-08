USE banking_db;
GO

IF OBJECT_ID(N'dbo.trg_accounts_audit', N'TR') IS NOT NULL DROP TRIGGER dbo.trg_accounts_audit;
IF OBJECT_ID(N'dbo.trg_transactions_notify', N'TR') IS NOT NULL DROP TRIGGER dbo.trg_transactions_notify;
IF OBJECT_ID(N'dbo.trg_users_audit', N'TR') IS NOT NULL DROP TRIGGER dbo.trg_users_audit;
IF OBJECT_ID(N'dbo.trg_accounts_prevent_delete', N'TR') IS NOT NULL DROP TRIGGER dbo.trg_accounts_prevent_delete;
GO

IF OBJECT_ID(N'dbo.vw_customer_account_summary', N'V') IS NOT NULL DROP VIEW dbo.vw_customer_account_summary;
IF OBJECT_ID(N'dbo.vw_transaction_detail', N'V') IS NOT NULL DROP VIEW dbo.vw_transaction_detail;
IF OBJECT_ID(N'dbo.vw_monthly_transaction_report', N'V') IS NOT NULL DROP VIEW dbo.vw_monthly_transaction_report;
IF OBJECT_ID(N'dbo.vw_admin_dashboard_today', N'V') IS NOT NULL DROP VIEW dbo.vw_admin_dashboard_today;
GO

CREATE TRIGGER dbo.trg_accounts_audit
ON dbo.accounts
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF UPDATE(status) OR UPDATE(balance)
    BEGIN
        INSERT INTO dbo.audit_logs (user_id, action, entity_type, entity_id, old_value, new_value, description, ip_address)
        SELECT
            c.user_id,
            CASE
                WHEN d.status <> i.status THEN N'UPDATE_ACCOUNT_STATUS'
                WHEN d.balance <> i.balance THEN N'UPDATE_ACCOUNT_BALANCE'
                ELSE N'UPDATE_ACCOUNT'
            END,
            N'accounts',
            i.account_id,
            (SELECT d.account_id, d.account_number, d.status, d.balance, d.daily_limit FOR JSON PATH, WITHOUT_ARRAY_WRAPPER),
            (SELECT i.account_id, i.account_number, i.status, i.balance, i.daily_limit FOR JSON PATH, WITHOUT_ARRAY_WRAPPER),
            N'Account data changed by trigger',
            N'127.0.0.1'
        FROM inserted i
        INNER JOIN deleted d ON d.account_id = i.account_id
        INNER JOIN dbo.customers c ON c.customer_id = i.customer_id
        WHERE d.status <> i.status OR d.balance <> i.balance;
    END
END;
GO

CREATE TRIGGER dbo.trg_transactions_notify
ON dbo.transactions
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;

    INSERT INTO dbo.notifications (user_id, title, content, type, related_tx_id)
    SELECT
        c.user_id,
        N'Biến động số dư',
        N'Tài khoản của bạn vừa bị trừ ' + FORMAT(i.amount, 'N0') + N' VND. Mã GD: ' + i.transaction_code,
        N'TRANSACTION',
        i.transaction_id
    FROM inserted i
    INNER JOIN dbo.accounts a ON a.account_id = i.from_account_id
    INNER JOIN dbo.customers c ON c.customer_id = a.customer_id
    WHERE i.status = N'SUCCESS'
      AND i.from_account_id IS NOT NULL;

    INSERT INTO dbo.notifications (user_id, title, content, type, related_tx_id)
    SELECT
        c.user_id,
        N'Biến động số dư',
        N'Tài khoản của bạn vừa được cộng ' + FORMAT(i.amount, 'N0') + N' VND. Mã GD: ' + i.transaction_code,
        N'TRANSACTION',
        i.transaction_id
    FROM inserted i
    INNER JOIN dbo.accounts a ON a.account_id = i.to_account_id
    INNER JOIN dbo.customers c ON c.customer_id = a.customer_id
    WHERE i.status = N'SUCCESS'
      AND i.to_account_id IS NOT NULL;
END;
GO

CREATE TRIGGER dbo.trg_users_audit
ON dbo.users
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF UPDATE(role) OR UPDATE(is_active)
    BEGIN
        INSERT INTO dbo.audit_logs (user_id, action, entity_type, entity_id, old_value, new_value, description, ip_address)
        SELECT
            i.user_id,
            CASE
                WHEN d.role <> i.role THEN N'UPDATE_USER_ROLE'
                WHEN d.is_active <> i.is_active THEN N'UPDATE_USER_STATUS'
                ELSE N'UPDATE_USER'
            END,
            N'users',
            i.user_id,
            (SELECT d.user_id, d.username, d.role, d.is_active FOR JSON PATH, WITHOUT_ARRAY_WRAPPER),
            (SELECT i.user_id, i.username, i.role, i.is_active FOR JSON PATH, WITHOUT_ARRAY_WRAPPER),
            N'User data changed by trigger',
            N'127.0.0.1'
        FROM inserted i
        INNER JOIN deleted d ON d.user_id = i.user_id
        WHERE d.role <> i.role OR d.is_active <> i.is_active;
    END
END;
GO

CREATE TRIGGER dbo.trg_accounts_prevent_delete
ON dbo.accounts
INSTEAD OF DELETE
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS (SELECT 1 FROM deleted WHERE balance > 0)
    BEGIN
        THROW 50001, N'Không thể xóa tài khoản còn số dư. Vui lòng rút hết tiền trước.', 1;
    END;

    DELETE a
    FROM dbo.accounts a
    INNER JOIN deleted d ON d.account_id = a.account_id;
END;
GO

CREATE VIEW dbo.vw_customer_account_summary
AS
SELECT
    c.customer_id,
    c.full_name,
    c.phone,
    u.email,
    c.kyc_status,
    COUNT(a.account_id) AS account_count,
    SUM(CASE WHEN a.status = N'ACTIVE' THEN a.balance ELSE 0 END) AS total_balance,
    SUM(CASE WHEN a.account_type = N'SAVINGS' AND a.status = N'ACTIVE' THEN a.balance ELSE 0 END) AS savings_balance,
    SUM(CASE WHEN a.account_type = N'PAYMENT' AND a.status = N'ACTIVE' THEN a.balance ELSE 0 END) AS payment_balance,
    MAX(a.opened_at) AS latest_account_opened_at
FROM dbo.customers c
INNER JOIN dbo.users u ON u.user_id = c.user_id
LEFT JOIN dbo.accounts a ON a.customer_id = c.customer_id
GROUP BY c.customer_id, c.full_name, c.phone, u.email, c.kyc_status;
GO

CREATE VIEW dbo.vw_transaction_detail
AS
SELECT
    t.transaction_id,
    t.transaction_code,
    t.transaction_type,
    t.amount,
    t.fee,
    t.status,
    t.description,
    t.reference_code,
    t.is_suspicious,
    t.created_at,
    t.completed_at,
    fa.account_number AS from_account_number,
    fc.full_name AS from_customer_name,
    ta.account_number AS to_account_number,
    tc.full_name AS to_customer_name
FROM dbo.transactions t
LEFT JOIN dbo.accounts fa ON fa.account_id = t.from_account_id
LEFT JOIN dbo.customers fc ON fc.customer_id = fa.customer_id
LEFT JOIN dbo.accounts ta ON ta.account_id = t.to_account_id
LEFT JOIN dbo.customers tc ON tc.customer_id = ta.customer_id;
GO

CREATE VIEW dbo.vw_monthly_transaction_report
AS
SELECT
    YEAR(t.created_at) AS report_year,
    MONTH(t.created_at) AS report_month,
    t.transaction_type,
    COUNT(*) AS transaction_count,
    SUM(t.amount) AS total_amount,
    AVG(t.amount) AS average_amount,
    MAX(t.amount) AS max_amount,
    MIN(t.amount) AS min_amount,
    SUM(CASE WHEN t.status = N'SUCCESS' THEN 1 ELSE 0 END) AS success_count,
    SUM(CASE WHEN t.status = N'FAILED' THEN 1 ELSE 0 END) AS failed_count,
    SUM(t.fee) AS total_fee
FROM dbo.transactions t
GROUP BY YEAR(t.created_at), MONTH(t.created_at), t.transaction_type;
GO

CREATE VIEW dbo.vw_admin_dashboard_today
AS
SELECT
    (SELECT COUNT(*) FROM dbo.users WHERE CONVERT(DATE, created_at) = CONVERT(DATE, GETDATE())) AS new_users_today,
    (SELECT COUNT(*) FROM dbo.transactions WHERE CONVERT(DATE, created_at) = CONVERT(DATE, GETDATE())) AS transactions_today,
    (SELECT ISNULL(SUM(amount), 0) FROM dbo.transactions WHERE status = N'SUCCESS' AND CONVERT(DATE, created_at) = CONVERT(DATE, GETDATE())) AS successful_amount_today,
    (SELECT COUNT(*) FROM dbo.transactions WHERE status = N'FAILED' AND CONVERT(DATE, created_at) = CONVERT(DATE, GETDATE())) AS failed_transactions_today,
    (SELECT COUNT(*) FROM dbo.accounts WHERE status = N'LOCKED') AS locked_accounts,
    (SELECT ISNULL(SUM(balance), 0) FROM dbo.accounts WHERE status = N'ACTIVE') AS active_total_balance;
GO
