package com.banking.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferResponse {
    private Integer resultCode;
    private String resultMessage;
    private String transactionCode;
}
