package com.banking.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {
    private Integer accountId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private String currency;
    private String status;
    private Boolean isDefault;
    private BigDecimal dailyLimit;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private Integer termMonths;
    private BigDecimal interestRate;
    private LocalDate maturityDate;
}
