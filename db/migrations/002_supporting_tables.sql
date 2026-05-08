USE banking_db;
GO

IF OBJECT_ID(N'dbo.system_config', N'U') IS NOT NULL DROP TABLE dbo.system_config;
IF OBJECT_ID(N'dbo.interest_rates', N'U') IS NOT NULL DROP TABLE dbo.interest_rates;
IF OBJECT_ID(N'dbo.audit_logs', N'U') IS NOT NULL DROP TABLE dbo.audit_logs;
IF OBJECT_ID(N'dbo.login_history', N'U') IS NOT NULL DROP TABLE dbo.login_history;
IF OBJECT_ID(N'dbo.cards', N'U') IS NOT NULL DROP TABLE dbo.cards;
IF OBJECT_ID(N'dbo.saved_bills', N'U') IS NOT NULL DROP TABLE dbo.saved_bills;
IF OBJECT_ID(N'dbo.bill_payments', N'U') IS NOT NULL DROP TABLE dbo.bill_payments;
IF OBJECT_ID(N'dbo.beneficiaries', N'U') IS NOT NULL DROP TABLE dbo.beneficiaries;
GO

CREATE TABLE dbo.beneficiaries (
    beneficiary_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_beneficiaries PRIMARY KEY,
    customer_id INT NOT NULL,
    account_number NVARCHAR(20) NOT NULL,
    bank_name NVARCHAR(100) NOT NULL CONSTRAINT df_beneficiaries_bank_name DEFAULT N'Nội bộ',
    full_name NVARCHAR(100) NOT NULL,
    nickname NVARCHAR(50) NULL,
    is_active BIT NOT NULL CONSTRAINT df_beneficiaries_is_active DEFAULT 1,
    created_at DATETIME2 NOT NULL CONSTRAINT df_beneficiaries_created_at DEFAULT SYSUTCDATETIME(),
    CONSTRAINT fk_beneficiaries_customers FOREIGN KEY (customer_id) REFERENCES dbo.customers(customer_id),
    CONSTRAINT uq_beneficiaries_customer_account UNIQUE (customer_id, account_number, bank_name)
);
GO

CREATE TABLE dbo.bill_payments (
    bill_payment_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_bill_payments PRIMARY KEY,
    account_id INT NOT NULL,
    biller_name NVARCHAR(100) NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    due_date DATETIME2 NULL,
    payment_date DATETIME2 NOT NULL CONSTRAINT df_bill_payments_payment_date DEFAULT SYSUTCDATETIME(),
    status NVARCHAR(20) NOT NULL CONSTRAINT df_bill_payments_status DEFAULT N'PENDING',
    reference NVARCHAR(500) NULL,
    notes NVARCHAR(500) NULL,
    is_active BIT NOT NULL CONSTRAINT df_bill_payments_is_active DEFAULT 1,
    created_at DATETIME2 NOT NULL CONSTRAINT df_bill_payments_created_at DEFAULT SYSUTCDATETIME(),
    updated_at DATETIME2 NULL,
    CONSTRAINT fk_bill_payments_accounts FOREIGN KEY (account_id) REFERENCES dbo.accounts(account_id),
    CONSTRAINT ck_bill_payments_status CHECK (status IN (N'PENDING', N'PAID', N'FAILED', N'CANCELLED'))
);
GO

CREATE TABLE dbo.saved_bills (
    saved_bill_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_saved_bills PRIMARY KEY,
    account_id INT NOT NULL,
    biller_name NVARCHAR(100) NOT NULL,
    account_number NVARCHAR(100) NOT NULL,
    nickname NVARCHAR(100) NULL,
    is_active BIT NOT NULL CONSTRAINT df_saved_bills_is_active DEFAULT 1,
    created_at DATETIME2 NOT NULL CONSTRAINT df_saved_bills_created_at DEFAULT SYSUTCDATETIME(),
    updated_at DATETIME2 NULL,
    CONSTRAINT fk_saved_bills_accounts FOREIGN KEY (account_id) REFERENCES dbo.accounts(account_id)
);
GO

