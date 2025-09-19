package com.ourdressingtable.membercosmetic.dto;

import com.ourdressingtable.membercosmetic.domain.NotifyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateMemberCosmeticRequest {

    @Schema(description = "화장품 브랜드", example = "1")
    @NotNull(message = "브랜드는 필수입니다.")
    private Long brandId;

    @Schema(description = "화장품 이름(필수 값, 전달 시 null/빈값 불가). 전달하지 않으면 기존 유지", example = "그린티 세럼")
    @NotBlank(message = "화장품 이름은 필수입니다.")
    private String name;

    @Schema(description = "화장품 카테고리", example = "1")
    @NotNull(message = "카테고리는 필수입니다.")
    private Long categoryId;

    @Schema(description = "구매 가격 (미전달=유지, null=삭제, 값=갱신)", example = "21000")
    private JsonNullable<BigDecimal> price = JsonNullable.undefined();

    @Schema(description = "화장품 이미지 (미전달=유지, null=삭제, 값=갱신)", example = "URL")
    private JsonNullable<String> imageUrl = JsonNullable.undefined();

    @Schema(description = "알림 날짜 (미전달=유지, null=삭제, 값=갱신)", example = "2030-01-01")
    private JsonNullable<LocalDate> notificationDate = JsonNullable.undefined();

    @Schema(description = "구매처 (미전달=유지, null=삭제, 값=갱신)", example = "올리브영")
    private JsonNullable<String> store = JsonNullable.undefined();

    @Schema(description = "화장품 이미지 (미전달=유지, null=삭제, 값=갱신)", example = "URL")
    private JsonNullable<String> expirationType = JsonNullable.undefined();

    @Schema(description = "만료 N일 전 알림 (미전달=유지, null=삭제, 값=갱신)", example = "30")
    private JsonNullable<Integer> notifyBeforeDays = JsonNullable.undefined();

    @Schema(description = "알림 사용 여부 (미전달=유지, null=삭제, 값=갱신)", example = "true")
    private JsonNullable<Boolean> setNotification = JsonNullable.undefined();

    @Schema(description = "알림 기준(EXPIRED, OPEN, USE) (미전달=유지, null=삭제, 값=갱신)", example = "OPEN")
    private JsonNullable<NotifyType> notifyType = JsonNullable.undefined();

    @Schema(description = "유효기간 (미전달=유지, null=삭제, 값=갱신)", example = "2025-12-31")
    private JsonNullable<LocalDate > expiredDate = JsonNullable.undefined();

    @Schema(description = "개봉날짜 (미전달=유지, null=삭제, 값=갱신)", example = "2025-11-01")
    private JsonNullable<LocalDate > openDate = JsonNullable.undefined();

    @Schema(description = "사용날짜 (미전달=유지, null=삭제, 값=갱신)", example = "2025-11-01")
    private JsonNullable<LocalDate > useByDate = JsonNullable.undefined();

    @Builder
    public UpdateMemberCosmeticRequest(
            Long brandId,
            String name,
            Long categoryId,
            JsonNullable<BigDecimal> price,
            JsonNullable<String> imageUrl,
            JsonNullable<LocalDate> notificationDate,
            JsonNullable<String> store,
            JsonNullable<String> expirationType,
            JsonNullable<Integer> notifyBeforeDays,
            JsonNullable<Boolean> setNotification,
            JsonNullable<NotifyType> notifyType,
            JsonNullable<LocalDate> expiredDate,
            JsonNullable<LocalDate> openDate,
            JsonNullable<LocalDate> useByDate
    ) {
        this.brandId = brandId;
        this.name = name;
        this.categoryId = categoryId;
        this.price = price != null ? price : JsonNullable.undefined();
        this.imageUrl = imageUrl != null ? imageUrl : JsonNullable.undefined();
        this.notificationDate = notificationDate != null ? notificationDate : JsonNullable.undefined();
        this.store = store != null ? store : JsonNullable.undefined();
        this.expirationType = expirationType != null ? expirationType : JsonNullable.undefined();
        this.notifyBeforeDays = notifyBeforeDays != null ? notifyBeforeDays : JsonNullable.undefined();
        this.setNotification = setNotification != null ? setNotification : JsonNullable.undefined();
        this.notifyType = notifyType != null ? notifyType : JsonNullable.undefined();
        this.expiredDate = expiredDate != null ? expiredDate : JsonNullable.undefined();
        this.openDate = openDate != null ? openDate : JsonNullable.undefined();
        this.useByDate = useByDate != null ? useByDate : JsonNullable.undefined();
    }
}
