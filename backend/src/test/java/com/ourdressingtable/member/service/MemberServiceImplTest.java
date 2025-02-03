package com.ourdressingtable.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Role;
import com.ourdressingtable.member.dto.CreateMemberRequest;
import com.ourdressingtable.member.repository.MemberRepository;
import com.ourdressingtable.member.service.impl.MemberServiceImpl;
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
        void createMember_Success() {
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

    }
}
