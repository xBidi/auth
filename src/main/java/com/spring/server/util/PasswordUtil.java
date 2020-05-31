package com.spring.server.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public abstract class PasswordUtil {
    public static boolean doPasswordsMatch(String plainPassword, String hashedPassword) {
        if (StringUtils.isBlank(plainPassword) || StringUtils.isBlank(hashedPassword)) {
            return false;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(plainPassword, hashedPassword);
    }
}
