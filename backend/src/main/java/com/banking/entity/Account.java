package com.banking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;

    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false, length = 20)
    private String accountType;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, columnDefinition = "NCHAR(3)")
    private String currency;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = false)
    private Boolean isDefault;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal dailyLimit;

    @Column(nullable = false, columnDefinition = "DATETIME2")
    private LocalDateTime openedAt;

    @Column(columnDefinition = "DATETIME2")
    private LocalDateTime closedAt;

    @Column
    private Integer termMonths;

    @Column(precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column
    private LocalDate maturityDate;

}
