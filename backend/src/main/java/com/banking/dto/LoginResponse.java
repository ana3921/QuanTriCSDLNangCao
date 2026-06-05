package com.banking.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private Integer userId;
    private Integer customerId;
    private String username;
    private String role;
}
