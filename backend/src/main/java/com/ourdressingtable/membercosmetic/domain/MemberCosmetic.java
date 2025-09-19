package com.ourdressingtable.membercosmetic.domain;

import static com.ourdressingtable.common.util.JsonNullableUtils.applyTriState;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.cosmeticbrand.domain.CosmeticBrand;
import com.ourdressingtable.cosmeticcategory.domain.CosmeticCategory;
import com.ourdressingtable.dressingtable.domain.DressingTable;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.membercosmetic.dto.UpdateMemberCosmeticRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_cosmetics")
@SQLRestriction("is_deleted = false")
public class MemberCosmetic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_cosmetic_id")
    private Long id;

    @Column(nullable = false)
    private String name;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "notify_type")
    private NotifyType notifyType;

    @Column(name = "notify_before_days")
    private Integer notifyBeforeDays;

    @Column(name = "set_notification")
    @ColumnDefault("false")
    private Boolean setNotification = false;

    @Column(name = "notification_date")
    private LocalDate notificationDate;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cosmetic_category_id", nullable = false)
    private CosmeticCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cosmetic_brand_id", nullable = false)
    private CosmeticBrand brand;

    @Builder
    public MemberCosmetic(Long id, String name, String imageUrl, LocalDate expiredDate, LocalDate openDate, LocalDate useByDate, BigDecimal price, String store, Boolean setNotification, LocalDate notificationDate, Member member, DressingTable dressingTable,
            NotifyType notifyType, String expirationType, Integer notifyBeforeDays, boolean isDeleted, LocalDateTime deletedAt, CosmeticBrand brand, CosmeticCategory category) {
        this.id = id;
        this.name = name;
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
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
        this.brand = brand;
        this.category = category;

    }

    public void markAsDeleted() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void updateMemberCosmetic(UpdateMemberCosmeticRequest request, CosmeticBrand brand, CosmeticCategory category) {

        if(request.getName() != null) {
            if(!StringUtils.hasText(request.getName())) {
                throw new OurDressingTableException(ErrorCode.MEMBER_COSMETIC_NAME_EMPTY);
            }
            this.name = request.getName();
        }

        if(request.getBrandId() != null) {
            if(brand == null) {
                throw new OurDressingTableException(ErrorCode.COSMETIC_BRAND_EMPTY);
            }
            this.brand = brand;
        }

        if(request.getCategoryId() != null) {
            if(category == null) {
                throw new OurDressingTableException(ErrorCode.COSMETIC_CATEGORY_EMPTY);
            }
            this.category = category;
        }


        applyTriState(request.getPrice(), v -> this.price = v, () -> this.price = null);
        applyTriState(request.getImageUrl(), v -> this.imageUrl = v, () -> this.imageUrl = null);
        applyTriState(request.getNotificationDate(), v -> this.notificationDate = v, () -> this.notificationDate = null);
        applyTriState(request.getStore(), v -> this.store = v, () -> this.store = null);
        applyTriState(request.getExpirationType(), v -> this.expirationType = v, () -> this.expirationType = null);
        applyTriState(request.getNotifyBeforeDays(), v -> this.notifyBeforeDays = v, () -> this.notifyBeforeDays = null);
        applyTriState(request.getSetNotification(), v -> this.setNotification = v, () -> this.setNotification = null);
        applyTriState(request.getNotifyType(), v -> this.notifyType = v, () -> this.notifyType = null);
        applyTriState(request.getExpiredDate(), v -> this.expiredDate = v, () -> this.expiredDate = null);
        applyTriState(request.getOpenDate(), v -> this.openDate = v, () -> this.openDate = null);
        applyTriState(request.getUseByDate(), v -> this.useByDate = v, () -> this.useByDate = null);

    }

    public void changeDressingTable(DressingTable dressingTable) {
        this.dressingTable = dressingTable;
    }

    private <T> T getOrDefault(T newValue, T currentValue) {
        return (newValue != null) ? newValue : currentValue;
    }


}
