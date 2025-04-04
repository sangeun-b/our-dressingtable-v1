package com.ourdressingtable.member.domain;


import static jakarta.persistence.FetchType.LAZY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ourdressingtable.member.dto.UpdateMemberRequest;
import com.ourdressingtable.util.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    private String nickname;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SkinType skinType;

    @Enumerated(EnumType.STRING)
    private ColorType colorType;

    private Date birthDate;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ColumnDefault("0")
    private int lockedCount;

    private String imageUrl;

    @Builder
    public Member(Long id, String email, String password, String name, String nickname, String phoneNumber, Role role, SkinType skinType, ColorType colorType, Date birthDate, AuthType authType, Status status, int lockedCount, String imageUrl) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.skinType = skinType;
        this.colorType = colorType;
        this.birthDate = birthDate;
        this.authType = authType;
        this.status = status;
        this.lockedCount = lockedCount;
        this.imageUrl = imageUrl;

    }

    public void updateMember(UpdateMemberRequest updateMemberRequest) {
        this.password = getOrDefault(updateMemberRequest.getPassword(),this.password);
        this.nickname = getOrDefault(updateMemberRequest.getNickname(),this.nickname);
        this.phoneNumber = getOrDefault(updateMemberRequest.getPhoneNumber(),this.phoneNumber);
        this.skinType = getOrDefault(updateMemberRequest.getSkinType(),this.skinType);
        this.colorType = getOrDefault(updateMemberRequest.getColorType(),this.colorType);
        this.birthDate = getOrDefault(updateMemberRequest.getBirthDate(),this.birthDate);
        this.imageUrl = getOrDefault(updateMemberRequest.getImageUrl(),this.imageUrl);
    }

    private String getOrDefault(String newValue, String currentValue) {
        return (newValue == null || newValue.isBlank()) ? currentValue : newValue;
    }

    private <T> T getOrDefault(T newValue, T currentValue) {
        return (newValue != null) ? newValue : currentValue;
    }
}
