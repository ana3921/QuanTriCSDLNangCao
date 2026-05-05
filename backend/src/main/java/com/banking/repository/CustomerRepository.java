package com.banking.repository;

import com.banking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByUserUserId(Integer userId);
    Optional<Customer> findByPhone(String phone);
    Optional<Customer> findByIdNumber(String idNumber);
}
