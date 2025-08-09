package com.ourdressingtable.dressingtable.service;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.dressingtable.domain.DressingTable;
import com.ourdressingtable.dressingtable.dto.CreateDressingTableRequest;
import com.ourdressingtable.dressingtable.dto.DressingTableDetailResponse;
import com.ourdressingtable.dressingtable.dto.DressingTableResponse;
import com.ourdressingtable.dressingtable.dto.UpdateDressingTableRequest;
import com.ourdressingtable.dressingtable.repository.DressingTableRepository;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import java.util.List;
import java.util.stream.Collectors;
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
    public Long createDressingTable(CreateDressingTableRequest dressingTableRequest) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberService.getActiveMemberEntityById(memberId);
        DressingTable dressingTable = dressingTableRequest.toEntity(member);
        dressingTableRepository.save(dressingTable);
        return dressingTable.getId();
    }

    @Override
    @Transactional
    public void updateDressingTable(UpdateDressingTableRequest dressingTableRequest, Long id) {
        Long memberId = SecurityUtil.getCurrentMemberId();
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

    @Override
    @Transactional
    public void deleteDressingTable(Long id) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        DressingTable dressingTable = dressingTableRepository.findById(id)
                .orElseThrow(() -> new OurDressingTableException(ErrorCode.DRESSING_TABLE_NOT_FOUND));

        if(!dressingTable.getMember().getId().equals(memberId)){
            throw new OurDressingTableException(ErrorCode.FORBIDDEN);
        }

        dressingTable.markAsDeleted();
    }

    @Override
    public List<DressingTableResponse> getAllMyDressingTables() {
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<DressingTable> dressingTableList = dressingTableRepository.findAllByMemberId(memberId);
        return dressingTableList.stream().map(DressingTableResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public DressingTableDetailResponse getDressingTableDetail(Long id) {
        DressingTable dressingTable = dressingTableRepository.findById(id)
                .orElseThrow(() -> new OurDressingTableException(ErrorCode.DRESSING_TABLE_NOT_FOUND));
        return DressingTableDetailResponse.from(dressingTable);
    }

    @Override
    public DressingTable getDressingTableEntityById(Long id) {
        return dressingTableRepository.findById(id)
                .orElseThrow(() -> new OurDressingTableException(ErrorCode.DRESSING_TABLE_NOT_FOUND));
    }
}
