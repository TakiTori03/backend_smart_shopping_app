package com.hust.smart_Shopping.utils;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LocalizationUtils {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    private final UserService userService;

    public String getLocalizedMessage(String messageKey, Object... objects) {
        HttpServletRequest request = WebUtils.getCurrentRequest();
        Locale locale;
        if (request.getHeader("Authorization") == null) {
            locale = localeResolver.resolveLocale(request);
        } else {
            User user = userService.getUserDetailsFromToken(WebUtils.getJwtfromRequest(request));
            locale = getUserLocale(user);

        }

        return messageSource.getMessage(messageKey, objects, locale);
    }

    private Locale getUserLocale(User user) {
        // Lấy ngôn ngữ từ user và chuyển thành Locale
        if (user != null && user.getLanguage() != null) {
            return Locale.forLanguageTag(user.getLanguage());
        }
        // Mặc định trả về Locale mặc định của hệ thống
        return Locale.getDefault();
    }
}
