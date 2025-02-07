package com.ourdressingtable.member.service;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ourdressingtable.exception.ErrorCode;
import com.ourdressingtable.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Role;
import com.ourdressingtable.member.dto.CreateMemberRequest;
import com.ourdressingtable.member.dto.OtherMemberResponse;
import com.ourdressingtable.member.repository.MemberRepository;
import com.ourdressingtable.member.service.impl.MemberServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberServiceImpl;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("회원 가입 테스트")
    class singUp {
        @DisplayName("회원 가입 성공")
        @Test
        public void createMember_shouldReturnSuccess() {
            // given
            CreateMemberRequest createMemberRequest = CreateMemberRequest.builder()
                    .email("member1@gmail.com")
                    .password("Password123!")
                    .name("member1")
                    .nickname("me")
                    .phoneNumber("010-1234-5678")
                    .role(Role.ROLE_MEMBER)
                    .build();

            Member member = createMemberRequest.toEntity();
            ReflectionTestUtils.setField(member,"id",1L);
            when(memberRepository.save(any(Member.class))).thenReturn(member);

            // when
            Long savedId = memberServiceImpl.createMember(createMemberRequest);

            //then
            assertNotNull(savedId);
            assertEquals(createMemberRequest.getEmail(), member.getEmail());

        }

        @DisplayName("회원 가입 실패_중복 이메일")
        @Test
        public void createMember_withDuplicateEmail_shouldReturnError() {
            // given
            CreateMemberRequest createMemberRequest = CreateMemberRequest.builder()
                    .email("member1@gmail.com")
                    .password("Password123!")
                    .name("member1")
                    .nickname("me")
                    .phoneNumber("010-1234-5678")
                    .role(Role.ROLE_MEMBER)
                    .build();

            when(memberRepository.existsByEmail(createMemberRequest.getEmail())).thenReturn(true);

            // when & then
            OurDressingTableException ourDressingTableException = assertThrows(OurDressingTableException.class, () -> memberServiceImpl.createMember(createMemberRequest));
            assertEquals(ourDressingTableException.getHttpStatus(), ErrorCode.EMAIL_ALREADY_EXISTS.getHttpStatus());
            assertEquals(ourDressingTableException.getMessage(), ErrorCode.EMAIL_ALREADY_EXISTS.getMessage());

        }
    }

    @Nested
    @DisplayName("회원 조회 테스트")
    class findUser {

        @DisplayName("회원 조회 성공 테스트")
        @Test
        public void findUser_ShouldReturnSuccess() {
            // given
            Member member = Member.builder()
                    .id(1L)
                    .email("member1@gmail.com")
                    .password("Password123!")
                    .name("member1")
                    .nickname("me")
                    .phoneNumber("010-1234-5678")
                    .role(Role.ROLE_MEMBER)
                    .build();

            given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

            // when
            OtherMemberResponse findMember = memberServiceImpl.getMember(member.getId());

            //then
            assertEquals(findMember.getNickname(),member.getNickname());

        }

        @DisplayName("회원 조회 실패 테스트 - USER NOT FOUND")
        @Test
        public void findUser_ShouldReturnUserNotFoundError() {
            // given
            Member member = Member.builder()
                    .id(1L)
                    .email("member1@gmail.com")
                    .password("Password123!")
                    .name("member1")
                    .nickname("me")
                    .phoneNumber("010-1234-5678")
                    .role(Role.ROLE_MEMBER)
                    .build();
            given(memberRepository.findById(member.getId())).willReturn(Optional.empty());

            // when
            OurDressingTableException exception = assertThrows(OurDressingTableException.class, () -> memberServiceImpl.getMember(member.getId()));

            //then
            assertEquals(exception.getHttpStatus(), ErrorCode.MEMBER_NOT_FOUND.getHttpStatus());

        }
    }
}
