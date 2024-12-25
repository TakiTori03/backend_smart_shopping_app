package com.hust.smart_Shopping.dtos.user;

import java.time.Instant;

import com.hust.smart_Shopping.models.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;

    private String name;
    private String email;

    private String username;

    private String phoneNumber;
    private String address;

    private Instant birthDay;

    private String language;

    private Integer timezone;

    private Long deviceId;

    private String countryCode;

    private String gender;

    private String avatar;

    private String role;

    private boolean isActivated;
    private boolean isVerified;

    private Long beLongToGroupAdminId;

    private Instant createdAt;

    private Instant updatedAt;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .username(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .birthDay(user.getBirthDay())
                .address(user.getAddress())
                .avatar(user.getAvatar())
                .isActivated(user.isActivated())
                .isVerified(user.isVerified())
                .gender(user.getGender() != null ? user.getGender().toString() : null)
                .role(user.getRole().getName())
                .language(user.getLanguage())
                .timezone(user.getTimezone())
                .deviceId(user.getDevice_id())
                .countryCode(user.getCountryCode())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .beLongToGroupAdminId(
                        user.getUserFamily() != null ? user.getUserFamily().getFamily().getLeader().getId() : null)
                .build();
    }
}
