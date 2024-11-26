package com.example.omega.service;

import com.example.omega.model.User;
import com.example.omega.repository.UserRepository;
import com.example.omega.request.LoginUserRequest;
import com.example.omega.request.RegisterUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;


    public User signup(RegisterUserRequest input) {
       User user = User.builder().email(input.getEmail())
               .fullName(input.getFullName())
                .password(passwordEncoder
                        .encode(input.getPassword())).build();

        return userRepository.save(user);
    }

    public User authenticate(LoginUserRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
