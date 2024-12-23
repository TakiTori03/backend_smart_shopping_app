package com.hust.smart_Shopping.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class WebUtils {
    // trả về cho chúng ta request hiện tại
    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    public static String getJwtfromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }
}
