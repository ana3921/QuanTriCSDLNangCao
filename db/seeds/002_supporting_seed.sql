USE banking_db;
GO

SET NOCOUNT ON;
GO

-- Clear dependent tables first so the seed can be rerun safely.
DISABLE TRIGGER ALL ON dbo.accounts;
DISABLE TRIGGER ALL ON dbo.users;
DELETE FROM dbo.notifications;
DELETE FROM dbo.bill_payments;
DELETE FROM dbo.saved_bills;
DELETE FROM dbo.cards;
DELETE FROM dbo.beneficiaries;
DELETE FROM dbo.audit_logs;
DELETE FROM dbo.login_history;
DELETE FROM dbo.transactions;
DELETE FROM dbo.interest_rates;
DELETE FROM dbo.system_config;
DELETE FROM dbo.accounts;
DELETE FROM dbo.customers;
DELETE FROM dbo.users;
ENABLE TRIGGER ALL ON dbo.accounts;
ENABLE TRIGGER ALL ON dbo.users;
GO

-- Users
INSERT INTO dbo.users (username, password_hash, email, role, is_active)
VALUES
    (N'admin', N'$2a$10$s6aQ1ucukNp./Kvb3CQKquJ54G8pE6as0F5ssfVEemU8DMCF04PGG', N'admin@bank.local', N'ADMIN', 1),
    (N'employee1', N'$2a$10$H6K6UGYPWieI9EduH3GkPOeNBq2.QKwdbof6ctP2i0nuyFp0ZzBO2', N'employee1@bank.local', N'EMPLOYEE', 1),
    (N'customer1', N'$2a$10$LZ8mCvlWuFa3dpiYUHAOqOq1M9qxjzFnSG56QkVV7sz33S0SML.ny', N'customer1@bank.local', N'CUSTOMER', 1),
    (N'customer2', N'$2a$10$OEwomJ2Zgqfps13L7CkptOeqlG8xd8ru30xD1ykl0.7o4B7R.5Fn6', N'customer2@bank.local', N'CUSTOMER', 1),
    (N'customer3', N'$2a$10$4sVhJ.J8QLNttvgmkkSlaejesDTGgSFq6VJ5rg8mZbLXuPWj0mRsy', N'customer3@bank.local', N'CUSTOMER', 1);
GO

-- Customers
INSERT INTO dbo.customers (user_id, full_name, id_number, phone, date_of_birth, address, kyc_status)
VALUES
    ((SELECT user_id FROM dbo.users WHERE username = N'customer1'), N'Nguyen Van A', N'012345678901', N'0901000001', '2000-01-15', N'Ho Chi Minh City', N'VERIFIED'),
    ((SELECT user_id FROM dbo.users WHERE username = N'customer2'), N'Tran Thi B', N'012345678902', N'0901000002', '2001-08-22', N'Ha Noi', N'VERIFIED'),
    ((SELECT user_id FROM dbo.users WHERE username = N'customer3'), N'Le Van C', N'012345678903', N'0901000003', '1999-05-10', N'Da Nang', N'PENDING');
GO

-- Accounts
INSERT INTO dbo.accounts (account_number, customer_id, account_type, balance, currency, status, is_default, daily_limit, term_months, interest_rate, maturity_date)
VALUES
    (N'1000000001', (SELECT customer_id FROM dbo.customers WHERE id_number = N'012345678901'), N'PAYMENT', 25000000.00, N'VND', N'ACTIVE', 1, 50000000.00, NULL, NULL, NULL),
    (N'1000000002', (SELECT customer_id FROM dbo.customers WHERE id_number = N'012345678901'), N'SAVINGS', 50000000.00, N'VND', N'ACTIVE', 0, 50000000.00, 6, 5.50, DATEADD(MONTH, 6, CAST(GETDATE() AS DATE))),
    (N'2000000001', (SELECT customer_id FROM dbo.customers WHERE id_number = N'012345678902'), N'PAYMENT', 12000000.00, N'VND', N'ACTIVE', 1, 30000000.00, NULL, NULL, NULL),
    (N'2000000002', (SELECT customer_id FROM dbo.customers WHERE id_number = N'012345678902'), N'SAVINGS', 7500000.00, N'VND', N'ACTIVE', 0, 30000000.00, 12, 6.00, DATEADD(MONTH, 12, CAST(GETDATE() AS DATE))),
    (N'3000000001', (SELECT customer_id FROM dbo.customers WHERE id_number = N'012345678903'), N'PAYMENT', 5000000.00, N'VND', N'LOCKED', 1, 10000000.00, NULL, NULL, NULL);
GO

-- Reference data for rates and configs
INSERT INTO dbo.interest_rates (term_months, rate_percent, effective_from, effective_to, is_active)
VALUES
    (1, 4.50, '2024-01-01', NULL, 1),
    (3, 5.00, '2024-01-01', NULL, 1),
    (6, 5.50, '2024-01-01', NULL, 1),
    (12, 6.00, '2024-01-01', NULL, 1),
    (24, 6.20, '2024-01-01', NULL, 1),
    (36, 6.50, '2024-01-01', NULL, 1);

INSERT INTO dbo.system_config (config_key, config_value, description)
VALUES
    (N'MAX_DAILY_TRANSFER', N'500000000', N'Hạn mức chuyển tiền tối đa mỗi ngày (VND)'),
    (N'SESSION_TIMEOUT_MIN', N'30', N'Thời gian hết phiên (phút)'),
    (N'MAX_LOGIN_ATTEMPTS', N'5', N'Số lần đăng nhập sai tối đa trước khi khóa'),
    (N'OTP_EXPIRE_MIN', N'5', N'Thời gian hiệu lực OTP (phút)'),
    (N'MIN_TRANSFER_AMOUNT', N'10000', N'Số tiền chuyển tối thiểu (VND)');
