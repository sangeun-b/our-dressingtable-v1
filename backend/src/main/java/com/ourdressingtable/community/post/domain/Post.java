package com.ourdressingtable.community.post.domain;

import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts")
@SQLDelete(sql = "UPDATE posts SET is_deleted = true, deleted_at = NOW() WHERE post_id = ?")
@SQLRestriction("is_deleted = false")
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String content;

    @ColumnDefault("0")
    private int viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_category_id", nullable = false)
    private CommunityCategory communityCategory;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Builder
    public Post(Long id, String title, String content, int viewCount, Member member, CommunityCategory communityCategory, Boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.member = member;
        this.communityCategory = communityCategory;
        this.isDeleted = isDeleted;

    }

    public static Post from(Member member, CommunityCategory communityCategory, CreatePostRequest createPostRequest) {
        return Post.builder()
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .member(member)
                .communityCategory(communityCategory)
                .build();

    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;

    }

    public void updateCommunityCategory(CommunityCategory communityCategory) {
        this.communityCategory = communityCategory;
    }

    @PreRemove
    public void onSoftDelete() {
        this.deletedAt = new Timestamp(System.currentTimeMillis());
    }

}
