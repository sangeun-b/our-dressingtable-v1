package com.ourdressingtable.membercosmetic.service;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.common.util.SecurityUtil;
import com.ourdressingtable.cosmeticbrand.domain.CosmeticBrand;
import com.ourdressingtable.cosmeticbrand.service.CosmeticBrandService;
import com.ourdressingtable.cosmeticcategory.domain.CosmeticCategory;
import com.ourdressingtable.cosmeticcategory.service.CosmeticCategoryService;
import com.ourdressingtable.dressingtable.domain.DressingTable;
import com.ourdressingtable.dressingtable.service.DressingTableService;
import com.ourdressingtable.member.domain.Member;
import com.ourdressingtable.member.service.MemberService;
import com.ourdressingtable.membercosmetic.domain.MemberCosmetic;
import com.ourdressingtable.membercosmetic.dto.CreateMemberCosmeticRequest;
import com.ourdressingtable.membercosmetic.dto.MemberCosmeticDetailResponse;
import com.ourdressingtable.membercosmetic.dto.MemberCosmeticResponse;
import com.ourdressingtable.membercosmetic.dto.UpdateMemberCosmeticRequest;
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
    private final CosmeticBrandService cosmeticBrandService;
    private final CosmeticCategoryService cosmeticCategoryService;

    @Override
    @Transactional
    public Long createMemberCosmetic(CreateMemberCosmeticRequest request) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberService.getActiveMemberEntityById(memberId);
        DressingTable dressingTable = dressingTableService.getDressingTableEntityById(request.getDressingTableId());

        if(!dressingTable.getMember().getId().equals(memberId)) {
            throw new OurDressingTableException(ErrorCode.NO_PERMISSION_FOR_DRESSING_TABLE);
        }

        CosmeticBrand cosmeticBrand = cosmeticBrandService.getCosmeticBrandEntityById(request.getBrandId());
        CosmeticCategory cosmeticCategory = cosmeticCategoryService.getCosmeticCategoryEntityById(request.getCategoryId());
        MemberCosmetic memberCosmetic = request.toEntity(member, cosmeticBrand, cosmeticCategory);

        dressingTable.addMemberCosmetic(memberCosmetic);
        memberCosmeticRepository.save(memberCosmetic);
        return memberCosmetic.getId();
    }

    @Override
    public MemberCosmeticDetailResponse getMemberCosmeticDetail(Long id) {
        MemberCosmetic memberCosmetic = getValidMemberCosmeticEntityById(id);
        return MemberCosmeticDetailResponse.from(memberCosmetic);
    }

    @Override
    @Transactional
    public void deleteMemberCosmetic(Long id) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        MemberCosmetic memberCosmetic = getValidMemberCosmeticEntityById(id);

        if(!memberCosmetic.getMember().getId().equals(memberId)) {
            throw new OurDressingTableException(ErrorCode.NO_PERMISSION_FOR_MEMBER_COSMETIC);
        }

        memberCosmetic.markAsDeleted();
    }

    @Override
    @Transactional
    public void updateMemberCosmetic(Long id, UpdateMemberCosmeticRequest request) {
        MemberCosmetic memberCosmetic = getValidMemberCosmeticEntityById(id);
        Long memberId = SecurityUtil.getCurrentMemberId();

        if(!memberCosmetic.getMember().getId().equals(memberId)) {
            throw new OurDressingTableException(ErrorCode.NO_PERMISSION_FOR_MEMBER_COSMETIC);
        }


        CosmeticBrand brand = null;
        if(request.getBrandId() != null) {
            brand = cosmeticBrandService.getCosmeticBrandEntityById(request.getBrandId());
        }

        CosmeticCategory category = null;
        if(request.getCategoryId() != null) {
            category = cosmeticCategoryService.getCosmeticCategoryEntityById(request.getCategoryId());

        }

        memberCosmetic.updateMemberCosmetic(request, brand, category);

    }

    @Override
    public MemberCosmetic getMemberCosmeticEntityById(Long id) {
        return memberCosmeticRepository.findById(id)
                .orElseThrow(() -> new OurDressingTableException(ErrorCode.MEMBER_COSMETIC_NOT_FOUND));
    }

    @Override
    public MemberCosmetic getValidMemberCosmeticEntityById(Long id) {
        return memberCosmeticRepository.findById(id).filter(memberCosmetic -> !memberCosmetic.isDeleted())
                .orElseThrow(() -> new OurDressingTableException(ErrorCode.MEMBER_COSMETIC_NOT_FOUND));
    }

}
