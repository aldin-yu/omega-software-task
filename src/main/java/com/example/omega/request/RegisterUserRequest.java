package com.example.omega.request;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String email;

    private String password;

    private String fullName;

}
