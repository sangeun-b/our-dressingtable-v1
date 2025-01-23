package com.ourdressingtable.domain.member.service.impl;

import com.ourdressingtable.domain.member.domain.Member;
import com.ourdressingtable.domain.member.dto.CreateMemberRequest;
import com.ourdressingtable.domain.member.repository.MemberRepository;
import com.ourdressingtable.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long createMember(CreateMemberRequest createMemberRequest) {
        Member member = memberRepository.save(createMemberRequest.toEntity());
        return member.getId();
    }
}
