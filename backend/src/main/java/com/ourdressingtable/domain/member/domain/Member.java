package com.ourdressingtable.domain.member.domain;


import com.ourdressingtable.global.utils.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}
