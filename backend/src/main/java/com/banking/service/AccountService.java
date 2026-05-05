package com.banking.service;

import com.banking.dto.AccountDTO;
import com.banking.entity.Account;
import com.banking.entity.Customer;
import com.banking.repository.AccountRepository;
import com.banking.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public List<AccountDTO> getAccountsByCustomerId(Integer customerId) {
        List<Account> accounts = accountRepository.findByCustomerCustomerId(customerId);
        return accounts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public AccountDTO getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return convertToDTO(account);
    }

    public List<AccountDTO> getActiveAccountsByCustomerId(Integer customerId) {
        List<Account> accounts = accountRepository.findByCustomerCustomerIdAndStatus(customerId, "ACTIVE");
        return accounts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private AccountDTO convertToDTO(Account account) {
        return AccountDTO.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(account.getStatus())
                .isDefault(account.getIsDefault())
                .dailyLimit(account.getDailyLimit())
                .openedAt(account.getOpenedAt())
                .closedAt(account.getClosedAt())
                .termMonths(account.getTermMonths())
                .interestRate(account.getInterestRate())
                .maturityDate(account.getMaturityDate())
                .build();
    }

}
