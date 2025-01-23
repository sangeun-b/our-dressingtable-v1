package com.ourdressingtable.domain.member.service;

import com.ourdressingtable.domain.member.dto.CreateMemberRequest;
import com.ourdressingtable.domain.member.dto.CreateMemberResponse;

public interface MemberService {
    Long createMember(CreateMemberRequest createMemberRequest);

}
