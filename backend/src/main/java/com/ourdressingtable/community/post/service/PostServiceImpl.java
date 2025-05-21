package com.ourdressingtable.community.post.service;

import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.repository.PostRepository;
import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.communityCategory.service.CommunityCategoryServiceImpl;
import com.ourdressingtable.exception.ErrorCode;
import com.ourdressingtable.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Member;
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
    private final CommunityCategoryServiceImpl communityCategoryService;

    @Override
    @Transactional
    public Long createPost(CreatePostRequest request, Long memberId) {
        CommunityCategory communityCategory = communityCategoryService.getCategoryEntityById(request.getCommunityCategoryId());
        // TODO: member 조회 변경 필요, Entity 조회 method 추가하기
//        Member member = memberService.getMemberEntityById(memberId);
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .communityCategory(communityCategory)
                .member(Member.builder().id(memberId).build())
                .build();
        postRepository.save(post);
        return post.getId();
    }

    @Override
    public Post getPost(Long id) {
        return null;
    }

    @Override
    public List<Post> getPosts() {
        return List.of();
    }

    @Override
    public void deletePost(Long id) {

    }

    @Override
    @Transactional
    public void updatePost(Long postId, UpdatePostRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new OurDressingTableException(ErrorCode.POST_NOT_FOUND));

        if (request.getTitle() != null)
            post.updateTitle(request.getTitle());

        if (request.getContent() != null)
            post.updateContent(request.getContent());

        if (request.getCommunityCategoryId() != null) {
            CommunityCategory communityCategory = communityCategoryService.getCategoryEntityById(request.getCommunityCategoryId());
            post.updateCommunityCategory(communityCategory);
        }

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

    @Override
    public Post getPostEntityById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new OurDressingTableException(ErrorCode.POST_NOT_FOUND));
    }
}
