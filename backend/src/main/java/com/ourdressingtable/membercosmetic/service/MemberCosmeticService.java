package com.ourdressingtable.membercosmetic.service;

import com.ourdressingtable.membercosmetic.domain.MemberCosmetic;
import com.ourdressingtable.membercosmetic.dto.CreateMemberCosmeticRequest;
import com.ourdressingtable.membercosmetic.dto.MemberCosmeticDetailResponse;
import com.ourdressingtable.membercosmetic.dto.UpdateMemberCosmeticRequest;

public interface MemberCosmeticService {
    Long createMemberCosmetic(CreateMemberCosmeticRequest request);
    MemberCosmeticDetailResponse getMemberCosmeticDetail(Long id);
    void deleteMemberCosmetic(Long id);
    MemberCosmetic getMemberCosmeticEntityById(Long id);
    void updateMemberCosmetic(Long id, UpdateMemberCosmeticRequest request);
    MemberCosmetic getValidMemberCosmeticEntityById(Long id);
}
