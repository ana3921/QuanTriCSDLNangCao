package com.banking.service;

import com.banking.dto.AuditLogRequest;
import com.banking.dto.AuditLogResponse;
import com.banking.entity.AuditLog;
import com.banking.entity.User;
import com.banking.repository.AuditLogRepository;
import com.banking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuditLogService auditLogService;

    private User testUser;
    private AuditLog testAuditLog;
    private AuditLogRequest testRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1);
        testUser.setUsername("admin");

        testAuditLog = new AuditLog();
        testAuditLog.setAuditLogId(1);
        testAuditLog.setUser(testUser);
        testAuditLog.setAction("UPDATE");
        testAuditLog.setEntityType("Account");
        testAuditLog.setEntityId(100);
        testAuditLog.setOldValue("1000");
        testAuditLog.setNewValue("5000");
        testAuditLog.setDescription("Account balance updated");
        testAuditLog.setTimestamp(LocalDateTime.now());

        testRequest = new AuditLogRequest();
        testRequest.setUserId(1);
        testRequest.setAction("CREATE");
        testRequest.setEntityType("Transfer");
        testRequest.setDescription("Transfer created");
    }

    @Test
    void testCreateAuditLog() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(testAuditLog);

        AuditLogResponse response = auditLogService.create(testRequest);

        assertNotNull(response);
        assertEquals("UPDATE", response.getAction());
        assertEquals("Account", response.getEntityType());
        verify(userRepository, times(1)).findById(1);
        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }

    @Test
    void testGetAuditLogById() {
        when(auditLogRepository.findById(1)).thenReturn(Optional.of(testAuditLog));

        AuditLogResponse response = auditLogService.getById(1);

        assertNotNull(response);
        assertEquals("UPDATE", response.getAction());
        assertEquals(Integer.valueOf(100), response.getEntityId());
        verify(auditLogRepository, times(1)).findById(1);
    }

    @Test
    void testGetAuditLogByIdNotFound() {
        when(auditLogRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> auditLogService.getById(999));
    }

    @Test
    void testGetByEntity() {
        when(auditLogRepository.findByEntityTypeAndEntityId("Account", 100))
                .thenReturn(java.util.Arrays.asList(testAuditLog));

        java.util.List<AuditLogResponse> responses = auditLogService.getByEntity("Account", 100);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(auditLogRepository, times(1)).findByEntityTypeAndEntityId("Account", 100);
    }
}
