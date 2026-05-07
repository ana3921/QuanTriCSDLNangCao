package com.banking.service;

import com.banking.dto.BillPaymentRequest;
import com.banking.dto.BillPaymentResponse;
import com.banking.entity.Account;
import com.banking.entity.BillPayment;
import com.banking.repository.AccountRepository;
import com.banking.repository.BillPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillPaymentService {
    @Autowired
    private BillPaymentRepository billPaymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    public BillPaymentResponse create(BillPaymentRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BillPayment billPayment = new BillPayment();
        billPayment.setAccount(account);
        billPayment.setBillerName(request.getBillerName());
        billPayment.setAmount(request.getAmount());
        billPayment.setDueDate(request.getDueDate());
        billPayment.setPaymentDate(LocalDateTime.now());
        billPayment.setStatus("paid");
        billPayment.setReference(request.getReference());
        billPayment.setNotes(request.getNotes());
        billPayment.setIsActive(true);
        billPayment.setCreatedAt(LocalDateTime.now());

        BillPayment saved = billPaymentRepository.save(billPayment);
        return mapToResponse(saved);
    }

    public BillPaymentResponse update(Integer billPaymentId, BillPaymentRequest request) {
        BillPayment billPayment = billPaymentRepository.findById(billPaymentId)
                .orElseThrow(() -> new RuntimeException("Bill payment not found"));

        billPayment.setBillerName(request.getBillerName());
        billPayment.setAmount(request.getAmount());
        billPayment.setDueDate(request.getDueDate());
        billPayment.setReference(request.getReference());
        billPayment.setNotes(request.getNotes());
        billPayment.setUpdatedAt(LocalDateTime.now());

        BillPayment updated = billPaymentRepository.save(billPayment);
        return mapToResponse(updated);
    }

    public void delete(Integer billPaymentId) {
        BillPayment billPayment = billPaymentRepository.findById(billPaymentId)
                .orElseThrow(() -> new RuntimeException("Bill payment not found"));
        billPayment.setIsActive(false);
        billPayment.setUpdatedAt(LocalDateTime.now());
        billPaymentRepository.save(billPayment);
    }

    public BillPaymentResponse getById(Integer billPaymentId) {
        BillPayment billPayment = billPaymentRepository.findById(billPaymentId)
                .orElseThrow(() -> new RuntimeException("Bill payment not found"));
        return mapToResponse(billPayment);
    }

    public List<BillPaymentResponse> getByAccount(Integer accountId) {
        return billPaymentRepository.findByAccountAccountIdAndIsActive(accountId, true)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BillPaymentResponse> getAll(Integer accountId) {
        return billPaymentRepository.findByAccountAccountId(accountId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private BillPaymentResponse mapToResponse(BillPayment billPayment) {
        return new BillPaymentResponse(
                billPayment.getBillPaymentId(),
                billPayment.getAccount().getAccountId(),
                billPayment.getBillerName(),
                billPayment.getAmount(),
                billPayment.getDueDate(),
                billPayment.getPaymentDate(),
                billPayment.getStatus(),
                billPayment.getReference(),
                billPayment.getNotes(),
                billPayment.getCreatedAt(),
                billPayment.getUpdatedAt()
        );
    }
}
