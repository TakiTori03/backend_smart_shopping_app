package com.hust.smart_Shopping.utils;

import java.util.Random;

public class RadomUntil {
    public static String generateConfirmCode() {
        Random random = new Random();
        return String.format("%05d", random.nextInt(10000));
    }
}
