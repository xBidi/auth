package com.spring.server.util;

import org.apache.commons.lang3.StringUtils;

public abstract class RegexUtil {
    public static boolean isJwt(final String jwt) {
        if (StringUtils.isBlank(jwt))
            return false;
        final String tokenWithoutPrefix = TokenUtil.removePrefix(jwt);
        final String regex = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$";
        return tokenWithoutPrefix.matches(regex);
    }

    public static boolean isBasicToken(final String token) {
        if (StringUtils.isBlank(token))
            return false;
        final String tokenWithoutPrefix = TokenUtil.removePrefix(token);
        final String regex =
            "[0-9]{13}-[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}";
        return tokenWithoutPrefix.matches(regex);
    }

    public static boolean isValidUsername(final String string) {
        return string.matches("[a-zA-Z0-9\\._\\-]{3,15}");
    }
}
