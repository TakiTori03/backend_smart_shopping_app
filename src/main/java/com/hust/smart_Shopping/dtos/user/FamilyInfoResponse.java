package com.hust.smart_Shopping.dtos.user;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FamilyInfoResponse {
    private Long groupAdmin;
    private List<UserResponse> members;
}
