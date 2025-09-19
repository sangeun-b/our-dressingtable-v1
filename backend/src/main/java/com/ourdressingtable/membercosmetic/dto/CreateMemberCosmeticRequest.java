package com.ourdressingtable.membercosmetic.dto;

import com.ourdressingtable.cosmeticbrand.domain.CosmeticBrand;
import com.ourdressingtable.cosmeticcategory.domain.CosmeticCategory;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.membercosmetic.domain.MemberCosmetic;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
public class CreateMemberCosmeticRequest {

    @Schema(description = "화장품 브랜드", example = "1")
    private Long brandId;

    @Schema(description = "화장품 이름", example = "그린티 세럼")
    private String name;

    @Schema(description = "화장품 카테고리", example = "1")
    private Long categoryId;

    @Schema(description = "화장품 이미지 URL", example = "URL")
    private String imageUrl;

    @Schema(description = "유효기간", example = "2030-01-01")
    private LocalDate expiredDate;

    @Schema(description = "개봉날짜", example = "2029-01-01")
    private LocalDate openDate;

    @Schema(description = "개봉 후 사용기간", example = "12M")
    private LocalDate useByDate;

    @Schema(description = "구입금액", example = "15000")
    private BigDecimal price;

    @Schema(description = "구매처", example = "올리브영")
    private String store;

    @Schema(description = "알림 설정", example = "true")
    private Boolean setNotification;

    @Schema(description = "알림 날짜", example="2030-01-01")
    private LocalDate notificationDate;

    @Schema(description = "화장대 ID", example = "1")
    @NotNull(message = "화장대 ID는 필수입니다.")
    private Long dressingTableId;

    @Builder
    public CreateMemberCosmeticRequest(Long brandId, String name, Long categoryId, String imageUrl, LocalDate expiredDate, LocalDate openDate,
            LocalDate useByDate, BigDecimal price, String store, Boolean setNotification,
            LocalDate notificationDate, Long dressingTableId) {
        this.brandId = brandId;
        this.name = name;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.expiredDate = expiredDate;
        this.openDate = openDate;
        this.useByDate = useByDate;
        this.price = price;
        this.store = store;
        this.setNotification = setNotification;
        this.notificationDate = notificationDate;
        this.dressingTableId = dressingTableId;

    }

    public MemberCosmetic toEntity(Member member, CosmeticBrand brand, CosmeticCategory category) {
        return MemberCosmetic.builder()
                .brand(brand)
                .name(name)
                .category(category)
                .imageUrl(imageUrl)
                .expiredDate(expiredDate)
                .openDate(openDate)
                .useByDate(useByDate)
                .price(price)
                .store(store)
                .setNotification(setNotification)
                .notificationDate(notificationDate)
                .member(member)
                .build();
    }

}
