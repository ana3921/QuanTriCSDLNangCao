package com.banking.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiaryRequest {
    private Integer customerId;
    private String accountNumber;
    private String bankName;
    private String fullName;
    private String nickname;
    private Boolean isActive;
}