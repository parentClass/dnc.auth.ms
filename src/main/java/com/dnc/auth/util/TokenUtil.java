package com.dnc.auth.util;

public class TokenUtil {
    private TokenUtil() {}
    public static String stripTokenFromHeader(String header) {
        return header.substring(7);
    }
}
