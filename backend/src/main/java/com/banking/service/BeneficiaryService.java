package com.banking.service;

import com.banking.dto.BeneficiaryRequest;
import com.banking.dto.BeneficiaryResponse;
import com.banking.entity.Beneficiary;
import com.banking.entity.Customer;
import com.banking.repository.BeneficiaryRepository;
import com.banking.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final CustomerRepository customerRepository;

    public List<BeneficiaryResponse> getBeneficiariesByCustomerId(Integer customerId) {
        return beneficiaryRepository.findByCustomerCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<BeneficiaryResponse> getActiveBeneficiariesByCustomerId(Integer customerId) {
        return beneficiaryRepository.findByCustomerCustomerIdAndIsActiveOrderByCreatedAtDesc(customerId, true)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BeneficiaryResponse getBeneficiaryById(Integer beneficiaryId) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found"));
        return toResponse(beneficiary);
    }

    @Transactional
    public BeneficiaryResponse createBeneficiary(BeneficiaryRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        String bankName = normalizeBankName(request.getBankName());
        String accountNumber = request.getAccountNumber().trim();

        beneficiaryRepository.findByCustomerCustomerIdAndAccountNumberAndBankName(
                customer.getCustomerId(), accountNumber, bankName)
                .ifPresent(existing -> { throw new RuntimeException("Beneficiary already exists"); });

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setCustomer(customer);
        beneficiary.setAccountNumber(accountNumber);
        beneficiary.setBankName(bankName);
        beneficiary.setFullName(request.getFullName().trim());
        beneficiary.setNickname(request.getNickname());
        beneficiary.setIsActive(request.getIsActive() == null || request.getIsActive());
        beneficiary.setCreatedAt(LocalDateTime.now());

        return toResponse(beneficiaryRepository.save(beneficiary));
    }

    @Transactional
    public BeneficiaryResponse updateBeneficiary(Integer beneficiaryId, BeneficiaryRequest request) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found"));

        if (request.getAccountNumber() != null) {
            beneficiary.setAccountNumber(request.getAccountNumber().trim());
        }

        if (request.getBankName() != null) {
            beneficiary.setBankName(normalizeBankName(request.getBankName()));
        }

        if (request.getFullName() != null) {
            beneficiary.setFullName(request.getFullName().trim());
        }

        if (request.getNickname() != null) {
            beneficiary.setNickname(request.getNickname().trim());
        }

        if (request.getIsActive() != null) {
            beneficiary.setIsActive(request.getIsActive());
        }

        return toResponse(beneficiaryRepository.save(beneficiary));
    }

    @Transactional
    public void deactivateBeneficiary(Integer beneficiaryId) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found"));
        beneficiary.setIsActive(false);
        beneficiaryRepository.save(beneficiary);
    }

    private BeneficiaryResponse toResponse(Beneficiary beneficiary) {
        return BeneficiaryResponse.builder()
                .beneficiaryId(beneficiary.getBeneficiaryId())
                .customerId(beneficiary.getCustomer().getCustomerId())
                .accountNumber(beneficiary.getAccountNumber())
                .bankName(beneficiary.getBankName())
                .fullName(beneficiary.getFullName())
                .nickname(beneficiary.getNickname())
                .isActive(beneficiary.getIsActive())
                .createdAt(beneficiary.getCreatedAt())
                .build();
    }

    private String normalizeBankName(String bankName) {
        if (bankName == null || bankName.isBlank()) {
            return "Nội bộ";
        }
        return bankName.trim();
    }
}