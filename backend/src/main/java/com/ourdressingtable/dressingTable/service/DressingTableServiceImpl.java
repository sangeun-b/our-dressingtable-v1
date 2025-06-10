package com.ourdressingtable.dressingTable.service;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.dressingTable.domain.DressingTable;
import com.ourdressingtable.dressingTable.dto.CreateDressingTableRequest;
import com.ourdressingtable.dressingTable.repository.DressingTableRepository;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DressingTableServiceImpl implements DressingTableService {

    private final DressingTableRepository dressingTableRepository;
    private final MemberService memberService;

    @Override
    @Transactional
    public Long createDressingTable(CreateDressingTableRequest dressingTableRequest, Long memberId) {
        Member member = memberService.getActiveMemberEntityById(memberId);
        DressingTable dressingTable = dressingTableRequest.toEntity(member);
        dressingTableRepository.save(dressingTable);
        return dressingTable.getId();
    }

    @Override
    @Transactional
    public void updateDressingTable(CreateDressingTableRequest dressingTableRequest, Long id, Long memberId) {
        DressingTable dressingTable = dressingTableRepository.findById(id).orElseThrow(() -> new OurDressingTableException(ErrorCode.DRESSING_TABLE_NOT_FOUND));

        if(!dressingTable.getMember().getId().equals(memberId)){
            throw new OurDressingTableException(ErrorCode.FORBIDDEN);
        }

        if(StringUtils.hasText(dressingTableRequest.getName())){
            dressingTable.updateName(dressingTableRequest.getName());
        }
        if(StringUtils.hasText(dressingTableRequest.getImageUrl())){
            dressingTable.updateImageUrl(dressingTableRequest.getImageUrl());
        }
    }
}
