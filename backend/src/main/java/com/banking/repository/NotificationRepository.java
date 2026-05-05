package com.banking.repository;

import com.banking.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserUserIdOrderByCreatedAtDesc(Integer userId);
    List<Notification> findByUserUserIdAndIsReadOrderByCreatedAtDesc(Integer userId, Boolean isRead);
}
