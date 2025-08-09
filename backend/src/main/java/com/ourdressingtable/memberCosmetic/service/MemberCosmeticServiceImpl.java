package com.ourdressingtable.membercosmetic.service;

import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.dressingtable.domain.DressingTable;
import com.ourdressingtable.dressingtable.service.DressingTableService;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import com.ourdressingtable.membercosmetic.domain.MemberCosmetic;
import com.ourdressingtable.membercosmetic.dto.CreateMemberCosmeticRequest;
import com.ourdressingtable.membercosmetic.repository.MemberCosmeticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberCosmeticServiceImpl implements
        MemberCosmeticService {

    private final MemberCosmeticRepository memberCosmeticRepository;
    private final DressingTableService dressingTableService;
    private final MemberService memberService;

    @Override
    public Long createMemberCosmetic(CreateMemberCosmeticRequest request) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberService.getActiveMemberEntityById(memberId);
        DressingTable dressingTable = dressingTableService.getDressingTableEntityById(request.getDressingTableId());
        MemberCosmetic memberCosmetic = request.toEntity(dressingTable, member);
        memberCosmeticRepository.save(memberCosmetic);
        return memberCosmetic.getId();
    }
}
