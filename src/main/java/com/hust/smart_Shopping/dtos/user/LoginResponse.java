package com.hust.smart_Shopping.dtos.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private UserResponse user;
}
