package com.banking.service;

import com.banking.dto.CardRequest;
import com.banking.dto.CardResponse;
import com.banking.entity.Card;
import com.banking.entity.Customer;
import com.banking.repository.CardRepository;
import com.banking.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CardService cardService;

    private Customer testCustomer;
    private Card testCard;
    private CardRequest testRequest;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setCustomerId(1);
        testCustomer.setFullName("John Doe");
        testCustomer.setIdNumber("ID001");
        testCustomer.setPhone("0123456789");
        testCustomer.setKycStatus("VERIFIED");

        testCard = new Card();
        testCard.setCardId(1);
        testCard.setCustomer(testCustomer);
        testCard.setCardNumber("4532015112830366");
        testCard.setCardType("credit");
        testCard.setCardholderName("John Doe");
        testCard.setExpiryMonth(12);
        testCard.setExpiryYear(2025);
        testCard.setStatus("active");
        testCard.setIsActive(true);

        testRequest = new CardRequest();
        testRequest.setCustomerId(1);
        testRequest.setCardNumber("4532015112830366");
        testRequest.setCardType("credit");
        testRequest.setCardholderName("John Doe");
        testRequest.setExpiryMonth(12);
        testRequest.setExpiryYear(2025);
        testRequest.setCvv("123");
    }

    @Test
    void testCreateCard() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(testCustomer));
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);

        CardResponse response = cardService.create(testRequest);

        assertNotNull(response);
        assertEquals("John Doe", response.getCardholderName());
        assertEquals("active", response.getStatus());
        verify(customerRepository, times(1)).findById(1);
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void testGetCardById() {
        when(cardRepository.findById(1)).thenReturn(Optional.of(testCard));

        CardResponse response = cardService.getById(1);

        assertNotNull(response);
        assertEquals("4532015112830366", response.getCardNumber());
        verify(cardRepository, times(1)).findById(1);
    }

    @Test
    void testGetCardByIdNotFound() {
        when(cardRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cardService.getById(999));
    }

    @Test
    void testDeleteCard() {
        when(cardRepository.findById(1)).thenReturn(Optional.of(testCard));
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);

        cardService.delete(1);

        verify(cardRepository, times(1)).findById(1);
        verify(cardRepository, times(1)).save(any(Card.class));
        assertFalse(testCard.getIsActive());
        assertEquals("inactive", testCard.getStatus());
    }
}
