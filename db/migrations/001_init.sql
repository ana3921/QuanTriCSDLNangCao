IF DB_ID(N'banking_db') IS NULL
BEGIN
    CREATE DATABASE banking_db
    COLLATE Vietnamese_CI_AS;
END
GO

USE banking_db;
GO

IF OBJECT_ID(N'dbo.transactions', N'U') IS NOT NULL DROP TABLE dbo.transactions;
IF OBJECT_ID(N'dbo.accounts', N'U') IS NOT NULL DROP TABLE dbo.accounts;
IF OBJECT_ID(N'dbo.customers', N'U') IS NOT NULL DROP TABLE dbo.customers;
IF OBJECT_ID(N'dbo.users', N'U') IS NOT NULL DROP TABLE dbo.users;
GO

CREATE TABLE dbo.users (
    user_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_users PRIMARY KEY,
    username NVARCHAR(50) NOT NULL,
    password_hash NVARCHAR(255) NOT NULL,
    email NVARCHAR(100) NOT NULL,
    role NVARCHAR(20) NOT NULL CONSTRAINT df_users_role DEFAULT N'CUSTOMER',
    is_active BIT NOT NULL CONSTRAINT df_users_is_active DEFAULT 1,
    created_at DATETIME2 NOT NULL CONSTRAINT df_users_created_at DEFAULT SYSUTCDATETIME(),
    updated_at DATETIME2 NOT NULL CONSTRAINT df_users_updated_at DEFAULT SYSUTCDATETIME(),
    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT ck_users_role CHECK (role IN (N'CUSTOMER', N'EMPLOYEE', N'ADMIN'))
);
GO

CREATE TABLE dbo.customers (
    customer_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_customers PRIMARY KEY,
    user_id INT NOT NULL,
    full_name NVARCHAR(100) NOT NULL,
    id_number NVARCHAR(20) NOT NULL,
    phone NVARCHAR(15) NOT NULL,
    date_of_birth DATE NOT NULL,
    address NVARCHAR(255) NULL,
    avatar_url NVARCHAR(500) NULL,
    kyc_status NVARCHAR(20) NOT NULL CONSTRAINT df_customers_kyc_status DEFAULT N'PENDING',
    CONSTRAINT uq_customers_user_id UNIQUE (user_id),
    CONSTRAINT uq_customers_id_number UNIQUE (id_number),
    CONSTRAINT uq_customers_phone UNIQUE (phone),
    CONSTRAINT fk_customers_users FOREIGN KEY (user_id) REFERENCES dbo.users(user_id),
    CONSTRAINT ck_customers_kyc_status CHECK (kyc_status IN (N'PENDING', N'VERIFIED', N'REJECTED'))
);
GO

CREATE TABLE dbo.accounts (
    account_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_accounts PRIMARY KEY,
    account_number NVARCHAR(20) NOT NULL,
    customer_id INT NOT NULL,
    account_type NVARCHAR(20) NOT NULL CONSTRAINT df_accounts_account_type DEFAULT N'PAYMENT',
    balance DECIMAL(18,2) NOT NULL CONSTRAINT df_accounts_balance DEFAULT 0.00,
    currency NCHAR(3) NOT NULL CONSTRAINT df_accounts_currency DEFAULT N'VND',
    status NVARCHAR(20) NOT NULL CONSTRAINT df_accounts_status DEFAULT N'ACTIVE',
    is_default BIT NOT NULL CONSTRAINT df_accounts_is_default DEFAULT 0,
    daily_limit DECIMAL(18,2) NOT NULL CONSTRAINT df_accounts_daily_limit DEFAULT 100000000.00,
    opened_at DATETIME2 NOT NULL CONSTRAINT df_accounts_opened_at DEFAULT SYSUTCDATETIME(),
    closed_at DATETIME2 NULL,
    term_months INT NULL,
    interest_rate DECIMAL(5,2) NULL,
    maturity_date DATE NULL,
    CONSTRAINT uq_accounts_account_number UNIQUE (account_number),
    CONSTRAINT fk_accounts_customers FOREIGN KEY (customer_id) REFERENCES dbo.customers(customer_id),
    CONSTRAINT ck_accounts_account_type CHECK (account_type IN (N'PAYMENT', N'SAVINGS')),
    CONSTRAINT ck_accounts_status CHECK (status IN (N'ACTIVE', N'LOCKED', N'CLOSED')),
    CONSTRAINT ck_accounts_balance CHECK (balance >= 0)
);
GO

