package com.ourdressingtable.auth.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.repository.MemberRepository;
import com.ourdressingtable.auth.service.CustomUserDetailsService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("사용자 조회 테스트")
    class LoadMember {
        @DisplayName("사용자 조회 성공")
        @Test
        public void loadMemberByUsername_returnSuccess() {
            // given
            Member member = TestDataFactory.testMember(1L);
            when(memberRepository.findByEmail(member.getEmail()))
                    .thenReturn(Optional.of(member));

            // when
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(member.getEmail());

            // then
            assertNotNull(userDetails);
            assertEquals(member.getEmail(), userDetails.getUsername());
        }

        @DisplayName("사용자 조회 실패 - USERNAME NOT FOUND")
        @Test
        public void loadMemberByUsername_returnNotFoundError() {
            // given
            String email = "unknown@example.com";
            when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

            // then
            assertThrows(UsernameNotFoundException.class, () -> {
                customUserDetailsService.loadUserByUsername(email);
            });
        }
    }

}
