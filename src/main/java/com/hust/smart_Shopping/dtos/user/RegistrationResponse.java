package com.hust.smart_Shopping.dtos.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationResponse {
    private UserResponse user;
    private String confirmToken;
}
