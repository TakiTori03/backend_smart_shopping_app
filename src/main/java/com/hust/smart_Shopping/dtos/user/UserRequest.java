package com.hust.smart_Shopping.dtos.user;

import lombok.Data;

@Data
public class UserRequest {

    private String name;
    private String email;
    private String password;

    private String phoneNumber;
    private String address;

    private String language;

    private Integer timezone;

    private Long deviceId;

    private String countryCode;

    private String gender;

    private String avatar;

    private String role;
}
