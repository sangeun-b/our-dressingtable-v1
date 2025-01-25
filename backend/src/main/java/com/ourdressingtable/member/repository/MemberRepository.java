package com.ourdressingtable.member.repository;

import com.ourdressingtable.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
