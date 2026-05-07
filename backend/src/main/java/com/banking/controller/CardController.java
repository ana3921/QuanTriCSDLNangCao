package com.banking.controller;

import com.banking.dto.CardRequest;
import com.banking.dto.CardResponse;
import com.banking.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "http://localhost:5173")
public class CardController {
    @Autowired
    private CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponse> create(@RequestBody CardRequest request) {
        CardResponse response = cardService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{cardId}")
    public ResponseEntity<CardResponse> update(
            @PathVariable Integer cardId,
            @RequestBody CardRequest request) {
        CardResponse response = cardService.update(cardId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> delete(@PathVariable Integer cardId) {
        cardService.delete(cardId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponse> getById(@PathVariable Integer cardId) {
        CardResponse response = cardService.getById(cardId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CardResponse>> getByCustomer(@PathVariable Integer customerId) {
        List<CardResponse> response = cardService.getByCustomer(customerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}/all")
    public ResponseEntity<List<CardResponse>> getAll(@PathVariable Integer customerId) {
        List<CardResponse> response = cardService.getAll(customerId);
        return ResponseEntity.ok(response);
    }
}
