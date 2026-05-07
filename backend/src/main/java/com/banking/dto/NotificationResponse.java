package com.banking.dto;

import java.time.LocalDateTime;

public class NotificationResponse {
    private Integer notificationId;
    private Integer userId;
    private String title;
    private String content;
    private String type;
    private Boolean isRead;
    private Integer relatedTransactionId;
    private LocalDateTime createdAt;

    // Constructor
    public NotificationResponse(Integer notificationId, Integer userId, String title, String content,
                               String type, Boolean isRead, Integer relatedTransactionId, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.isRead = isRead;
        this.relatedTransactionId = relatedTransactionId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Integer getRelatedTransactionId() {
        return relatedTransactionId;
    }

    public void setRelatedTransactionId(Integer relatedTransactionId) {
        this.relatedTransactionId = relatedTransactionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
