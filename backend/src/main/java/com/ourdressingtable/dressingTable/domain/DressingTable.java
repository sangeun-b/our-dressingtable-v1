package com.ourdressingtable.dressingTable.domain;

import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.ourdressingtable.util.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "dressing_tables")
public class DressingTable extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public DressingTable(Long id, String name, String imageUrl, Member member) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.member = member;
    }

    public void updateName(String newName) {
        this.name = newName;
    }
    public void updateImageUrl(String newImageUrl) {
        this.imageUrl = newImageUrl;
    }
}
