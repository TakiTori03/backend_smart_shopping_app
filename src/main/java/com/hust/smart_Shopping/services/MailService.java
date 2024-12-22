package com.hust.smart_Shopping.services;

import java.util.Map;

import com.hust.smart_Shopping.models.User;

public interface MailService {

    public void sendActivationEmail(User user);

    public void sendCreationEmail(User user);

    public void sendPasswordResetMail(User user);

}