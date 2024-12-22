package com.hust.smart_Shopping.dtos.admin;

import lombok.Data;

@Data
public class AdminUpdateRequest {
    private String oldName;
    private String newName;
}
