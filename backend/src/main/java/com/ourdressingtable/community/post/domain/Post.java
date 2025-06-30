package com.ourdressingtable.community.post.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ourdressingtable.community.comment.domain.Comment;
import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.common.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts")
//@SQLDelete(sql = "UPDATE posts SET is_deleted = true, deleted_at = NOW() WHERE post_id = ?")
@SQLRestriction("is_deleted = false")
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String content;

    @ColumnDefault("0")
    private int viewCount = 0;

    @ColumnDefault("0")
    private int likeCount = 0;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_category_id", nullable = false)
    private CommunityCategory communityCategory;

    @OneToMany(mappedBy = "post")
    @JsonBackReference
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public Post(Long id, String title, String content, int viewCount, int likeCount, Member member, CommunityCategory communityCategory, boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.postLikes = new ArrayList<>();
        this.member = member;
        this.communityCategory = communityCategory;
        this.isDeleted = isDeleted;
        this.deletedAt = null;

    }

//    public void addMember(Member member) {
//        if(this.member != null) {
//            this.member.getPosts().remove(this);
//        }
//        this.member = member;
//        member.getPosts().add(this);
//    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;

    }

    public void updateCommunityCategory(CommunityCategory communityCategory) {
        this.communityCategory = communityCategory;
    }

    public void increaseLike() {
        this.likeCount++;
    }

    public void decreaseLike() {
        if(this.likeCount > 0)
            this.likeCount--;
    }

    public void markAsDeleted() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

//    @PreRemove
//    public void onSoftDelete() {
//        this.deletedAt = new Timestamp(System.currentTimeMillis());
//    }

}
