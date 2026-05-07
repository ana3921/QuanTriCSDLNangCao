USE banking_db;
GO

-- Backup and restore helper scripts for documentation and SSMS execution.
-- Update the backup paths before running on your machine.

-- Full backup
-- BACKUP DATABASE banking_db
-- TO DISK = 'C:\Backup\banking_db_full_YYYYMMDD.bak'
-- WITH COMPRESSION, CHECKSUM, STATS = 10;
-- GO

-- Differential backup
-- BACKUP DATABASE banking_db
-- TO DISK = 'C:\Backup\banking_db_diff_YYYYMMDD_HHMM.bak'
-- WITH DIFFERENTIAL, COMPRESSION, CHECKSUM, STATS = 10;
-- GO

-- Transaction log backup
-- ALTER DATABASE banking_db SET RECOVERY FULL;
-- GO
-- BACKUP LOG banking_db
-- TO DISK = 'C:\Backup\banking_db_log_YYYYMMDD_HHMM.trn'
-- WITH COMPRESSION, CHECKSUM, STATS = 10;
-- GO

-- Restore full backup
-- RESTORE DATABASE banking_db
-- FROM DISK = 'C:\Backup\banking_db_full_YYYYMMDD.bak'
-- WITH REPLACE, RECOVERY;
-- GO

-- Restore full + differential + log to point in time
-- RESTORE DATABASE banking_db
-- FROM DISK = 'C:\Backup\banking_db_full_YYYYMMDD.bak'
-- WITH REPLACE, NORECOVERY;
-- GO
-- RESTORE DATABASE banking_db
-- FROM DISK = 'C:\Backup\banking_db_diff_YYYYMMDD_HHMM.bak'
-- WITH NORECOVERY;
-- GO
-- RESTORE LOG banking_db
-- FROM DISK = 'C:\Backup\banking_db_log_YYYYMMDD_HHMM.trn'
-- WITH STOPAT = '2026-05-07T12:00:00', RECOVERY;
-- GO

-- Verify backup file integrity
-- RESTORE VERIFYONLY
-- FROM DISK = 'C:\Backup\banking_db_full_YYYYMMDD.bak';
-- GO
