package com.ourdressingtable.communityCategory.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "community_categories")
public class CommunityCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_category_id")
    private Long id;

    private String name;

    @Builder
    public CommunityCategory(String name) {
        this.name = name;
    }
}
