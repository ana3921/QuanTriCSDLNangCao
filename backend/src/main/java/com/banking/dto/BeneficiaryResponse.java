package com.banking.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiaryResponse {
    private Integer beneficiaryId;
    private Integer customerId;
    private String accountNumber;
    private String bankName;
    private String fullName;
    private String nickname;
    private Boolean isActive;
    private LocalDateTime createdAt;
}