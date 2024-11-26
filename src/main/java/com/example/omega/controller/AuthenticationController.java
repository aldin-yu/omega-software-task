package com.example.omega.controller;

import com.example.omega.model.User;
import com.example.omega.request.LoginUserRequest;
import com.example.omega.request.RegisterUserRequest;
import com.example.omega.response.LoginResponse;
import com.example.omega.service.AuthenticationService;
import com.example.omega.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("v1/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserRequest registerUserRequest) {
        User registeredUser = authenticationService.signup(registerUserRequest);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserRequest loginUserRequest) {
        User authenticatedUser = authenticationService.authenticate(loginUserRequest);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        return ResponseEntity.ok(LoginResponse.builder().token(jwtToken).expiresIn(jwtService.getExpirationTime()).build());
    }
}
