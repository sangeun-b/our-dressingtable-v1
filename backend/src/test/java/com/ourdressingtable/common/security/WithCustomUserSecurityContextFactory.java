package com.ourdressingtable.common.security;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.security.dto.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Member member = Member.builder()
                .id(customUser.memberId())
                .email(customUser.email())
                .build();

        CustomUserDetails userDetails = CustomUserDetails.builder()
                .memberId(customUser.memberId())
                .email(customUser.email())
                .password(customUser.password())
                .role(customUser.role())
                .status(customUser.status())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        context.setAuthentication(authentication);
        return context;
    }

}
