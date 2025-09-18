package com.ourdressingtable.auth.dto;

import com.ourdressingtable.common.util.AuthorityUtil;
import com.ourdressingtable.member.domain.Member;
import java.util.Collection;
import java.util.Objects;

import com.ourdressingtable.member.domain.Role;
import com.ourdressingtable.member.domain.Status;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long memberId;
    private final String email;
    private final String password;
    private final Role role;
    private final Status status;

    @Builder
    public CustomUserDetails(Long memberId, String email, String password, Role role, Status status) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    @Override

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtil.createAuthorities(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status != Status.WITHDRAWAL;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != Status.LOCK;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == Status.ACTIVE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(email,that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    public static CustomUserDetails from(Member member) {
        return CustomUserDetails.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .role(member.getRole())
                .status(member.getStatus())
                .build();
    }
}
