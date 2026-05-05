package com.banking.controller;

import com.banking.dto.AccountDTO;
import com.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountDTO>> getAccountsByCustomer(@PathVariable Integer customerId) {
        List<AccountDTO> accounts = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountDTO> getAccountByNumber(@PathVariable String accountNumber) {
        AccountDTO account = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/customer/{customerId}/active")
    public ResponseEntity<List<AccountDTO>> getActiveAccounts(@PathVariable Integer customerId) {
        List<AccountDTO> accounts = accountService.getActiveAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }

}
