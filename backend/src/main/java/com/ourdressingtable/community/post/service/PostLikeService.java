package com.ourdressingtable.community.post.service;

public interface PostLikeService {
    boolean postLike(Long postId, Long memberId);
    boolean hasLiked(Long postId, Long memberId);
}
