package com.hust.smart_Shopping.dtos;

import com.hust.smart_Shopping.utils.MessageKeys;

import lombok.Getter;

@Getter
public enum ApiSuccessCode {
    LOGIN_SUCCESS("00047", MessageKeys.LOGIN_SUCCESS),
    REGISTER_SUCCESS("00035", MessageKeys.REGISTER_SUCCESS),
    REFRESH_SUCCESS("00065", MessageKeys.REFRESH_TOKEN_SUCCESS),
    SEND_CODE_SUCCESS("00048", MessageKeys.SEND_CODE_SUCCESS),
    VERIFY_SUCCESS("00058", MessageKeys.VERIFY_SUCCESS),
    CHANGE_PASS_SUCCESS("00076", MessageKeys.CHANGE_PASS_SUCCESS),
    EDIT_PROFILE_SUCCESS("00086", MessageKeys.EDIT_PROFILE_SUCCESS);

    private final String code;
    private final String message;

    ApiSuccessCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
