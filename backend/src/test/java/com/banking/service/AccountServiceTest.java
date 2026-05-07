package com.banking.service;

import com.banking.dto.AccountRequest;
import com.banking.dto.AccountResponse;
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
    private AccountRequest testRequest;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setCustomerId(1);
        testCustomer.setFirstName("John");
        testCustomer.setLastName("Doe");

        testAccount = new Account();
        testAccount.setAccountId(1);
        testAccount.setAccountNumber("ACC001");
        testAccount.setAccountType("Savings");
        testAccount.setBalance(BigDecimal.valueOf(5000));
        testAccount.setCustomer(testCustomer);
        testAccount.setIsActive(true);

        testRequest = new AccountRequest();
        testRequest.setCustomerId(1);
        testRequest.setAccountNumber("ACC002");
        testRequest.setAccountType("Checking");
    }

    @Test
    void testGetAccountById() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(testAccount));

        AccountResponse response = accountService.getById(1);

        assertNotNull(response);
        assertEquals("ACC001", response.getAccountNumber());
        verify(accountRepository, times(1)).findById(1);
    }

    @Test
    void testGetAccountByIdNotFound() {
        when(accountRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> accountService.getById(999));
        verify(accountRepository, times(1)).findById(999);
    }

    @Test
    void testCreateAccount() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(testCustomer));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        AccountResponse response = accountService.create(testRequest);

        assertNotNull(response);
        assertEquals("ACC001", response.getAccountNumber());
        verify(customerRepository, times(1)).findById(1);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testCreateAccountCustomerNotFound() {
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> accountService.create(testRequest));
        verify(customerRepository, times(1)).findById(999);
    }

    @Test
    void testGetAccountsByCustomer() {
        when(accountRepository.findByCustomerCustomerIdAndIsActive(1, true))
                .thenReturn(java.util.Arrays.asList(testAccount));

        java.util.List<AccountResponse> responses = accountService.getByCustomer(1);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(accountRepository, times(1)).findByCustomerCustomerIdAndIsActive(1, true);
    }
}