CREATE TABLE dbo.transactions (
    transaction_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_transactions PRIMARY KEY,
    transaction_code NVARCHAR(30) NOT NULL,
    from_account_id INT NULL,
    to_account_id INT NULL,
    amount DECIMAL(18,2) NOT NULL,
    fee DECIMAL(18,2) NOT NULL CONSTRAINT df_transactions_fee DEFAULT 0.00,
    transaction_type NVARCHAR(20) NOT NULL,
    status NVARCHAR(20) NOT NULL CONSTRAINT df_transactions_status DEFAULT N'PENDING',
    description NVARCHAR(255) NULL,
    reference_code NVARCHAR(50) NULL,
    is_suspicious BIT NOT NULL CONSTRAINT df_transactions_is_suspicious DEFAULT 0,
    created_at DATETIME2 NOT NULL CONSTRAINT df_transactions_created_at DEFAULT SYSUTCDATETIME(),
    completed_at DATETIME2 NULL,
    CONSTRAINT uq_transactions_transaction_code UNIQUE (transaction_code),
    CONSTRAINT fk_transactions_from_account FOREIGN KEY (from_account_id) REFERENCES dbo.accounts(account_id),
    CONSTRAINT fk_transactions_to_account FOREIGN KEY (to_account_id) REFERENCES dbo.accounts(account_id),
    CONSTRAINT ck_transactions_amount CHECK (amount > 0),
    CONSTRAINT ck_transactions_type CHECK (transaction_type IN (N'TRANSFER', N'DEPOSIT', N'WITHDRAWAL', N'FEE', N'PAYMENT', N'REVERSAL')),
    CONSTRAINT ck_transactions_status CHECK (status IN (N'PENDING', N'SUCCESS', N'FAILED', N'CANCELLED', N'REVERSED'))
);
GO

CREATE TABLE dbo.notifications (
    notification_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_notifications PRIMARY KEY,
    user_id INT NOT NULL,
    title NVARCHAR(200) NOT NULL,
    content NVARCHAR(1000) NOT NULL,
    type NVARCHAR(30) NOT NULL,
    is_read BIT NOT NULL CONSTRAINT df_notifications_is_read DEFAULT 0,
    related_tx_id INT NULL,
    created_at DATETIME2 NOT NULL CONSTRAINT df_notifications_created_at DEFAULT SYSUTCDATETIME(),
    CONSTRAINT fk_notifications_users FOREIGN KEY (user_id) REFERENCES dbo.users(user_id),
    CONSTRAINT fk_notifications_transactions FOREIGN KEY (related_tx_id) REFERENCES dbo.transactions(transaction_id),
    CONSTRAINT ck_notifications_type CHECK (type IN (N'TRANSACTION', N'SECURITY', N'SYSTEM', N'PROMOTION', N'REMINDER'))
);
GO

CREATE INDEX ix_users_email ON dbo.users(email);
CREATE INDEX ix_customers_phone ON dbo.customers(phone);
CREATE INDEX ix_customers_id_number ON dbo.customers(id_number);
CREATE INDEX ix_accounts_customer_id ON dbo.accounts(customer_id);
CREATE INDEX ix_accounts_account_number ON dbo.accounts(account_number);
CREATE INDEX ix_transactions_created_at ON dbo.transactions(created_at);
CREATE INDEX ix_transactions_from_account_id ON dbo.transactions(from_account_id);
CREATE INDEX ix_transactions_to_account_id ON dbo.transactions(to_account_id);
CREATE INDEX ix_notifications_user_id ON dbo.notifications(user_id);
CREATE INDEX ix_notifications_created_at ON dbo.notifications(created_at);
GO

IF EXISTS (SELECT 1 FROM sys.server_principals WHERE name = N'banking_user')
BEGIN
    IF NOT EXISTS (SELECT 1 FROM sys.database_principals WHERE name = N'banking_user')
    BEGIN
        CREATE USER [banking_user] FOR LOGIN [banking_user];
    END;

    IF NOT EXISTS (
        SELECT 1
        FROM sys.database_role_members drm
        INNER JOIN sys.database_principals r ON r.principal_id = drm.role_principal_id
        INNER JOIN sys.database_principals m ON m.principal_id = drm.member_principal_id
        WHERE r.name = N'db_owner' AND m.name = N'banking_user'
    )
    BEGIN
        ALTER ROLE db_owner ADD MEMBER [banking_user];
    END;
END;
GO
