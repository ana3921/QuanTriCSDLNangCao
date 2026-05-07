package com.banking.service;

import com.banking.dto.AccountDTO;
import com.banking.entity.Account;
import com.banking.entity.Customer;
import com.banking.repository.AccountRepository;
import com.banking.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountService accountService;

    private Customer testCustomer;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setCustomerId(1);
        testCustomer.setFullName("John Doe");
        testCustomer.setIdNumber("ID001");
        testCustomer.setPhone("0123456789");
        testCustomer.setKycStatus("VERIFIED");

        testAccount = new Account();
        testAccount.setAccountId(1);
        testAccount.setAccountNumber("ACC001");
        testAccount.setAccountType("Savings");
        testAccount.setBalance(BigDecimal.valueOf(5000));
        testAccount.setCustomer(testCustomer);
        testAccount.setCurrency("VND");
        testAccount.setStatus("ACTIVE");
        testAccount.setIsDefault(true);
        testAccount.setDailyLimit(BigDecimal.valueOf(1000000));
    }

    @Test
    void testGetAccountByNumber() {
        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(testAccount));

        AccountDTO response = accountService.getAccountByNumber("ACC001");

        assertNotNull(response);
        assertEquals("ACC001", response.getAccountNumber());
        verify(accountRepository, times(1)).findByAccountNumber("ACC001");
    }

    @Test
    void testGetAccountByNumberNotFound() {
        when(accountRepository.findByAccountNumber("MISSING")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> accountService.getAccountByNumber("MISSING"));
        verify(accountRepository, times(1)).findByAccountNumber("MISSING");
    }

    @Test
    void testGetAccountsByCustomerId() {
        when(accountRepository.findByCustomerCustomerId(1)).thenReturn(java.util.Arrays.asList(testAccount));

        java.util.List<AccountDTO> responses = accountService.getAccountsByCustomerId(1);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("ACC001", responses.get(0).getAccountNumber());
        verify(accountRepository, times(1)).findByCustomerCustomerId(1);
    }

    @Test
    void testGetActiveAccountsByCustomerId() {
        when(accountRepository.findByCustomerCustomerIdAndStatus(1, "ACTIVE"))
                .thenReturn(java.util.Arrays.asList(testAccount));

        java.util.List<AccountDTO> responses = accountService.getActiveAccountsByCustomerId(1);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("ACTIVE", responses.get(0).getStatus());
        verify(accountRepository, times(1)).findByCustomerCustomerIdAndStatus(1, "ACTIVE");
    }
}
