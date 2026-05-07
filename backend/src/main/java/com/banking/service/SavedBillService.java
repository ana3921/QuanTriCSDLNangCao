package com.banking.service;

import com.banking.dto.SavedBillRequest;
import com.banking.dto.SavedBillResponse;
import com.banking.entity.Account;
import com.banking.entity.SavedBill;
import com.banking.repository.AccountRepository;
import com.banking.repository.SavedBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavedBillService {
    @Autowired
    private SavedBillRepository savedBillRepository;

    @Autowired
    private AccountRepository accountRepository;

    public SavedBillResponse create(SavedBillRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        SavedBill savedBill = new SavedBill();
        savedBill.setAccount(account);
        savedBill.setBillerName(request.getBillerName());
        savedBill.setAccountNumber(request.getAccountNumber());
        savedBill.setNickname(request.getNickname());
        savedBill.setIsActive(true);
        savedBill.setCreatedAt(LocalDateTime.now());

        SavedBill saved = savedBillRepository.save(savedBill);
        return mapToResponse(saved);
    }

    public SavedBillResponse update(Integer savedBillId, SavedBillRequest request) {
        SavedBill savedBill = savedBillRepository.findById(savedBillId)
                .orElseThrow(() -> new RuntimeException("Saved bill not found"));

        savedBill.setBillerName(request.getBillerName());
        savedBill.setAccountNumber(request.getAccountNumber());
        savedBill.setNickname(request.getNickname());
        savedBill.setUpdatedAt(LocalDateTime.now());

        SavedBill updated = savedBillRepository.save(savedBill);
        return mapToResponse(updated);
    }

    public void delete(Integer savedBillId) {
        SavedBill savedBill = savedBillRepository.findById(savedBillId)
                .orElseThrow(() -> new RuntimeException("Saved bill not found"));
        savedBill.setIsActive(false);
        savedBill.setUpdatedAt(LocalDateTime.now());
        savedBillRepository.save(savedBill);
    }

    public SavedBillResponse getById(Integer savedBillId) {
        SavedBill savedBill = savedBillRepository.findById(savedBillId)
                .orElseThrow(() -> new RuntimeException("Saved bill not found"));
        return mapToResponse(savedBill);
    }

    public List<SavedBillResponse> getByAccount(Integer accountId) {
        return savedBillRepository.findByAccountAccountIdAndIsActive(accountId, true)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<SavedBillResponse> getAll(Integer accountId) {
        return savedBillRepository.findByAccountAccountId(accountId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private SavedBillResponse mapToResponse(SavedBill savedBill) {
        return new SavedBillResponse(
                savedBill.getSavedBillId(),
                savedBill.getAccount().getAccountId(),
                savedBill.getBillerName(),
                savedBill.getAccountNumber(),
                savedBill.getNickname(),
                savedBill.getIsActive(),
                savedBill.getCreatedAt(),
                savedBill.getUpdatedAt()
        );
    }
}
