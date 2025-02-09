package com.ourdressingtable.member.repository;

import com.ourdressingtable.member.domain.WithdrawalMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalMemberRepository extends JpaRepository<WithdrawalMember, Long> {

}
