USE banking_db;
GO

-- Optimization evidence for the report.
-- Run these queries in SSMS with Actual Execution Plan enabled (Ctrl+M).
-- Capture screenshots before and after comparing execution plans / statistics.

SET STATISTICS IO ON;
SET STATISTICS TIME ON;
GO

-- Query 1: account transaction history
SELECT
    t.transaction_id,
    t.transaction_code,
    t.amount,
    t.transaction_type,
    t.status,
    t.created_at,
    t.description
FROM dbo.transactions t
WHERE t.from_account_id = 1
ORDER BY t.created_at DESC;
GO

-- Query 2: monthly transaction aggregation
SELECT
    YEAR(created_at) AS report_year,
    MONTH(created_at) AS report_month,
    transaction_type,
    COUNT(*) AS transaction_count,
    SUM(amount) AS total_amount
FROM dbo.transactions
WHERE status = N'SUCCESS'
  AND created_at >= DATEADD(MONTH, -6, GETDATE())
GROUP BY YEAR(created_at), MONTH(created_at), transaction_type
ORDER BY report_year DESC, report_month DESC;
GO

SET STATISTICS IO OFF;
SET STATISTICS TIME OFF;
GO

-- Report notes:
-- 1. Capture actual execution plan screenshot for each query.
-- 2. Record logical reads / CPU time before and after index usage.
-- 3. Compare Index Seek vs Table Scan / Index Scan in the report.
