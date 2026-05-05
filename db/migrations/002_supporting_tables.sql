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
    customer_id INT NOT NULL,
    from_account_id INT NOT NULL,
    bill_type NVARCHAR(30) NOT NULL,
    provider_name NVARCHAR(100) NOT NULL,
    customer_code NVARCHAR(50) NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    status NVARCHAR(20) NOT NULL CONSTRAINT df_bill_payments_status DEFAULT N'SUCCESS',
    transaction_id INT NULL,
    paid_at DATETIME2 NOT NULL CONSTRAINT df_bill_payments_paid_at DEFAULT SYSUTCDATETIME(),
    CONSTRAINT fk_bill_payments_customers FOREIGN KEY (customer_id) REFERENCES dbo.customers(customer_id),
    CONSTRAINT fk_bill_payments_accounts FOREIGN KEY (from_account_id) REFERENCES dbo.accounts(account_id),
    CONSTRAINT fk_bill_payments_transactions FOREIGN KEY (transaction_id) REFERENCES dbo.transactions(transaction_id),
    CONSTRAINT ck_bill_payments_bill_type CHECK (bill_type IN (N'ELECTRICITY', N'WATER', N'INTERNET', N'PHONE', N'TUITION', N'INSURANCE', N'CREDIT_CARD', N'LOAN')),
    CONSTRAINT ck_bill_payments_status CHECK (status IN (N'PENDING', N'SUCCESS', N'FAILED', N'CANCELLED'))
);
GO

CREATE TABLE dbo.saved_bills (
    saved_bill_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_saved_bills PRIMARY KEY,
    customer_id INT NOT NULL,
    bill_type NVARCHAR(30) NOT NULL,
    provider_name NVARCHAR(100) NOT NULL,
    customer_code NVARCHAR(50) NOT NULL,
    nickname NVARCHAR(100) NULL,
    is_active BIT NOT NULL CONSTRAINT df_saved_bills_is_active DEFAULT 1,
    created_at DATETIME2 NOT NULL CONSTRAINT df_saved_bills_created_at DEFAULT SYSUTCDATETIME(),
    CONSTRAINT fk_saved_bills_customers FOREIGN KEY (customer_id) REFERENCES dbo.customers(customer_id),
    CONSTRAINT uq_saved_bills_customer_provider UNIQUE (customer_id, bill_type, provider_name, customer_code)
);
GO

CREATE TABLE dbo.cards (
    card_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_cards PRIMARY KEY,
    card_number NVARCHAR(20) NOT NULL,
    account_id INT NOT NULL,
    card_type NVARCHAR(20) NOT NULL,
    status NVARCHAR(20) NOT NULL CONSTRAINT df_cards_status DEFAULT N'ACTIVE',
    expiry_date DATE NOT NULL,
    daily_limit DECIMAL(18,2) NOT NULL CONSTRAINT df_cards_daily_limit DEFAULT 50000000.00,
    allow_international BIT NOT NULL CONSTRAINT df_cards_allow_international DEFAULT 0,
    allow_online BIT NOT NULL CONSTRAINT df_cards_allow_online DEFAULT 1,
    issued_at DATETIME2 NOT NULL CONSTRAINT df_cards_issued_at DEFAULT SYSUTCDATETIME(),
    CONSTRAINT uq_cards_card_number UNIQUE (card_number),
    CONSTRAINT fk_cards_accounts FOREIGN KEY (account_id) REFERENCES dbo.accounts(account_id),
    CONSTRAINT ck_cards_card_type CHECK (card_type IN (N'DEBIT', N'CREDIT')),
    CONSTRAINT ck_cards_status CHECK (status IN (N'ACTIVE', N'LOCKED', N'EXPIRED', N'CANCELLED'))
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
    log_id INT IDENTITY(1,1) NOT NULL CONSTRAINT pk_audit_logs PRIMARY KEY,
    user_id INT NULL,
    action NVARCHAR(100) NOT NULL,
    target_table NVARCHAR(50) NULL,
    target_id INT NULL,
    old_value NVARCHAR(MAX) NULL,
    new_value NVARCHAR(MAX) NULL,
    ip_address NVARCHAR(50) NULL,
    created_at DATETIME2 NOT NULL CONSTRAINT df_audit_logs_created_at DEFAULT SYSUTCDATETIME(),
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
CREATE INDEX ix_bill_payments_customer_id ON dbo.bill_payments(customer_id);
CREATE INDEX ix_bill_payments_from_account_id ON dbo.bill_payments(from_account_id);
CREATE INDEX ix_cards_account_id ON dbo.cards(account_id);
CREATE INDEX ix_login_history_user_id ON dbo.login_history(user_id);
CREATE INDEX ix_audit_logs_user_id ON dbo.audit_logs(user_id);
CREATE INDEX ix_interest_rates_term_months ON dbo.interest_rates(term_months);
GO
