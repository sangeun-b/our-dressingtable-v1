package com.ourdressingtable.membercosmetic.dto;

import com.ourdressingtable.membercosmetic.domain.MemberCosmetic;
import com.ourdressingtable.membercosmetic.domain.NotifyType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCosmeticDetailResponse {

    @Schema(description = "회원 화장품 id", example="1")
    private Long id;

    @Schema(description = "화장품 브랜드", example = "이니스프리")
    private String brand;

    @Schema(description = "화장품 이름", example = "그린티 세럼")
    private String name;

    @Schema(description = "화장품 카테고리", example = "스킨>세럼")
    private String category;

    @Schema(description = "회원 화장품 이미지 url", example="https://image.img")
    private String imageUrl;

    @Schema(description = "유효기간", example="2025-12-31")
    private LocalDate expiredDate;

    @Schema(description = "개봉날짜", example="2025-11-01")
    private LocalDate openDate;

    @Schema(description = "사용날짜", example="2025-11-01")
    private LocalDate useByDate;

    @Schema(description = "개봉 후 사용기간", example="6M")
    private String expirationType;

    @Schema(description = "알림 기준(expired, open, use)", example = "expired")
    private NotifyType notifyType;

    @Schema(description = "구매가", example="20,000")
    private BigDecimal price;

    @Schema(description = "구매처", example="올리브영")
    private String store;

    @Schema(description = "만료 N일 전 알림", example="30")
    private Integer notifyBeforeDays;

    @Schema(description = "알림 사용 여부", example="true")
    private Boolean setNotification;

    @Schema(description = "알림날짜", example="2025-11-01")
    private LocalDate notificationDate;

    @Builder
    public MemberCosmeticDetailResponse(
            Long id,
            String brand,
            String name,
            String category,
            String imageUrl,
            LocalDate expiredDate,
            LocalDate openDate,
            LocalDate useByDate,
            String expirationType,
            NotifyType notifyType,
            BigDecimal price,
            String store,
            Integer notifyBeforeDays,
            Boolean setNotification,
            LocalDate notificationDate
    ) {
        this.id = id;
        this.brand = brand;
        this.name = name;
        this.category = category;
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
    }

    public static MemberCosmeticDetailResponse from(MemberCosmetic mc) {
        return MemberCosmeticDetailResponse.builder()
                .id(mc.getId())
                .brand(mc.getBrand().getName())
                .name(mc.getName())
                .category(mc.getCategory().getName())
                .imageUrl(mc.getImageUrl())
                .expiredDate(mc.getExpiredDate())
                .openDate(mc.getOpenDate())
                .useByDate(mc.getUseByDate())
                .expirationType(mc.getExpirationType())
                .notifyType(mc.getNotifyType())
                .price(mc.getPrice())
                .store(mc.getStore())
                .notifyBeforeDays(mc.getNotifyBeforeDays())
                .setNotification(mc.getSetNotification())
                .notificationDate(mc.getNotificationDate())
                .build();
    }

}
