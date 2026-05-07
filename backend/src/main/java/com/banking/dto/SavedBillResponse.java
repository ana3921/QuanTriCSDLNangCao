package com.banking.dto;

import java.time.LocalDateTime;

public class SavedBillResponse {
    private Integer savedBillId;
    private Integer accountId;
    private String billerName;
    private String accountNumber;
    private String nickname;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor
    public SavedBillResponse(Integer savedBillId, Integer accountId, String billerName, 
                            String accountNumber, String nickname, Boolean isActive, 
                            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.savedBillId = savedBillId;
        this.accountId = accountId;
        this.billerName = billerName;
        this.accountNumber = accountNumber;
        this.nickname = nickname;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Integer getSavedBillId() {
        return savedBillId;
    }

    public void setSavedBillId(Integer savedBillId) {
        this.savedBillId = savedBillId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
