package com.ourdressingtable.community.post.service;

public interface PostLikeService {
    boolean toggleLike(Long postId, Long memberId);
    boolean hasLiked(Long postId, Long memberId);
}
