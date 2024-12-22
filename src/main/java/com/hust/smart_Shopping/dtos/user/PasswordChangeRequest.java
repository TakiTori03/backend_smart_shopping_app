package com.hust.smart_Shopping.dtos.user;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String oldPassword;
    private String newPassword;
}
