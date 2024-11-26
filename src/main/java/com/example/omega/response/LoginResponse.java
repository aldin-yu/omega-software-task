package com.example.omega.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class LoginResponse {
    private String token;

    private long expiresIn;


}