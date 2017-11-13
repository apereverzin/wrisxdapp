package com.wrisx.wrisxdapp.riskknowledge.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class PasswordProvider {
    private static final String PASSWORD_CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";

    public String getRandomPassword(int passwordLength) {
        return RandomStringUtils.random(passwordLength, PASSWORD_CHARACTERS);
    }
}
