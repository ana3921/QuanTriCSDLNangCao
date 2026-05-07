package com.banking.controller;

import com.banking.dto.SavedBillRequest;
import com.banking.dto.SavedBillResponse;
import com.banking.service.SavedBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-bills")
@CrossOrigin(origins = "http://localhost:5173")
public class SavedBillController {
    @Autowired
    private SavedBillService savedBillService;

    @PostMapping
    public ResponseEntity<SavedBillResponse> create(@RequestBody SavedBillRequest request) {
        SavedBillResponse response = savedBillService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{savedBillId}")
    public ResponseEntity<SavedBillResponse> update(
            @PathVariable Integer savedBillId,
            @RequestBody SavedBillRequest request) {
        SavedBillResponse response = savedBillService.update(savedBillId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{savedBillId}")
    public ResponseEntity<Void> delete(@PathVariable Integer savedBillId) {
        savedBillService.delete(savedBillId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{savedBillId}")
    public ResponseEntity<SavedBillResponse> getById(@PathVariable Integer savedBillId) {
        SavedBillResponse response = savedBillService.getById(savedBillId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<SavedBillResponse>> getByAccount(@PathVariable Integer accountId) {
        List<SavedBillResponse> response = savedBillService.getByAccount(accountId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountId}/all")
    public ResponseEntity<List<SavedBillResponse>> getAll(@PathVariable Integer accountId) {
        List<SavedBillResponse> response = savedBillService.getAll(accountId);
        return ResponseEntity.ok(response);
    }
}