CREATE TABLE dbo.cards (
    card_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_cards PRIMARY KEY,
    customer_id INT NOT NULL,
    card_number NVARCHAR(50) NOT NULL,
    card_type NVARCHAR(50) NOT NULL,
    cardholder_name NVARCHAR(100) NOT NULL,
    expiry_month INT NOT NULL,
    expiry_year INT NOT NULL,
    cvv NVARCHAR(3) NOT NULL,
    status NVARCHAR(20) NOT NULL CONSTRAINT df_cards_status DEFAULT N'ACTIVE',
    is_active BIT NOT NULL CONSTRAINT df_cards_is_active DEFAULT 1,
    created_at DATETIME2 NOT NULL CONSTRAINT df_cards_created_at DEFAULT SYSUTCDATETIME(),
    updated_at DATETIME2 NULL,
    CONSTRAINT uq_cards_card_number UNIQUE (card_number),
    CONSTRAINT fk_cards_customers FOREIGN KEY (customer_id) REFERENCES dbo.customers(customer_id),
    CONSTRAINT ck_cards_card_type CHECK (card_type IN (N'CREDIT', N'DEBIT', N'PREPAID')),
    CONSTRAINT ck_cards_status CHECK (status IN (N'ACTIVE', N'INACTIVE', N'BLOCKED', N'EXPIRED')),
    CONSTRAINT ck_cards_expiry_month CHECK (expiry_month BETWEEN 1 AND 12),
    CONSTRAINT ck_cards_expiry_year CHECK (expiry_year >= 2000)
);
GO

CREATE TABLE dbo.login_history (
    login_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_login_history PRIMARY KEY,
    user_id INT NOT NULL,
    ip_address NVARCHAR(50) NULL,
    device_info NVARCHAR(255) NULL,
    status NVARCHAR(20) NOT NULL,
    failure_reason NVARCHAR(100) NULL,
    logged_at DATETIME2 NOT NULL CONSTRAINT df_login_history_logged_at DEFAULT SYSUTCDATETIME(),
    CONSTRAINT fk_login_history_users FOREIGN KEY (user_id) REFERENCES dbo.users(user_id),
    CONSTRAINT ck_login_history_status CHECK (status IN (N'SUCCESS', N'FAILED'))
);
GO

CREATE TABLE dbo.audit_logs (
    audit_log_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_audit_logs PRIMARY KEY,
    user_id INT NOT NULL,
    action NVARCHAR(100) NOT NULL,
    entity_type NVARCHAR(50) NOT NULL,
    entity_id INT NULL,
    old_value NVARCHAR(500) NULL,
    new_value NVARCHAR(500) NULL,
    description NVARCHAR(500) NOT NULL,
    ip_address NVARCHAR(50) NULL,
    [timestamp] DATETIME2 NOT NULL CONSTRAINT df_audit_logs_timestamp DEFAULT SYSUTCDATETIME(),
    CONSTRAINT fk_audit_logs_users FOREIGN KEY (user_id) REFERENCES dbo.users(user_id)
);
GO

CREATE TABLE dbo.interest_rates (
    rate_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_interest_rates PRIMARY KEY,
    term_months INT NOT NULL,
    rate_percent DECIMAL(5,2) NOT NULL,
    effective_from DATE NOT NULL,
    effective_to DATE NULL,
    is_active BIT NOT NULL CONSTRAINT df_interest_rates_is_active DEFAULT 1,
    CONSTRAINT uq_interest_rates_term_effective_from UNIQUE (term_months, effective_from),
    CONSTRAINT ck_interest_rates_term_months CHECK (term_months > 0),
    CONSTRAINT ck_interest_rates_rate_percent CHECK (rate_percent >= 0)
);
GO

CREATE TABLE dbo.system_config (
    config_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_system_config PRIMARY KEY,
    config_key NVARCHAR(100) NOT NULL,
    config_value NVARCHAR(500) NOT NULL,
    description NVARCHAR(255) NULL,
    updated_at DATETIME2 NOT NULL CONSTRAINT df_system_config_updated_at DEFAULT SYSUTCDATETIME(),
    CONSTRAINT uq_system_config_config_key UNIQUE (config_key)
);
GO

CREATE INDEX ix_beneficiaries_customer_id ON dbo.beneficiaries(customer_id);
CREATE INDEX ix_bill_payments_account_id ON dbo.bill_payments(account_id);
CREATE INDEX ix_cards_customer_id ON dbo.cards(customer_id);
CREATE INDEX ix_login_history_user_id ON dbo.login_history(user_id);
CREATE INDEX ix_audit_logs_user_id ON dbo.audit_logs(user_id);
CREATE INDEX ix_interest_rates_term_months ON dbo.interest_rates(term_months);
GO
