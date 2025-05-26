package com.ourdressingtable.community.post.domain;

import com.ourdressingtable.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "post_id"})
})
public class PostLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    public PostLike(Long id, Member member, Post post) {
        this.id = id;
        this.member = member;
        this.post = post;
    }

    private void addPost(Post post) {
       if(this.post != null) {
           this.post.getPostLikes().remove(this);
       }
       this.post = post;
       post.getPostLikes().add(this);
    }

    private void addMember(Member member) {
        if(this.member != null) {
            this.member.getPostLikes().remove(this);
        }
        this.member = member;
        member.getPostLikes().add(this);
    }

    public static PostLike create(Member member, Post post) {
        PostLike postLike = PostLike.builder().build();
        postLike.addMember(member);
        postLike.addPost(post);
        return postLike;
    }
}
