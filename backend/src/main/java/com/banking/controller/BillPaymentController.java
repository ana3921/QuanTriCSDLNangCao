package com.banking.controller;

import com.banking.dto.BillPaymentRequest;
import com.banking.dto.BillPaymentResponse;
import com.banking.service.BillPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bill-payments")
@CrossOrigin(origins = "http://localhost:5173")
public class BillPaymentController {
    @Autowired
    private BillPaymentService billPaymentService;

    @PostMapping
    public ResponseEntity<BillPaymentResponse> create(@RequestBody BillPaymentRequest request) {
        BillPaymentResponse response = billPaymentService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{billPaymentId}")
    public ResponseEntity<BillPaymentResponse> update(
            @PathVariable Integer billPaymentId,
            @RequestBody BillPaymentRequest request) {
        BillPaymentResponse response = billPaymentService.update(billPaymentId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{billPaymentId}")
    public ResponseEntity<Void> delete(@PathVariable Integer billPaymentId) {
        billPaymentService.delete(billPaymentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{billPaymentId}")
    public ResponseEntity<BillPaymentResponse> getById(@PathVariable Integer billPaymentId) {
        BillPaymentResponse response = billPaymentService.getById(billPaymentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<BillPaymentResponse>> getByAccount(@PathVariable Integer accountId) {
        List<BillPaymentResponse> response = billPaymentService.getByAccount(accountId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountId}/all")
    public ResponseEntity<List<BillPaymentResponse>> getAll(@PathVariable Integer accountId) {
        List<BillPaymentResponse> response = billPaymentService.getAll(accountId);
        return ResponseEntity.ok(response);
    }
}
