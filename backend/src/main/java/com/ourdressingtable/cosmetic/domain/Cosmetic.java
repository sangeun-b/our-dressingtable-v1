package com.ourdressingtable.cosmetic.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cosmetics")
public class Cosmetic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cosmetic_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(length = 50)
    private String volume;

    @Column(length = 50)
    private String texture;

    @Column(length = 50)
    private String scent;

    @Column(name = "product_type", length = 50)
    private String productType;

    @Column(name = "skin_type", length = 50)
    private String skinType;

    @Lob
    private String ingredients;

    @Lob
    private String allergens;

    private String countryOfOrigin;

    private String manufacturer;

    private String expirationType;

    private String barcode;

    private String officialUrl;

    @Lob
    private String description;

    private String certifications;

    private String releasedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CosmeticCategory cosmeticCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private CosmeticBrand brand;

    @Builder
    public Cosmetic(Long id, String name, String imageUrl, String volume, String texture, String scent, String productType, String skinType, String ingredients,
                    String allergens, String countryOfOrigin, String manufacturer, String expirationType, String barcode, String officialUrl, String description,
                    String certifications,  String releasedAt, CosmeticCategory cosmeticCategory, CosmeticBrand brand) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.volume = volume;
        this.texture = texture;
        this.scent = scent;
        this.productType = productType;
        this.skinType = skinType;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.countryOfOrigin = countryOfOrigin;
        this.manufacturer = manufacturer;
        this.expirationType = expirationType;
        this.barcode = barcode;
        this.officialUrl = officialUrl;
        this.description = description;
        this.certifications = certifications;
        this.releasedAt = releasedAt;
        this.cosmeticCategory = cosmeticCategory;
        this.brand = brand;

    }
}

