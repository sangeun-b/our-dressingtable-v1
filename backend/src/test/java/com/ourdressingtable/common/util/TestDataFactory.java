package com.ourdressingtable.common.util;

import com.ourdressingtable.auth.dto.FindEmailRequest;
import com.ourdressingtable.community.comment.domain.Comment;
import com.ourdressingtable.community.comment.dto.CreateCommentRequest;
import com.ourdressingtable.community.comment.dto.UpdateCommentRequest;
import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.domain.PostLike;
import com.ourdressingtable.community.post.dto.*;
import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import com.ourdressingtable.dressingTable.domain.DressingTable;
import com.ourdressingtable.dressingTable.dto.CreateDressingTableRequest;
import com.ourdressingtable.dressingTable.dto.DressingTableResponse;
import com.ourdressingtable.dressingTable.dto.UpdateDressingTableRequest;
import com.ourdressingtable.member.domain.*;
import com.ourdressingtable.member.dto.request.CreateMemberRequest;
import com.ourdressingtable.member.dto.response.MemberResponse;
import com.ourdressingtable.member.dto.response.OtherMemberResponse;
import com.ourdressingtable.member.dto.request.UpdateMemberRequest;
import com.ourdressingtable.member.dto.request.WithdrawalMemberRequest;
import com.ourdressingtable.auth.email.dto.ConfirmEmailVerificationCodeRequest;
import com.ourdressingtable.auth.email.dto.SendEmailVerificationCodeRequest;
import com.ourdressingtable.auth.dto.CustomUserDetails;
import com.ourdressingtable.auth.dto.LoginRequest;
import com.ourdressingtable.auth.email.dto.ConfirmPasswordResetRequest;
import com.ourdressingtable.auth.email.dto.ResetPasswordEmailRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TestDataFactory {

    public static Member testMember(Long id) {
        return Member.builder()
                .id(id)
                .email("test@example.com")
                .password("{noop}Password123!")
                .phoneNumber("010-1234-5678")
                .name("김이름")
                .nickname("mee")
                .status(Status.ACTIVE)
                .role(Role.ROLE_BASIC)
                .build();
    }

    public static Member testMemberWithEmailNull(Long id) {
        return Member.builder()
                .id(id)
                .email(null)
                .phoneNumber("010-1234-5678")
                .role(Role.ROLE_BASIC)
                .build();
    }
    public static CreateMemberRequest testCreateMemberRequest() {
        return CreateMemberRequest.builder()
                .email("member1@gmail.com")
                .password("Password123!")
                .name("member1")
                .nickname("me")
                .phoneNumber("010-1234-5678")
                .build();
    }

    public static UpdateMemberRequest testUpdateMemberRequest() {
        return UpdateMemberRequest.builder().nickname("new me").build();
    }

    public static OtherMemberResponse testOtherMemberResponse() {
        return OtherMemberResponse.builder()
                .nickname("me")
                .skinType(SkinType.OILY_SKIN)
                .build();
    }

    public static MemberResponse testMemberResponse() {
        return MemberResponse.builder()
                .email("test@example.com")
                .name("김이름")
                .nickname("mee")
                .skinType(SkinType.OILY_SKIN)
                .colorType(ColorType.AUTUMN_WARM)
                .role(String.valueOf(Role.ROLE_BASIC))
                .build();
    }

    public static WithdrawalMemberRequest testWithdrawalMemberRequest() {
        return WithdrawalMemberRequest.builder()
                .reason("재가입 예정")
                .password("{noop}Password123!")
                .isBlock(false)
                .build();
    }

    public static WithdrawalMember testWithdrawalMember(String email, LocalDateTime withdrewAt, boolean isBlock) {
        return WithdrawalMember.builder()
                .hashedEmail(HashUtil.hash(email))
                .maskedEmail(MaskingUtil.maskedEmail(email))
                .hashedPhone(HashUtil.hash("010-1234-5678"))
                .maskedPhone(MaskingUtil.maskedPhone("010-1234-5678"))
                .reason("테스트")
                .isBlock(isBlock)
                .withdrewAt(Timestamp.valueOf(withdrewAt))
                .member(testMember(99L))
                .build();
    }

    public static LoginRequest testLoginRequest() {
        return LoginRequest.builder()
                .email("test@example.com")
                .password("Password123!")
                .build();
    }

    public static CreatePostRequest testCreatePostRequest() {
        return CreatePostRequest.builder()
                .title("Test 제목")
                .content("Test 내용")
                .communityCategoryId(1L)
                .build();
    }

    public static UpdatePostRequest testUpdatePostRequest() {
        return UpdatePostRequest.builder()
                .title("수정 제목")
                .content("수정 내용")
                .communityCategoryId(2L)
                .build();
    }

    public static PostDetailResponse testPostDetailResponse() {
        return PostDetailResponse.builder()
                .id(1L)
                .title("제목")
                .content("내용")
                .categoryName("후기")
                .viewCount(10)
                .likeCount(5)
                .likedByCurrentMember(true)
                .author("사용자1")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public static CommunityCategory testCommunityCategory(Long id) {
        return CommunityCategory.builder()
                .id(id)
                .name("자유")
                .build();
    }

    public static CustomUserDetails testUserDetails(Long memberId) {
        return CustomUserDetails.builder()
                .memberId(memberId)
                .email("user" + memberId + "@example.com")
                .password("password")
                .role(Role.ROLE_BASIC)
                .status(Status.ACTIVE)
                .build();
    }

    public static PostSearchCondition testPostSearchCondition(String searchType, String keyword, String category, String sortBy) {
        return PostSearchCondition.builder()
                .searchType(searchType)
                .keyword(keyword)
                .category(category)
                .sortBy(sortBy)
                .build();
    }

    public static MyPostSearchCondition testMyPostSearchCondition(String sortBy) {
        return MyPostSearchCondition.builder()
                .sortBy(sortBy).build();
    }

    public static Post testPost(Long id, Member member, CommunityCategory communityCategory) {
        return Post.builder()
                .id(id)
                .title("제목")
                .content("내용")
                .member(member)
                .communityCategory(communityCategory)
                .isDeleted(false)
                .likeCount(5)
                .build();
    }

    public static CreateDressingTableRequest testCreateDressingTableRequest() {
        return CreateDressingTableRequest.builder()
                .name("이름")
                .imageUrl("https://image.img")
                .build();
    }

    public static CreateDressingTableRequest testCreateDressingTableRequestWithNameNull() {
        return CreateDressingTableRequest.builder()
                .imageUrl("https://image.img")
                .build();
    }

    public static UpdateDressingTableRequest testUpdateDressingTableRequest() {
        return UpdateDressingTableRequest.builder()
                .name("새 이름")
                .build();
    }

    public static DressingTableResponse testDressingTableResponse(Long id, String name) {
        return DressingTableResponse.builder()
                .id(id)
                .name(name)
                .imageUrl("https://image.img")
                .build();
    }

    public static DressingTable testDressingTable(Long id, Member member) {
        return DressingTable.builder()
                .id(id)
                .name("나의 화장대")
                .imageUrl("https://image.img")
                .member(member)
                .build();

    }

    public static PostLike testPostLike(Long id, Member member, Post post) {
        return PostLike.builder()
                .id(id)
                .member(member)
                .post(post)
                .build();
    }

    public static CreateCommentRequest testCreateCommentRequest(Long postId) {
        return CreateCommentRequest.builder()
                .content("좋아요!")
                .postId(postId)
                .parentId(null)
                .build();
    }

    public static CreateCommentRequest testCreateCommentRequestWithParent(Long postId, Long parentId) {
        return CreateCommentRequest.builder()
                .content("좋아요!")
                .postId(postId)
                .parentId(parentId)
                .build();
    }

    public static CreateCommentRequest testCreateCommentRequestWithNull(Long postId) {
        return CreateCommentRequest.builder()
                .postId(null)
                .parentId(null)
                .build();
    }

    public static UpdateCommentRequest testUpdateCommentRequest(Long commentId) {
        return UpdateCommentRequest.builder()
                .content("수정된 내용")
                .build();
    }

    public static Comment testComment(Long id) {
        return Comment.builder()
                .id(id)
                .content("추천!")
                .depth(0)
                .build();
    }
    public static Comment testCommentWithMemberAndPost(Long id, Member member, Post post) {
        return Comment.builder()
                .id(id)
                .content("추천!")
                .depth(0)
                .member(member)
                .post(post)
                .build();
    }

    public static SendEmailVerificationCodeRequest testSendEmailVerificationCodeRequest() {
        return SendEmailVerificationCodeRequest.builder()
                .email("sample@example.com")
                .build();
    }


    public static ConfirmEmailVerificationCodeRequest testConfirmEmailVerificationCodeRequest () {
        return ConfirmEmailVerificationCodeRequest .builder()
                .email("sample@example.com")
                .verificationCode("123456")
                .build();
    }

    public static ResetPasswordEmailRequest testResetPasswordEmailRequest(String email) {
        return ResetPasswordEmailRequest.builder()
                .email(email)
                .build();
    }

    public static ConfirmPasswordResetRequest testPasswordResetRequest(String token, String password) {
        return ConfirmPasswordResetRequest.builder()
                .token(token)
                .newPassword(password)
                .build();
    }

    public static FindEmailRequest testFindEmailRequest(String name, String phone) {
        return FindEmailRequest.builder().name(name).phoneNumber(phone).build();
    }
}
