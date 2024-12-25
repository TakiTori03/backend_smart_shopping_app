package com.hust.smart_Shopping.dtos.user;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class EditUserRequest {
    private String userName;
    private MultipartFile image;
}
