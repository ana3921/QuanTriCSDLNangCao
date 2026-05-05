USE banking_db;
GO

SET NOCOUNT ON;
GO

DELETE FROM dbo.notifications;
DELETE FROM dbo.transactions;
DELETE FROM dbo.accounts;
DELETE FROM dbo.customers;
DELETE FROM dbo.users;
GO

-- Insert users
INSERT INTO dbo.users (username, password_hash, email, role, is_active)
VALUES
    (N'admin', N'$2b$12$demo_admin_hash', N'admin@bank.local', N'ADMIN', 1),
    (N'employee1', N'$2b$12$demo_employee_hash', N'employee1@bank.local', N'EMPLOYEE', 1),
    (N'customer1', N'$2b$12$demo_customer1_hash', N'customer1@bank.local', N'CUSTOMER', 1),
    (N'customer2', N'$2b$12$demo_customer2_hash', N'customer2@bank.local', N'CUSTOMER', 1);
GO

-- Insert customers using direct lookups
INSERT INTO dbo.customers (user_id, full_name, id_number, phone, date_of_birth, address, kyc_status)
VALUES
    ((SELECT user_id FROM dbo.users WHERE username = N'customer1'), N'Nguyen Van A', N'012345678901', N'0901000001', '2000-01-15', N'Ho Chi Minh City', N'VERIFIED'),
    ((SELECT user_id FROM dbo.users WHERE username = N'customer2'), N'Tran Thi B', N'012345678902', N'0901000002', '2001-08-22', N'Ha Noi', N'VERIFIED');
GO

-- Insert accounts using direct lookups
INSERT INTO dbo.accounts (account_number, customer_id, account_type, balance, currency, status, is_default, daily_limit)
VALUES
    (N'1000000001', (SELECT customer_id FROM dbo.customers WHERE id_number = N'012345678901'), N'PAYMENT', 25000000.00, N'VND', N'ACTIVE', 1, 50000000.00),
    (N'1000000002', (SELECT customer_id FROM dbo.customers WHERE id_number = N'012345678901'), N'SAVINGS', 50000000.00, N'VND', N'ACTIVE', 0, 50000000.00),
    (N'2000000001', (SELECT customer_id FROM dbo.customers WHERE id_number = N'012345678902'), N'PAYMENT', 12000000.00, N'VND', N'ACTIVE', 1, 30000000.00);
GO

-- Insert transactions using direct lookups
INSERT INTO dbo.transactions (
    transaction_code,
    from_account_id,
    to_account_id,
    amount,
    fee,
    transaction_type,
    status,
    description,
    reference_code,
    is_suspicious,
    created_at,
    completed_at
)
VALUES
    (N'TXN202604300001', 
     (SELECT account_id FROM dbo.accounts WHERE account_number = N'1000000001'),
     (SELECT account_id FROM dbo.accounts WHERE account_number = N'2000000001'),
     1500000.00, 0.00, N'TRANSFER', N'SUCCESS', N'Chuyen tien demo', N'REF001', 0, SYSUTCDATETIME(), SYSUTCDATETIME()),
    (N'DEP202604300001', 
     NULL,
     (SELECT account_id FROM dbo.accounts WHERE account_number = N'1000000001'),
     2000000.00, 0.00, N'DEPOSIT', N'SUCCESS', N'Nop tien demo', N'REF002', 0, SYSUTCDATETIME(), SYSUTCDATETIME());
GO

-- Insert notifications
INSERT INTO dbo.notifications (user_id, title, content, type, is_read, related_tx_id)
SELECT u.user_id, N'Bien dong so du', N'Tai khoan da phat sinh giao dich demo', N'TRANSACTION', 0, t.transaction_id
FROM dbo.users AS u
CROSS JOIN (SELECT TOP (1) transaction_id FROM dbo.transactions ORDER BY transaction_id DESC) AS t
WHERE u.username = N'customer1';
GO

INSERT INTO dbo.notifications (user_id, title, content, type, is_read, related_tx_id)
SELECT u.user_id, N'Bao mat', N'Dang nhap thanh cong tren thiet bi moi', N'SECURITY', 0, NULL
FROM dbo.users AS u
WHERE u.username = N'customer2';
GO
