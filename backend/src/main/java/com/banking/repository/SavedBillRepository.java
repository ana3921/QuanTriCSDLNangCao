package com.banking.repository;

import com.banking.entity.SavedBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedBillRepository extends JpaRepository<SavedBill, Integer> {
    List<SavedBill> findByAccountAccountIdAndIsActive(Integer accountId, Boolean isActive);
    List<SavedBill> findByAccountAccountId(Integer accountId);
}
