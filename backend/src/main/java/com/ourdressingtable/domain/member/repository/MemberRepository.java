package com.ourdressingtable.domain.member.repository;

import com.ourdressingtable.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
