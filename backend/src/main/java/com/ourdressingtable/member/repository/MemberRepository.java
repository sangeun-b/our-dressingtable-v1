package com.ourdressingtable.member.repository;

import com.ourdressingtable.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(@NotBlank(message = "이메일을 입력해주세요.")String email);
    Optional<Member > findByEmail(String email);
    boolean existsByNickname(@NotBlank(message = "별명을 입력해주세요.") String nickname);
}
