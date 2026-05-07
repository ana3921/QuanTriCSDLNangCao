package com.banking.dto;

public class NotificationRequest {
    private Integer userId;
    private String title;
    private String content;
    private String type;
    private Integer relatedTransactionId;

    // Getters and Setters
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

    public Integer getRelatedTransactionId() {
        return relatedTransactionId;
    }

    public void setRelatedTransactionId(Integer relatedTransactionId) {
        this.relatedTransactionId = relatedTransactionId;
    }
}
