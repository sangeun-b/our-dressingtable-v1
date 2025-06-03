package com.ourdressingtable.common.util;

import com.ourdressingtable.community.post.domain.Post;
import com.ourdressingtable.community.post.dto.CreatePostRequest;
import com.ourdressingtable.community.post.dto.UpdatePostRequest;
import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Role;
import com.ourdressingtable.member.domain.SkinType;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.dto.CreateMemberRequest;
import com.ourdressingtable.member.dto.OtherMemberResponse;
import com.ourdressingtable.member.dto.UpdateMemberRequest;
import com.ourdressingtable.security.dto.CustomUserDetails;

public class TestDataFactory {

    public static Member testMember(Long id) {
        return Member.builder()
                .id(id)
                .email("test@example.com")
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

    public static CustomUserDetails testUserDetails(Long memberId) {
        return CustomUserDetails.builder()
                .memberId(memberId)
                .email("user" + memberId + "@example.com")
                .password("password")
                .role(Role.ROLE_BASIC)
                .status(Status.ACTIVATE)
                .build();
    }
    public static Post testPost(Long id, Member member) {
        return Post.builder()
                .id(id)
                .title("제목")
                .content("내용")
                .member(member)
                .communityCategory(CommunityCategory.builder().id(1L).build())
                .build();
    }

}
