package com.ourdressingtable.communitycategory.domain;


import com.ourdressingtable.common.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "community_categories")
public class CommunityCategory extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_category_id")
    private Long id;

    private String name;

    private String code;

    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    private int sortOrder;

    @Builder
    public CommunityCategory(Long id, String name, String code, String description, int sortOrder, boolean isActive) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.isActive = isActive;
        this.sortOrder = sortOrder;
    }
}
