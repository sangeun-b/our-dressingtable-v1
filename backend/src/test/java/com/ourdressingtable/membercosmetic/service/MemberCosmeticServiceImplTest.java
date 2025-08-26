package com.ourdressingtable.membercosmetic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ourdressingtable.auth.dto.CustomUserDetails;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.security.WithCustomUser;
import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.cosmeticbrand.domain.CosmeticBrand;
import com.ourdressingtable.cosmeticcategory.domain.CosmeticCategory;
import com.ourdressingtable.dressingtable.domain.DressingTable;
import com.ourdressingtable.dressingtable.service.DressingTableService;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Role;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.service.MemberService;
import com.ourdressingtable.membercosmetic.domain.MemberCosmetic;
import com.ourdressingtable.membercosmetic.dto.CreateMemberCosmeticRequest;
import com.ourdressingtable.membercosmetic.dto.UpdateMemberCosmeticRequest;
import com.ourdressingtable.membercosmetic.repository.MemberCosmeticRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("회원 화장품 Service 테스트")
public class MemberCosmeticServiceImplTest {

    @InjectMocks
    private MemberCosmeticServiceImpl memberCosmeticService;

    @Mock
    private MemberCosmeticRepository memberCosmeticRepository;

    @Mock
    private DressingTableService dressingTableService;

    @Mock
    private MemberService memberService;

    @BeforeEach
    void setUpSecurityContext() {
        CustomUserDetails userDetails = CustomUserDetails.builder()
                .memberId(1L)
                .email("test@example.com")
                .password("password")
                .role(Role.ROLE_BASIC)
                .status(Status.ACTIVE)
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }


    @Nested
    @DisplayName("회원 화장품 등록 테스트")
    class CreateMemberCosmetic {

        @DisplayName("회원 화장품 등록 성공")
        @WithCustomUser
        @Test
        public void createMemberCosmetic_shouldReturnSuccess() throws Exception {
            CreateMemberCosmeticRequest request = TestDataFactory.testMemberCosmeticRequest(1L);
            Member member = TestDataFactory.testMember(1L);
            DressingTable dressingTable = TestDataFactory.testDressingTable(1L,member);
            given(dressingTableService.getDressingTableEntityById(1L)).willReturn(dressingTable);
            given(memberService.getActiveMemberEntityById(member.getId())).willReturn(member);

            given(memberCosmeticRepository.save(any(MemberCosmetic.class)))
                    .willAnswer(invocation -> {
                        MemberCosmetic memberCosmetic = invocation.getArgument(0);
                        ReflectionTestUtils.setField(memberCosmetic, "id",10L);
                        return memberCosmetic;
                    });

            Long id = memberCosmeticService.createMemberCosmetic(request);

            assertEquals(10L, id);
            then(memberCosmeticRepository).should().save(any(MemberCosmetic.class));
        }

        @DisplayName("회원 화장품 등록 실패 - 존재하지 않는 화장대")
        @WithCustomUser
        @Test
        public void createMemberCosmetic_shouldReturnDressingTableNotFoundError() throws Exception {
            given(dressingTableService.getDressingTableEntityById(100L)).willThrow(new OurDressingTableException(
                    ErrorCode.DRESSING_TABLE_NOT_FOUND));

            CreateMemberCosmeticRequest request = TestDataFactory.testMemberCosmeticRequest(100L);

            assertThrows(OurDressingTableException.class, () -> memberCosmeticService.createMemberCosmetic(request));


        }
    }

    @Nested
    @DisplayName("회원 화장품 수정 테스트")
    class UpdateMemberCosmetic {

        @DisplayName("회원 화장품 수정 성공")
        @Test
        public void updateMemberCosmetic_returnSuccess() throws Exception {
            Member member = TestDataFactory.testMember(1L);
            CosmeticBrand cosmeticBrand = TestDataFactory.testCosmeticBrand(1L);
            CosmeticCategory cosmeticCategory = TestDataFactory.testCosmeticCategory(1L);
            DressingTable dressingTable = TestDataFactory.testDressingTable(1L, member);
            MemberCosmetic memberCosmetic = TestDataFactory.testMemberCosmetic(1L, cosmeticBrand,
                    cosmeticCategory, dressingTable, member);

            UpdateMemberCosmeticRequest request = UpdateMemberCosmeticRequest.builder()
                    .name("new name")
                    .price(JsonNullable.of(new java.math.BigDecimal("50000")))
                    .imageUrl(JsonNullable.undefined())
                    .notificationDate(JsonNullable.of(java.time.LocalDate.now()))
                    .store(JsonNullable.of("공식 홈페이지"))
                    .build();

            when(memberCosmeticRepository.findById(1L)).thenReturn(Optional.of(memberCosmetic));
            memberCosmeticService.updateMemberCosmetic(1L, request);

            assertThat(memberCosmetic.getName()).isEqualTo("new name");
            assertThat(memberCosmetic.getPrice()).isEqualTo("50000");

        }

        @DisplayName("회원 화장품 수정 실패")
        @Test
        public void updateMemberCosmetic_returnForbiddenError() throws Exception {
            Member owner = TestDataFactory.testMember(1L);
            CosmeticBrand cosmeticBrand = TestDataFactory.testCosmeticBrand(1L);
            CosmeticCategory cosmeticCategory = TestDataFactory.testCosmeticCategory(1L);
            DressingTable dressingTable = TestDataFactory.testDressingTable(1L, owner);
            MemberCosmetic memberCosmetic = TestDataFactory.testMemberCosmetic(
                    1L, cosmeticBrand, cosmeticCategory, dressingTable, owner);

            when(memberCosmeticRepository.findById(1L)).thenReturn(Optional.of(memberCosmetic));

            UpdateMemberCosmeticRequest request = UpdateMemberCosmeticRequest.builder()
                    .name("new name")
                    .price(JsonNullable.of(new java.math.BigDecimal("50000")))
                    .store(JsonNullable.of("공식 홈페이지"))
                    .build();

            // when & then
            try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
                mocked.when(SecurityUtil::getCurrentMemberId).thenReturn(10L); // 다른 사용자라고 가정

                assertThatThrownBy(() -> memberCosmeticService.updateMemberCosmetic(1L, request))
                        .isInstanceOf(OurDressingTableException.class)
                        .hasMessageContaining(ErrorCode.NO_PERMISSION_FOR_MEMBER_COSMETIC.getMessage());
            }

        }


    }

}
