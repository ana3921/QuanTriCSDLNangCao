package com.banking.service;

import com.banking.dto.NotificationRequest;
import com.banking.dto.NotificationResponse;
import com.banking.entity.Notification;
import com.banking.entity.Transaction;
import com.banking.entity.User;
import com.banking.repository.NotificationRepository;
import com.banking.repository.UserRepository;
import com.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public NotificationResponse create(NotificationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(request.getTitle());
        notification.setContent(request.getContent());
        notification.setType(request.getType());
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        if (request.getRelatedTransactionId() != null) {
            Transaction transaction = transactionRepository.findById(request.getRelatedTransactionId())
                    .orElse(null);
            notification.setRelatedTransaction(transaction);
        }

        Notification saved = notificationRepository.save(notification);
        return mapToResponse(saved);
    }

    public NotificationResponse markAsRead(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setIsRead(true);
        Notification updated = notificationRepository.save(notification);
        return mapToResponse(updated);
    }

    public void delete(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
    }

    public NotificationResponse getById(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        return mapToResponse(notification);
    }

    public List<NotificationResponse> getByUser(Integer userId) {
        return notificationRepository.findByUserUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getUnreadByUser(Integer userId) {
        return notificationRepository.findByUserUserIdAndIsReadOrderByCreatedAtDesc(userId, false)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private NotificationResponse mapToResponse(Notification notification) {
        Integer txId = notification.getRelatedTransaction() != null ? 
                notification.getRelatedTransaction().getTransactionId() : null;
        
        return new NotificationResponse(
                notification.getNotificationId(),
                notification.getUser().getUserId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getType(),
                notification.getIsRead(),
                txId,
                notification.getCreatedAt()
        );
    }
}
