package com.banking.repository;

import com.banking.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> findByCustomerCustomerIdAndIsActive(Integer customerId, Boolean isActive);
    List<Card> findByCustomerCustomerId(Integer customerId);
    List<Card> findByStatus(String status);
}
