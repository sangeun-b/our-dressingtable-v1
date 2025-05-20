package com.ourdressingtable.community.service.impl;

import com.ourdressingtable.community.domain.Post;
import com.ourdressingtable.community.dto.CommunityCategoryResponse;
import com.ourdressingtable.community.dto.CreatePostRequest;
import com.ourdressingtable.community.dto.UpdatePostRequest;
import com.ourdressingtable.community.repository.PostRepository;
import com.ourdressingtable.community.service.PostService;
import com.ourdressingtable.member.dto.OtherMemberResponse;
import com.ourdressingtable.member.service.impl.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberServiceImpl memberService;

    @Override
    @Transactional
    public Long createPost(CreatePostRequest request, CommunityCategoryResponse category) {
        Long memberId = 1L;
        OtherMemberResponse otherMemberResponse = memberService.getMember(memberId);
        return 0L;
    }

    @Override
    public Optional<Post> getPost(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Post> getPosts() {
        return List.of();
    }

    @Override
    public void deletePost(Long id) {

    }

    @Override
    public void updatePost(UpdatePostRequest request) {

    }

    @Override
    public int countPosts() {
        return 0;
    }

    @Override
    public int countPostsByCategory(String category) {
        return 0;
    }

    @Override
    public List<Post> getPostsByCategory(String category) {
        return List.of();
    }

    @Override
    public List<Post> getPostsByMember(Long memberId) {
        return List.of();
    }

    @Override
    public List<Post> getPostsByTitle(String title) {
        return List.of();
    }

    @Override
    public List<Post> getPostsByContent(String content) {
        return List.of();
    }
}
