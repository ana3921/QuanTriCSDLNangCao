package com.banking.repository;

import com.banking.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Integer> {
    List<Beneficiary> findByCustomerCustomerIdOrderByCreatedAtDesc(Integer customerId);
    List<Beneficiary> findByCustomerCustomerIdAndIsActiveOrderByCreatedAtDesc(Integer customerId, Boolean isActive);
    Optional<Beneficiary> findByBeneficiaryIdAndCustomerCustomerId(Integer beneficiaryId, Integer customerId);
    Optional<Beneficiary> findByCustomerCustomerIdAndAccountNumberAndBankName(Integer customerId, String accountNumber, String bankName);
}