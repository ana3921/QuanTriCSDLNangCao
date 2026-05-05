package com.banking.repository;

import com.banking.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByCustomerCustomerId(Integer customerId);
    List<Account> findByCustomerCustomerIdAndStatus(Integer customerId, String status);
}
