package com.banking.service;

import com.banking.dto.AuditLogRequest;
import com.banking.dto.AuditLogResponse;
import com.banking.entity.AuditLog;
import com.banking.entity.User;
import com.banking.repository.AuditLogRepository;
import com.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogService {
    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private UserRepository userRepository;

    public AuditLogResponse create(AuditLogRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        AuditLog auditLog = new AuditLog();
        auditLog.setUser(user);
        auditLog.setAction(request.getAction());
        auditLog.setEntityType(request.getEntityType());
        auditLog.setEntityId(request.getEntityId());
        auditLog.setOldValue(request.getOldValue());
        auditLog.setNewValue(request.getNewValue());
        auditLog.setDescription(request.getDescription());
        auditLog.setIpAddress(request.getIpAddress());
        auditLog.setTimestamp(LocalDateTime.now());

        AuditLog saved = auditLogRepository.save(auditLog);
        return mapToResponse(saved);
    }

    public AuditLogResponse getById(Integer auditLogId) {
        AuditLog auditLog = auditLogRepository.findById(auditLogId)
                .orElseThrow(() -> new RuntimeException("Audit log not found"));
        return mapToResponse(auditLog);
    }

    public List<AuditLogResponse> getByUser(Integer userId) {
        return auditLogRepository.findByUserUserIdOrderByTimestampDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getByEntity(String entityType, Integer entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getByAction(String action, String entityType) {
        return auditLogRepository.findByActionAndEntityType(action, entityType)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        return auditLogRepository.findByTimestampBetween(startTime, endTime)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AuditLogResponse mapToResponse(AuditLog auditLog) {
        return new AuditLogResponse(
                auditLog.getAuditLogId(),
                auditLog.getUser().getUserId(),
                auditLog.getAction(),
                auditLog.getEntityType(),
                auditLog.getEntityId(),
                auditLog.getOldValue(),
                auditLog.getNewValue(),
                auditLog.getDescription(),
                auditLog.getIpAddress(),
                auditLog.getTimestamp()
        );
    }
}
