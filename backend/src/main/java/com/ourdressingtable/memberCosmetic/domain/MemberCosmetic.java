package com.ourdressingtable.membercosmetic.domain;

import com.ourdressingtable.cosmetic.domain.Cosmetic;
import com.ourdressingtable.dressingtable.domain.DressingTable;
import com.ourdressingtable.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_cosmetics")
public class MemberCosmetic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "expired_date")
    private LocalDate expiredDate;

    @Column(name = "open_date")
    private LocalDate openDate;

    @Column(name = "use_by_date")
    private LocalDate useByDate;

    @Column(name="expiration_type")
    private String expirationType;

    @Column(scale =  2, precision = 10)
    private BigDecimal price;

    private String store;

    @Column(name = "notify_type")
    private NotifyType notifyType;

    @Column(name = "notify_before_days")
    private Integer notifyBeforeDays;

    @Column(name = "set_notification")
    @ColumnDefault("false")
    private Boolean setNotification = false;

    @Column(name = "notification_date")
    private LocalDate notificationDate;

    // TODO: 추후 화장품 정보 등록 추가
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "cosmetic_id", nullable = false)
//    private Cosmetic cosmetic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "dressing_table_id", nullable = false)
    private DressingTable dressingTable;

    @Builder
    public MemberCosmetic(Long id, String imageUrl, LocalDate expiredDate, LocalDate openDate, LocalDate useByDate, BigDecimal price, String store, Boolean setNotification, LocalDate notificationDate, Member member, DressingTable dressingTable,
            NotifyType notifyType, String expirationType, Integer notifyBeforeDays) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.expiredDate = expiredDate;
        this.openDate = openDate;
        this.useByDate = useByDate;
        this.expirationType = expirationType;
        this.notifyType = notifyType;
        this.price = price;
        this.store = store;
        this.notifyBeforeDays = notifyBeforeDays;
        this.setNotification = setNotification;
        this.notificationDate = notificationDate;
//        this.cosmetic = cosmetic;
        this.member = member;
        this.dressingTable = dressingTable;

    }




}
