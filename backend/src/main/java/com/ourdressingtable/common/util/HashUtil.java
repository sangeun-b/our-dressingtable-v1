package com.ourdressingtable.common.util;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static String hash(String input) {
        if(StringUtils.isBlank(input)) {
            throw new IllegalArgumentException("Input cannot be blank or null");
        }

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for(byte b : hash){
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("암호화 오류", e);
        }
    }

}
