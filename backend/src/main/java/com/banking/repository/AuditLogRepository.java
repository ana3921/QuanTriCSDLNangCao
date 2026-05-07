package com.banking.repository;

import com.banking.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {
    List<AuditLog> findByUserUserIdOrderByTimestampDesc(Integer userId);
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Integer entityId);
    List<AuditLog> findByActionAndEntityType(String action, String entityType);
    List<AuditLog> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
}
