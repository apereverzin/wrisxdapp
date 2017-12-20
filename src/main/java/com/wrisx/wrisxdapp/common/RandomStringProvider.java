package com.wrisx.wrisxdapp.common;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class RandomStringProvider {
    private static final String STRING_CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";

    public String getRandomString(int stringLength) {
        return RandomStringUtils.random(stringLength, STRING_CHARACTERS);
    }
}
