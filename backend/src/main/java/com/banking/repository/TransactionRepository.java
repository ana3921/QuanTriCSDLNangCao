package com.banking.repository;

import com.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Optional<Transaction> findByTransactionCode(String transactionCode);
    List<Transaction> findByFromAccountAccountIdOrderByCreatedAtDesc(Integer fromAccountId);
    List<Transaction> findByToAccountAccountIdOrderByCreatedAtDesc(Integer toAccountId);

    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount.accountId = :accountId OR t.toAccount.accountId = :accountId) ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(Integer accountId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.fromAccount.accountId = :accountId AND t.transactionType IN ('TRANSFER', 'WITHDRAWAL', 'PAYMENT') AND t.status = 'SUCCESS' AND t.createdAt >= :startOfDay AND t.createdAt < :endOfDay")
    Long getTodaySpentAmount(Integer accountId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
