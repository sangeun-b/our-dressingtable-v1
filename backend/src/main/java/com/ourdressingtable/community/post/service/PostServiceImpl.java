package com.ourdressingtable.community.post.service;

import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.dto.PostDetailResponse;
import com.ourdressingtable.community.post.dto.PostResponse;
import com.ourdressingtable.community.post.dto.PostSearchCondition;
import com.ourdressingtable.community.post.repository.PostRepository;
import com.ourdressingtable.community.service.CommunityService;
import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.communityCategory.service.CommunityCategoryService;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;
    private final CommunityCategoryService communityCategoryService;

    @Override
    @Transactional
    public Long createPost(CreatePostRequest request, Long memberId) {
        CommunityCategory communityCategory = communityCategoryService.getCategoryEntityById(request.getCommunityCategoryId());
        Member member = memberService.getActiveMemberEntityById(memberId);
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .communityCategory(communityCategory)
                .member(member)
                .build();
        postRepository.save(post);
        return post.getId();
    }

    @Override
    public Page<PostResponse> getPosts(PostSearchCondition condition, Pageable pageable) {
        Page<Post> posts = postRepository.search(condition, pageable);
        return posts.map(PostResponse::from);
    }

    @Override
    public void deletePost(Long postId) {
        Post post = getValidPostEntityById(postId);

        post.markAsDeleted();

    }

    @Override
    @Transactional
    public void updatePost(Long postId, UpdatePostRequest request) {
        Post post = getValidPostEntityById(postId);

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

    @Override
    public Post getValidPostEntityById(Long id) {
        return postRepository.findById(id).filter(post -> !post.isDeleted())
                .orElseThrow(() -> new OurDressingTableException(ErrorCode.POST_NOT_FOUND));
    }
}
