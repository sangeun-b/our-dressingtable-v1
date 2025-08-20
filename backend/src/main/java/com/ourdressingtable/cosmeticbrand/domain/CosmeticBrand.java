package com.ourdressingtable.cosmeticbrand.domain;

import com.ourdressingtable.cosmetic.domain.Cosmetic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cosmetic_brands")
public class CosmeticBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cosmetic_brand_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "homepage_url")
    private String homepageUrl;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "brand")
    private List<Cosmetic> cosmetics = new ArrayList<>();

    @Builder
    public CosmeticBrand(Long id, String name, String logoUrl, String homepageUrl, String description,List<Cosmetic> cosmetics) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.homepageUrl = homepageUrl;
        this.description = description;
        this.cosmetics = cosmetics;

    }
}
