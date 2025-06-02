package com.ourdressingtable.common.util;

import com.ourdressingtable.member.domain.Role;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthorityUtil {

    public static List<GrantedAuthority> createAuthorities(Role role) {
        return List.of(new SimpleGrantedAuthority(role.getAuth()));
    }

// 다중 권한
//    public static List<GrantedAuthority> createAuthorities(Collection<Role> roles) {
//        return roles.stream()
//                .map(role -> new SimpleGrantedAuthority(role.getAuth()))
//                .collect(Collectors.toList());
//    }
}
