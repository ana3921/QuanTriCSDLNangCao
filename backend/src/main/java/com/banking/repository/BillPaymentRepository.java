package com.banking.repository;

import com.banking.entity.BillPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillPaymentRepository extends JpaRepository<BillPayment, Integer> {
    List<BillPayment> findByAccountAccountIdAndIsActive(Integer accountId, Boolean isActive);
    List<BillPayment> findByAccountAccountId(Integer accountId);
    List<BillPayment> findByStatus(String status);
}
