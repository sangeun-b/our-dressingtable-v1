package com.ourdressingtable.member.service;

import com.ourdressingtable.member.dto.CreateMemberRequest;
import com.ourdressingtable.member.dto.MemberResponse;
import com.ourdressingtable.member.dto.OtherMemberResponse;
import com.ourdressingtable.member.dto.UpdateMemberRequest;

public interface MemberService {
    Long createMember(CreateMemberRequest createMemberRequest);
    OtherMemberResponse getMember(Long id);
    void updateMember(Long id, UpdateMemberRequest updateMemberRequest);

}
