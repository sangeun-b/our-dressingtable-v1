package com.ourdressingtable.util;

public class MaskingUtil {

    public static String maskedEmail(String email) {
        String[] parts = email.split("@");
        String namePart = parts[0];
        String domainPart = parts[1];

        String maskedName = namePart.charAt(0) + "." + namePart.charAt(0) + "***";

        String[] domainParts = domainPart.split("\\.");
        String maskedDomain = domainParts[0].charAt(0)+"*****";

        return maskedName + "@" + maskedDomain + "." + domainParts[1];

    }

    public static String maskedPhone(String phone) {
        return phone.replaceAll("(\\d{3})-\\d{4}-(\\d{4})", "$1****$2");
    }

}
