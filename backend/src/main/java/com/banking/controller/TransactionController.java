package com.banking.controller;

import com.banking.dto.TransferRequest;
import com.banking.dto.TransferResponse;
import com.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest request) {
        TransferResponse response = transactionService.transfer(request);
        if (response.getResultCode() == 0) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Map<String, Object>>> getTransactionHistory(@PathVariable("accountId") Integer accountId) {
        List<Map<String, Object>> transactions = transactionService.getTransactionHistory(accountId);
        return ResponseEntity.ok(transactions);
    }

}
