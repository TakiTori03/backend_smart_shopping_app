package com.hust.smart_Shopping.dtos.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationResponse {
    private String confirmToken;
}
