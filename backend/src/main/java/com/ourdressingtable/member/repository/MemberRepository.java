package com.ourdressingtable.member.repository;

import com.ourdressingtable.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Optional<Member > findByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);
}
