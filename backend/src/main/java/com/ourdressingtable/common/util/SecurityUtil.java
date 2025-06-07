package com.ourdressingtable.common.util;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.security.dto.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static CustomUserDetails getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) {
            throw new OurDressingTableException(ErrorCode.UNAUTHORIZED);
        }
        return (CustomUserDetails) auth.getPrincipal();
    }

    public static Long getCurrentMemberId() {
        return getCurrentUser().getMemberId();
    }
}
