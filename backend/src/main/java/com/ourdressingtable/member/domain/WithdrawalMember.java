package com.ourdressingtable.member.domain;

import com.ourdressingtable.util.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

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

    @Builder
    public WithdrawalMember(String hashedEmail, String maskedEmail, String hashedPhone, String maskedPhone, String reason, boolean isBlock) {
        this.hashedEmail = hashedEmail;
        this.maskedEmail = maskedEmail;
        this.hashedPhone = hashedPhone;
        this.maskedPhone = maskedPhone;
        this.reason = reason;
        this.isBlock = isBlock;
    }

    @PrePersist
    public void prePersist() {
        this.withdrewAt = Timestamp.valueOf(LocalDateTime.now());
    }


}
