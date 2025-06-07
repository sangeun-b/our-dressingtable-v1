package com.ourdressingtable.dressingTable.service;

import com.ourdressingtable.dressingTable.domain.DressingTable;
import com.ourdressingtable.dressingTable.dto.DressingTableRequest;
import com.ourdressingtable.dressingTable.repository.DressingTableRepository;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DressingTableServiceImpl implements DressingTableService {

    private final DressingTableRepository dressingTableRepository;
    private final MemberService memberService;

    @Override
    @Transactional
    public Long createDressingTable(DressingTableRequest dressingTableRequest, Long memberId) {
        Member member = memberService.getActiveMemberEntityById(memberId);
        DressingTable dressingTable = dressingTableRequest.toEntity(member);
        dressingTableRepository.save(dressingTable);
        return dressingTable.getId();
    }
}
