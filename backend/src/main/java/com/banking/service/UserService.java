package com.banking.service;

import com.banking.dto.LoginRequest;
import com.banking.dto.LoginResponse;
import com.banking.entity.User;
import com.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        
        if (user.isEmpty() || !user.get().getIsActive()) {
            throw new RuntimeException("User not found or inactive");
        }

        User foundUser = user.get();

        if (foundUser.getPasswordHash() == null || !passwordEncoder.matches(request.getPassword(), foundUser.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(foundUser.getUserId(), foundUser.getUsername(), foundUser.getRole());

        return LoginResponse.builder()
                .token(token)
                .userId(foundUser.getUserId())
                .username(foundUser.getUsername())
                .role(foundUser.getRole())
                .build();
    }

}
