package com.ourdressingtable.member.domain;

import static jakarta.persistence.FetchType.LAZY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ourdressingtable.util.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "withdrawal_members")
public class WithdrawalMember extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email_hashed;

    private String email_masked;

    private String phone_hashed;

    private String phone_masked;

    private String reason;

    private boolean is_blacklisted;

    @Builder
    public WithdrawalMember(String email_hashed, String email_masked, String phone_hashed, String phone_masked, String reason, boolean is_blacklisted) {
        this.email_hashed = email_hashed;
        this.email_masked = email_masked;
        this.phone_hashed = phone_hashed;
        this.phone_masked = phone_masked;
        this.reason = reason;
        this.is_blacklisted = is_blacklisted;
    }



}
