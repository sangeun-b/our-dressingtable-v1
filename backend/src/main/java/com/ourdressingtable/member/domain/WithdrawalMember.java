package com.ourdressingtable.member.domain;

import com.ourdressingtable.member.dto.WithdrawalMemberRequest;
import com.ourdressingtable.util.BaseTimeEntity;
import com.ourdressingtable.common.util.HashUtil;
import com.ourdressingtable.common.util.MaskingUtil;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "withdrawal_members")
public class WithdrawalMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hashedEmail;

    private String maskedEmail;

    private String hashedPhone;

    private String maskedPhone;

    private String reason;

    private boolean isBlock;

    @Column(name = "withdrew_at")
    private Timestamp withdrewAt;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public WithdrawalMember(Long id, String hashedEmail, String maskedEmail, String hashedPhone, String maskedPhone, String reason, boolean isBlock, Member member) {
        this.id = id;
        this.hashedEmail = hashedEmail;
        this.maskedEmail = maskedEmail;
        this.hashedPhone = hashedPhone;
        this.maskedPhone = maskedPhone;
        this.reason = reason;
        this.isBlock = isBlock;
        this.member = member;
    }

    public static WithdrawalMember from(Member member, WithdrawalMemberRequest withdrawalMemberRequest, boolean isBlock) {
        return WithdrawalMember.builder()
                .hashedEmail(HashUtil.hash(member.getEmail()))
                .maskedEmail(MaskingUtil.maskedEmail(member.getEmail()))
                .hashedPhone(HashUtil.hash(member.getPhoneNumber()))
                .maskedPhone(MaskingUtil.maskedPhone(member.getPhoneNumber()))
                .reason(withdrawalMemberRequest.getReason())
                .isBlock(isBlock)
                .member(member)
                .build();
    }

    @PrePersist
    public void prePersist() {
        this.withdrewAt = Timestamp.valueOf(LocalDateTime.now());
    }

}
