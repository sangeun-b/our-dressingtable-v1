package com.ourdressingtable.common.config;

import com.ourdressingtable.community.comment.domain.Comment;
import com.ourdressingtable.community.comment.repository.CommentRepository;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.repository.PostRepository;
import com.ourdressingtable.communitycategory.domain.CommunityCategory;
import com.ourdressingtable.communitycategory.repository.CommunityCategoryRepository;
import com.ourdressingtable.cosmeticbrand.domain.CosmeticBrand;
import com.ourdressingtable.cosmeticbrand.repository.CosmeticBrandRepository;
import com.ourdressingtable.cosmeticcategory.domain.CosmeticCategory;
import com.ourdressingtable.cosmeticcategory.repository.CosmeticCategoryRepository;
import com.ourdressingtable.dressingtable.domain.DressingTable;
import com.ourdressingtable.dressingtable.repository.DressingTableRepository;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Role;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.repository.MemberRepository;
import com.ourdressingtable.membercosmetic.domain.MemberCosmetic;
import com.ourdressingtable.membercosmetic.repository.MemberCosmeticRepository;
import java.time.LocalDate;
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
    private final DressingTableRepository dressingTableRepository;
    private final CosmeticBrandRepository cosmeticBrandRepository;
    private final CosmeticCategoryRepository cosmeticCategoryRepository;
    private final MemberCosmeticRepository memberCosmeticRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Member 생성
        String encodedPw = passwordEncoder.encode("Password123!@@@@@@@?");
        Member member = Member.builder()
                .email("sample@gmail.com")
                .password(encodedPw)
                .name("김이름")
                .phoneNumber("010-1234-5678")
                .role(Role.ROLE_BASIC)
                .status(Status.ACTIVE)
                .build();
        memberRepository.save(member);

        String second_encodedPw = passwordEncoder.encode("Password123!@@@@@@@?");
        Member second_member = Member.builder()
                .email("test@gmail.com")
                .password(second_encodedPw)
                .name("김이번")
                .phoneNumber("010-7777-8765")
                .role(Role.ROLE_BASIC)
                .status(Status.ACTIVE)
                .build();
        memberRepository.save(second_member);

        // Category 생성
        CommunityCategory free = CommunityCategory.builder()
                .name("자유게시판")
                .code("FREE")
                .build();
        CommunityCategory qna = CommunityCategory.builder()
                .name("질문")
                .code("QNA")
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

        // DressingTable 생성
        DressingTable dressingTable = DressingTable.builder()
                .name("나의 화장대")
                .imageUrl("https://image.img")
                .member(member)
                .build();

        dressingTableRepository.save(dressingTable);

        // CosmeticBrand 생성
        CosmeticBrand cosmeticBrand = CosmeticBrand.builder()
                .name("헤라").build();

        cosmeticBrandRepository.save(cosmeticBrand);

        // CosmeticCategory 생성
        CosmeticCategory cosmeticCategory = CosmeticCategory.builder()
                .name("메이크업")
                .build();
        cosmeticCategoryRepository.save(cosmeticCategory);

        // MemberCosmetic 생성
        MemberCosmetic memberCosmetic = MemberCosmetic.builder()
                .brand(cosmeticBrand)
                .category(cosmeticCategory)
                .dressingTable(dressingTable)
                .member(member)
                .name("블랙 쿠션")
                .openDate(LocalDate.now())
                .build();
        memberCosmeticRepository.save(memberCosmetic);
    }

}
