package com.ourdressingtable.dressingtable.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.TestDataFactory;
import com.ourdressingtable.dressingtable.domain.DressingTable;
import com.ourdressingtable.dressingtable.dto.CreateDressingTableRequest;
import com.ourdressingtable.dressingtable.dto.DressingTableResponse;
import com.ourdressingtable.dressingtable.dto.UpdateDressingTableRequest;
import com.ourdressingtable.dressingtable.repository.DressingTableRepository;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.domain.Role;
import com.ourdressingtable.member.domain.Status;
import com.ourdressingtable.member.service.MemberService;
import com.ourdressingtable.auth.dto.CustomUserDetails;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("화장대 Service 테스트")
public class DressingTableServiceImplTest {

    @InjectMocks
    private DressingTableServiceImpl dressingTableService;

    @Mock
    private DressingTableRepository dressingTableRepository;

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
    @DisplayName("화장대 생성 테스트")
    class CreateDressingTable {

        @DisplayName("화장대 생성 성공")
        @Test
        public void createDressingTable_shouldReturnSuccess() throws Exception {
            // given
            CreateDressingTableRequest createDressingTableRequest = TestDataFactory.testCreateDressingTableRequest();

            Member member = TestDataFactory.testMember(1L);
            given(memberService.getActiveMemberEntityById(member.getId())).willReturn(member);

            given(dressingTableRepository.save(any(DressingTable.class)))
                    .willAnswer(invocation -> {
                        DressingTable dressingTable = invocation.getArgument(0);
                        ReflectionTestUtils.setField(dressingTable, "id", 100L);
                        return dressingTable;
                    });

            Long id = dressingTableService.createDressingTable(createDressingTableRequest);

            assertEquals(100L, id);
            then(dressingTableRepository).should().save(any(DressingTable.class));
        }

        @DisplayName("화장대 생성 실패 - 존재하지 않는 회원")
        @Test
        public void createDressingTable_shouldReturnMemberNotFoundError() throws Exception {
            // given
            CustomUserDetails userDetails = TestDataFactory.testUserDetails(100L);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            CreateDressingTableRequest createDressingTableRequest = TestDataFactory.testCreateDressingTableRequest();

            given(memberService.getActiveMemberEntityById(100L))
                    .willThrow(new OurDressingTableException(ErrorCode.MEMBER_NOT_FOUND));

            assertThrows(OurDressingTableException.class, () -> dressingTableService.createDressingTable(createDressingTableRequest));


        }
    }

    @Nested
    @DisplayName("화장대 수정 테스트")
    class UpdateDressingTable {

        @DisplayName("화장대 수정 성공")
        @Test
        public void updateDressingTable_shouldReturnSuccess() throws Exception {
            UpdateDressingTableRequest updateDressingTableRequest = TestDataFactory.testUpdateDressingTableRequest();
            Member member = TestDataFactory.testMember(1L);
            DressingTable dressingTable = TestDataFactory.testDressingTable(1L, member);

            given(dressingTableRepository.findById(dressingTable.getId())).willReturn(Optional.of(dressingTable));

            dressingTableService.updateDressingTable(updateDressingTableRequest, 1L);

            assertEquals("새 이름", dressingTable.getName());
        }

        @DisplayName("화장대 수정 실패")
        @Test
        public void updateDressingTable_shouldReturnDressingTableNotFoundError() throws Exception {
            given(dressingTableRepository.findById(1L)).willReturn(Optional.empty());

            UpdateDressingTableRequest updateDressingTableRequest = TestDataFactory.testUpdateDressingTableRequest();

            assertThrows(OurDressingTableException.class, () -> dressingTableService.updateDressingTable(updateDressingTableRequest,1L));
        }
    }

    @Nested
    @DisplayName("화장대 삭제 테스트")
    class DeleteDressingTable {

        @DisplayName("화장대 삭제 성공")
        @Test
        public void deleteDressingTable_shouldReturnSuccess() throws Exception {
            Member member = TestDataFactory.testMember(1L);
            DressingTable dressingTable = TestDataFactory.testDressingTable(1L, member);

            given(memberService.getActiveMemberEntityById(1L)).willReturn(member);
            given(dressingTableRepository.findById(1L)).willReturn(Optional.of(dressingTable));

            dressingTableService.deleteDressingTable(1L);

            assertThat(dressingTable.isDeleted()).isTrue();

        }

        @DisplayName("화장대 삭제 실패 - 권한 없음")
        @Test
        public void deleteDressingTable_shouldReturnForbiddenError() throws Exception {
            CustomUserDetails customUserDetails = TestDataFactory.testUserDetails(100L);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    customUserDetails,  customUserDetails.getPassword(), customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

            Member member = TestDataFactory.testMember(1L);

            DressingTable dressingTable = TestDataFactory.testDressingTable(1L, member);

            given(dressingTableRepository.findById(1L)).willReturn(Optional.of(dressingTable));

            assertThatThrownBy(() -> dressingTableService.deleteDressingTable(1L))
                    .isInstanceOf(OurDressingTableException.class)
                    .hasMessageContaining(ErrorCode.FORBIDDEN.getMessage());
        }

    }

    @Nested
    @DisplayName("나의 모든 화장대 조회 테스트")
    class GetAllMyDressingTable {

        @DisplayName("나의 모든 화장대 조회 성공")
        @Test
        public void getAllMyDressingTable_shouldReturnSuccess() throws Exception {
            Member member = TestDataFactory.testMember(1L);
            List<DressingTable> dressingTableList = List.of(TestDataFactory.testDressingTable(1L, member),
                                                            TestDataFactory.testDressingTable(2L, member));

            given(dressingTableRepository.findAllByMemberId(1L)).willReturn(dressingTableList);

            List<DressingTableResponse> result = dressingTableService.getAllMyDressingTables();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo(dressingTableList.get(0).getName());
        }

        @DisplayName("나의 모든 화장대 조회 실패 - 인증 정보 없음")
        @Test
        public void getAllMyDressingTable_shouldReturnUnauthorizedError() throws Exception {
            SecurityContextHolder.clearContext();

            assertThatThrownBy(() -> dressingTableService.getAllMyDressingTables())
                    .isInstanceOf(OurDressingTableException.class)
                    .hasMessageContaining(ErrorCode.UNAUTHORIZED.getMessage());
        }
    }
}
