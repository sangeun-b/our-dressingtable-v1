package com.ourdressingtable.common.util;

import com.ourdressingtable.common.exception.OurDressingTableException;
import org.apache.commons.lang3.StringUtils;

public class MaskingUtil {

    public static String maskedEmail(String email) {
        if(StringUtils.isBlank(email))
            throw new IllegalArgumentException("Email cannot be null or empty");

        String[] parts = email.split("@");

        if(parts.length != 2 || parts[0].isEmpty() || parts[1].isEmpty())
            throw new IllegalArgumentException("Invalid email format");

        String namePart = parts[0];
        String domainPart = parts[1];

        String maskedName = namePart.charAt(0) + "." + namePart.charAt(0) + "***";

        String[] domainParts = domainPart.split("\\.");

        if(domainParts.length != 2 || domainParts[0].isEmpty() || domainParts[1].isEmpty())
            throw new IllegalArgumentException("Invalid domain format");

        String maskedDomain = domainParts[0].charAt(0)+"*****";

        return maskedName + "@" + maskedDomain + "." + domainParts[1];

    }

    public static String maskedPhone(String phone) {
        if(StringUtils.isBlank(phone))
            throw new IllegalArgumentException("Phone cannot be null or empty");

        if(!phone.matches("\\d{3}-\\d{4}-\\d{4}"))
            throw new IllegalArgumentException("Phone number format must be xxx-xxxx-xxxx");

        return phone.replaceAll("(\\d{3})-\\d{4}-(\\d{4})", "$1****$2");
    }

}