GO

-- Cards
INSERT INTO dbo.cards (customer_id, card_number, card_type, cardholder_name, expiry_month, expiry_year, cvv, status, is_active)
VALUES
    ((SELECT customer_id FROM dbo.customers WHERE id_number = N'012345678901'), N'4111111111111111', N'DEBIT', N'Nguyen Van A', 12, 2028, N'111', N'ACTIVE', 1),
    ((SELECT customer_id FROM dbo.customers WHERE id_number = N'012345678902'), N'4222222222222222', N'DEBIT', N'Tran Thi B', 6, 2027, N'222', N'BLOCKED', 1),
    ((SELECT customer_id FROM dbo.customers WHERE id_number = N'012345678901'), N'4333333333333333', N'CREDIT', N'Nguyen Van A', 1, 2029, N'333', N'ACTIVE', 1);
GO

-- Transactions
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
     1500000.00, 0.00, N'TRANSFER', N'SUCCESS', N'Chuyen tien demo', N'REF001', 0, DATEADD(DAY, -2, SYSUTCDATETIME()), DATEADD(DAY, -2, SYSUTCDATETIME())),
    (N'DEP202604300001', 
     NULL,
     (SELECT account_id FROM dbo.accounts WHERE account_number = N'1000000001'),
     2000000.00, 0.00, N'DEPOSIT', N'SUCCESS', N'Nop tien demo', N'REF002', 0, DATEADD(DAY, -1, SYSUTCDATETIME()), DATEADD(DAY, -1, SYSUTCDATETIME())),
    (N'WDR202605010001',
     (SELECT account_id FROM dbo.accounts WHERE account_number = N'2000000001'),
     NULL,
     500000.00, 0.00, N'WITHDRAWAL', N'SUCCESS', N'Rut tien demo', N'REF003', 0, DATEADD(HOUR, -12, SYSUTCDATETIME()), DATEADD(HOUR, -12, SYSUTCDATETIME()));
GO

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
    (N'BIL202605070001',
     (SELECT account_id FROM dbo.accounts WHERE account_number = N'1000000001'),
     NULL,
     350000.00, 0.00, N'PAYMENT', N'SUCCESS', N'Thanh toan tien dien demo', N'BILL001', 0, SYSUTCDATETIME(), SYSUTCDATETIME());
GO

INSERT INTO dbo.bill_payments (account_id, biller_name, amount, due_date, payment_date, status, reference, notes, is_active)
SELECT
    a.account_id,
    N'EVN HCMC',
    350000.00,
    DATEADD(DAY, 7, SYSUTCDATETIME()),
    SYSUTCDATETIME(),
    N'PAID',
    N'BILL001',
    N'Thanh toan tien dien demo',
    1
FROM dbo.accounts a
WHERE a.account_number = N'1000000001';
GO

-- Beneficiaries
INSERT INTO dbo.beneficiaries (customer_id, account_number, bank_name, full_name, nickname, is_active)
VALUES
    ((SELECT customer_id FROM dbo.customers WHERE id_number = N'012345678901'), N'2000000001', N'Nội bộ', N'Tran Thi B', N'Chi phi hang thang', 1),
    ((SELECT customer_id FROM dbo.customers WHERE id_number = N'012345678902'), N'1000000001', N'Nội bộ', N'Nguyen Van A', N'Anh A', 1),
    ((SELECT customer_id FROM dbo.customers WHERE id_number = N'012345678902'), N'9700001234567890', N'Vietcombank', N'Pham Van D', N'Ngoai ngan hang', 1);
GO

-- Saved bills
INSERT INTO dbo.saved_bills (account_id, biller_name, account_number, nickname, is_active)
VALUES
    ((SELECT account_id FROM dbo.accounts WHERE account_number = N'1000000001'), N'EVN HCMC', N'EVN001', N'Tien dien nha', 1),
    ((SELECT account_id FROM dbo.accounts WHERE account_number = N'1000000001'), N'VNPT', N'VNPT001', N'Mang internet', 1),
    ((SELECT account_id FROM dbo.accounts WHERE account_number = N'2000000001'), N'SAIGON WATER', N'WATER001', N'Tien nuoc', 1);
GO

-- Login history
INSERT INTO dbo.login_history (user_id, ip_address, device_info, status, failure_reason)
VALUES
    ((SELECT user_id FROM dbo.users WHERE username = N'customer1'), N'127.0.0.1', N'Chrome / Windows', N'SUCCESS', NULL),
    ((SELECT user_id FROM dbo.users WHERE username = N'customer2'), N'127.0.0.1', N'Chrome / Windows', N'SUCCESS', NULL),
    ((SELECT user_id FROM dbo.users WHERE username = N'customer3'), N'127.0.0.1', N'Chrome / Windows', N'FAILED', N'Sai mat khau');
GO

-- Audit logs
INSERT INTO dbo.audit_logs (user_id, action, entity_type, entity_id, old_value, new_value, description, ip_address)
VALUES
    ((SELECT user_id FROM dbo.users WHERE username = N'admin'), N'SEED_INITIAL_DATA', N'users', NULL, NULL, N'Initial seed data loaded', N'Seed inserted users', N'127.0.0.1'),
    ((SELECT user_id FROM dbo.users WHERE username = N'admin'), N'SEED_INITIAL_DATA', N'accounts', NULL, NULL, N'Accounts and demo transactions loaded', N'Seed inserted accounts and transactions', N'127.0.0.1');
GO

-- Notifications
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
