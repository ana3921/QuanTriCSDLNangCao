package com.banking.service;

import com.banking.dto.NotificationRequest;
import com.banking.dto.NotificationResponse;
import com.banking.entity.Notification;
import com.banking.entity.User;
import com.banking.repository.NotificationRepository;
import com.banking.repository.UserRepository;
import com.banking.repository.TransactionRepository;
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
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private NotificationService notificationService;

    private User testUser;
    private Notification testNotification;
    private NotificationRequest testRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1);
        testUser.setUsername("testuser");

        testNotification = new Notification();
        testNotification.setNotificationId(1);
        testNotification.setUser(testUser);
        testNotification.setTitle("Transaction Alert");
        testNotification.setContent("You have received $100");
        testNotification.setType("transaction");
        testNotification.setIsRead(false);
        testNotification.setCreatedAt(LocalDateTime.now());

        testRequest = new NotificationRequest();
        testRequest.setUserId(1);
        testRequest.setTitle("New Transfer");
        testRequest.setContent("Transfer initiated");
        testRequest.setType("transaction");
    }

    @Test
    void testCreateNotification() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        NotificationResponse response = notificationService.create(testRequest);

        assertNotNull(response);
        assertEquals("Transaction Alert", response.getTitle());
        assertFalse(response.getIsRead());
        verify(userRepository, times(1)).findById(1);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testMarkAsRead() {
        when(notificationRepository.findById(1)).thenReturn(Optional.of(testNotification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        NotificationResponse response = notificationService.markAsRead(1);

        assertNotNull(response);
        verify(notificationRepository, times(1)).findById(1);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testGetNotificationById() {
        when(notificationRepository.findById(1)).thenReturn(Optional.of(testNotification));

        NotificationResponse response = notificationService.getById(1);

        assertNotNull(response);
        assertEquals("Transaction Alert", response.getTitle());
        verify(notificationRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteNotification() {
        when(notificationRepository.findById(1)).thenReturn(Optional.of(testNotification));

        notificationService.delete(1);

        verify(notificationRepository, times(1)).findById(1);
        verify(notificationRepository, times(1)).delete(testNotification);
    }
}
