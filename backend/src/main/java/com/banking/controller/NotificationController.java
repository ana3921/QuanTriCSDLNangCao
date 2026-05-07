package com.banking.controller;

import com.banking.dto.NotificationRequest;
import com.banking.dto.NotificationResponse;
import com.banking.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> create(@RequestBody NotificationRequest request) {
        NotificationResponse response = notificationService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Integer notificationId) {
        NotificationResponse response = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> delete(@PathVariable Integer notificationId) {
        notificationService.delete(notificationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> getById(@PathVariable Integer notificationId) {
        NotificationResponse response = notificationService.getById(notificationId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getByUser(@PathVariable Integer userId) {
        List<NotificationResponse> response = notificationService.getByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadByUser(@PathVariable Integer userId) {
        List<NotificationResponse> response = notificationService.getUnreadByUser(userId);
        return ResponseEntity.ok(response);
    }
}
