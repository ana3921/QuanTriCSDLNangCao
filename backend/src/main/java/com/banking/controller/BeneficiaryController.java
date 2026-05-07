package com.banking.controller;

import com.banking.dto.BeneficiaryRequest;
import com.banking.dto.BeneficiaryResponse;
import com.banking.service.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BeneficiaryResponse>> getBeneficiariesByCustomer(@PathVariable("customerId") Integer customerId) {
        return ResponseEntity.ok(beneficiaryService.getBeneficiariesByCustomerId(customerId));
    }

    @GetMapping("/customer/{customerId}/active")
    public ResponseEntity<List<BeneficiaryResponse>> getActiveBeneficiariesByCustomer(@PathVariable("customerId") Integer customerId) {
        return ResponseEntity.ok(beneficiaryService.getActiveBeneficiariesByCustomerId(customerId));
    }

    @GetMapping("/{beneficiaryId}")
    public ResponseEntity<BeneficiaryResponse> getBeneficiaryById(@PathVariable("beneficiaryId") Integer beneficiaryId) {
        return ResponseEntity.ok(beneficiaryService.getBeneficiaryById(beneficiaryId));
    }

    @PostMapping
    public ResponseEntity<BeneficiaryResponse> createBeneficiary(@RequestBody BeneficiaryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaryService.createBeneficiary(request));
    }

    @PutMapping("/{beneficiaryId}")
    public ResponseEntity<BeneficiaryResponse> updateBeneficiary(
            @PathVariable("beneficiaryId") Integer beneficiaryId,
            @RequestBody BeneficiaryRequest request) {
        return ResponseEntity.ok(beneficiaryService.updateBeneficiary(beneficiaryId, request));
    }

    @DeleteMapping("/{beneficiaryId}")
    public ResponseEntity<Void> deactivateBeneficiary(@PathVariable("beneficiaryId") Integer beneficiaryId) {
        beneficiaryService.deactivateBeneficiary(beneficiaryId);
        return ResponseEntity.noContent().build();
    }
}