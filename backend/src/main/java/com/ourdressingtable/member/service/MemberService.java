package com.ourdressingtable.member.service;

import com.ourdressingtable.member.dto.CreateMemberRequest;

public interface MemberService {
    Long createMember(CreateMemberRequest createMemberRequest);

}
