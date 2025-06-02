package com.ourdressingtable.community.post.service;

import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.domain.PostLike;
import com.ourdressingtable.community.post.repository.PostLikeRepository;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final MemberService memberService;
    private final PostService postService;

    @Override
    @Transactional
    public boolean toggleLike(Long postId, Long memberId) {
        Member member = memberService.getActiveMemberEntityById(memberId);
        Post post = postService.getPostEntityById(postId);
        Optional<PostLike> existingLike = postLikeRepository.findByMemberAndPost(member, post);
        if(existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get());
            post.decreaseLike();
            return false;
        }

        PostLike postLike = PostLike.create(member, post);
        postLikeRepository.save(postLike);
        post.increaseLike();
        return true;

    }

    @Override
    public boolean hasLiked(Long postId, Long memberId) {
        Member member = memberService.getMemberEntityById(memberId);
        Post post = postService.getPostEntityById(postId);
        return postLikeRepository.existsByMemberAndPost(member, post);

    }
}
