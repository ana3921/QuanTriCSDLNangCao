package com.banking.service;

import com.banking.dto.CardRequest;
import com.banking.dto.CardResponse;
import com.banking.entity.Card;
import com.banking.entity.Customer;
import com.banking.repository.CardRepository;
import com.banking.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public CardResponse create(CardRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Card card = new Card();
        card.setCustomer(customer);
        card.setCardNumber(request.getCardNumber());
        card.setCardType(request.getCardType());
        card.setCardholderName(request.getCardholderName());
        card.setExpiryMonth(request.getExpiryMonth());
        card.setExpiryYear(request.getExpiryYear());
        card.setCvv(request.getCvv());
        card.setStatus("active");
        card.setIsActive(true);
        card.setCreatedAt(LocalDateTime.now());

        Card saved = cardRepository.save(card);
        return mapToResponse(saved);
    }

    public CardResponse update(Integer cardId, CardRequest request) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        card.setCardNumber(request.getCardNumber());
        card.setCardholderName(request.getCardholderName());
        card.setExpiryMonth(request.getExpiryMonth());
        card.setExpiryYear(request.getExpiryYear());
        card.setCvv(request.getCvv());
        card.setUpdatedAt(LocalDateTime.now());

        Card updated = cardRepository.save(card);
        return mapToResponse(updated);
    }

    public void delete(Integer cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        card.setIsActive(false);
        card.setStatus("inactive");
        card.setUpdatedAt(LocalDateTime.now());
        cardRepository.save(card);
    }

    public CardResponse getById(Integer cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        return mapToResponse(card);
    }

    public List<CardResponse> getByCustomer(Integer customerId) {
        return cardRepository.findByCustomerCustomerIdAndIsActive(customerId, true)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CardResponse> getAll(Integer customerId) {
        return cardRepository.findByCustomerCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CardResponse mapToResponse(Card card) {
        return new CardResponse(
                card.getCardId(),
                card.getCustomer().getCustomerId(),
                card.getCardNumber(),
                card.getCardType(),
                card.getCardholderName(),
                card.getExpiryMonth(),
                card.getExpiryYear(),
                card.getStatus(),
                card.getCreatedAt(),
                card.getUpdatedAt()
        );
    }
}
