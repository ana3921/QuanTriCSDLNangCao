package com.banking.service;

import com.banking.dto.TransferRequest;
import com.banking.dto.TransferResponse;
import com.banking.entity.Account;
import com.banking.entity.Transaction;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final JdbcTemplate jdbcTemplate;

    public TransferResponse transfer(TransferRequest request) {
        // Call the stored procedure
        String sql = "EXEC sp_transfer_money ?, ?, ?, ?, ?, ?";
        
        Map<String, Object> outParams = new HashMap<>();
        
        try {
            // For simplicity, we'll query the database directly
            Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                    .orElseThrow(() -> new RuntimeException("From account not found"));
            
            Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                    .orElseThrow(() -> new RuntimeException("To account not found"));

            if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
                return new TransferResponse(-3, "Insufficient balance", null);
            }

            fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
            toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            Transaction transaction = new Transaction();
            transaction.setTransactionCode("TXN" + System.currentTimeMillis());
            transaction.setFromAccount(fromAccount);
            transaction.setToAccount(toAccount);
            transaction.setAmount(request.getAmount());
            transaction.setFee(BigDecimal.ZERO);
            transaction.setTransactionType("TRANSFER");
            transaction.setStatus("SUCCESS");
            transaction.setDescription(request.getDescription());
            transaction.setIsSuspicious(false);
            transaction.setCreatedAt(java.time.LocalDateTime.now());
            transaction.setCompletedAt(java.time.LocalDateTime.now());

            transactionRepository.save(transaction);

            return new TransferResponse(0, "Transfer successful", transaction.getTransactionCode());
        } catch (Exception e) {
            return new TransferResponse(-99, e.getMessage(), null);
        }
    }

    public List<Map<String, Object>> getTransactionHistory(Integer accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
        return transactions.stream().map(t -> {
            Map<String, Object> map = new HashMap<>();
            map.put("transactionId", t.getTransactionId());
            map.put("transactionCode", t.getTransactionCode());
            map.put("amount", t.getAmount());
            map.put("type", t.getTransactionType());
            map.put("status", t.getStatus());
            map.put("description", t.getDescription());
            map.put("createdAt", t.getCreatedAt());
            return map;
        }).collect(Collectors.toList());
    }

}
