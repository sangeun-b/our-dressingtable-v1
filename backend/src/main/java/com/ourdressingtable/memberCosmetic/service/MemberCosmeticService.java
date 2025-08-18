package com.ourdressingtable.membercosmetic.service;

import com.ourdressingtable.membercosmetic.dto.CreateMemberCosmeticRequest;
import com.ourdressingtable.membercosmetic.dto.MemberCosmeticResponse;

public interface MemberCosmeticService {
    Long createMemberCosmetic(CreateMemberCosmeticRequest request);
    MemberCosmeticResponse getMemberCosmeticDetail(Long id);
}
