package com.hust.smart_Shopping.constants;

public interface AppConstants {
    public class RoleType {
        public static final String ADMIN = "ADMIN";
        public static final String USER = "USER";
        public static final String LEADER = "LEADER";
        public static final String MEMBER = "MEMBER";
    }

    public String DEFAULT_COUNTRYCODE = "VN";
    public Integer DEFAULT_TIMEZONE = 7;
    public String DEFAULT_LANGUAGE = "vi";
    public Integer MAX_TOKEN_PER_USER = 3;

    public String FRONTEND_HOST = "http://localhost:3000";
}
