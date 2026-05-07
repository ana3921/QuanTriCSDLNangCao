package com.banking.service;

import com.banking.dto.BillPaymentRequest;
import com.banking.dto.BillPaymentResponse;
import com.banking.entity.Account;
import com.banking.entity.BillPayment;
import com.banking.repository.AccountRepository;
import com.banking.repository.BillPaymentRepository;
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
public class BillPaymentServiceTest {

    @Mock
    private BillPaymentRepository billPaymentRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private BillPaymentService billPaymentService;

    private Account testAccount;
    private BillPayment testBillPayment;
    private BillPaymentRequest testRequest;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setAccountId(1);
        testAccount.setAccountNumber("ACC001");

        testBillPayment = new BillPayment();
        testBillPayment.setBillPaymentId(1);
        testBillPayment.setAccount(testAccount);
        testBillPayment.setBillerName("Electric Company");
        testBillPayment.setAmount(BigDecimal.valueOf(150));
        testBillPayment.setStatus("paid");
        testBillPayment.setIsActive(true);

        testRequest = new BillPaymentRequest();
        testRequest.setAccountId(1);
        testRequest.setBillerName("Water Company");
        testRequest.setAmount(BigDecimal.valueOf(100));
    }

    @Test
    void testCreateBillPayment() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(testAccount));
        when(billPaymentRepository.save(any(BillPayment.class))).thenReturn(testBillPayment);

        BillPaymentResponse response = billPaymentService.create(testRequest);

        assertNotNull(response);
        assertEquals("Electric Company", response.getBillerName());
        verify(accountRepository, times(1)).findById(1);
        verify(billPaymentRepository, times(1)).save(any(BillPayment.class));
    }

    @Test
    void testGetBillPaymentById() {
        when(billPaymentRepository.findById(1)).thenReturn(Optional.of(testBillPayment));

        BillPaymentResponse response = billPaymentService.getById(1);

        assertNotNull(response);
        assertEquals("Electric Company", response.getBillerName());
        assertEquals(BigDecimal.valueOf(150), response.getAmount());
        verify(billPaymentRepository, times(1)).findById(1);
    }

    @Test
    void testGetBillPaymentByIdNotFound() {
        when(billPaymentRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> billPaymentService.getById(999));
        verify(billPaymentRepository, times(1)).findById(999);
    }

    @Test
    void testDeleteBillPayment() {
        when(billPaymentRepository.findById(1)).thenReturn(Optional.of(testBillPayment));
        when(billPaymentRepository.save(any(BillPayment.class))).thenReturn(testBillPayment);

        billPaymentService.delete(1);

        verify(billPaymentRepository, times(1)).findById(1);
        verify(billPaymentRepository, times(1)).save(any(BillPayment.class));
        assertFalse(testBillPayment.getIsActive());
    }
}
