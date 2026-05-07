package com.banking.controller;

import com.banking.dto.AuditLogRequest;
import com.banking.dto.AuditLogResponse;
import com.banking.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@CrossOrigin(origins = "http://localhost:5173")
public class AuditLogController {
    @Autowired
    private AuditLogService auditLogService;

    @PostMapping
    public ResponseEntity<AuditLogResponse> create(@RequestBody AuditLogRequest request) {
        AuditLogResponse response = auditLogService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{auditLogId}")
    public ResponseEntity<AuditLogResponse> getById(@PathVariable Integer auditLogId) {
        AuditLogResponse response = auditLogService.getById(auditLogId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLogResponse>> getByUser(@PathVariable Integer userId) {
        List<AuditLogResponse> response = auditLogService.getByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<List<AuditLogResponse>> getByEntity(
            @PathVariable String entityType,
            @PathVariable Integer entityId) {
        List<AuditLogResponse> response = auditLogService.getByEntity(entityType, entityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/action/{action}/{entityType}")
    public ResponseEntity<List<AuditLogResponse>> getByAction(
            @PathVariable String action,
            @PathVariable String entityType) {
        List<AuditLogResponse> response = auditLogService.getByAction(action, entityType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<AuditLogResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<AuditLogResponse> response = auditLogService.getByDateRange(startTime, endTime);
        return ResponseEntity.ok(response);
    }
}
