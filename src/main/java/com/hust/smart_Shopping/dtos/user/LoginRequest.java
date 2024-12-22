package com.hust.smart_Shopping.dtos.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
