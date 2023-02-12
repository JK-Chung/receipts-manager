package com.chung.receiptsmanager.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HeadersUtil {

    private HeadersUtil() {
        // disallow instantiation for this util class
    }

    public static String generateBasicAuthHeaderValue(final String username, final String password) {
        final String plaintextBasicAuth = username + ":" + password;
        final String base64Encoded = Base64.getEncoder()
                .encodeToString(plaintextBasicAuth.getBytes(StandardCharsets.ISO_8859_1));
        return "Basic " + base64Encoded;
    }

}
