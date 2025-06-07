package com.ourdressingtable.common.config;

import com.ourdressingtable.community.comment.domain.Comment;
import com.ourdressingtable.community.comment.repository.CommentRepository;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.repository.PostRepository;
import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import com.ourdressingtable.communityCategory.repository.CommunityCategoryRepository;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Role;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Profile("local")
public class DummyDataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final CommunityCategoryRepository communityCategoryRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Member 생성
        String encodedPw = passwordEncoder.encode("Password123!@@@@@@@?");
        Member member = Member.builder()
                .email("sample@gmail.com")
                .password(encodedPw)
                .name("김이름")
                .role(Role.ROLE_BASIC)
                .status(Status.ACTIVE)
                .build();
        memberRepository.save(member);

        // Category 생성
        CommunityCategory free = CommunityCategory.builder()
                .name("자유게시판")
                .build();
        CommunityCategory qna = CommunityCategory.builder()
                .name("질문")
                .build();

        List<CommunityCategory> categories = List.of(free, qna);

        communityCategoryRepository.saveAll(categories);

        // Post 생성
        Post freePost = Post.builder()
                .title("안녕하세요!")
                .content("첫 번째 게시글입니다.")
                .member(member)
                .communityCategory(free)
                .build();

        Post qnaPost = Post.builder()
                .title("질문이 있어요")
                .content("게시글 등록은 어떻게 하나요?")
                .member(member)
                .communityCategory(qna)
                .build();
        postRepository.saveAll(List.of(freePost, qnaPost));

        // Comment 생성
        Comment comment1 = Comment.builder()
                .content("환영합니다~")
                .post(freePost)
                .member(member)
                .build();

        Comment comment2 = Comment.builder()
                .content("저도 궁금해요!")
                .post(qnaPost)
                .member(member)
                .build();

        commentRepository.saveAll(List.of(comment1, comment2));
    }

}
