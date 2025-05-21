package com.ourdressingtable.community.service.impl;

import com.ourdressingtable.community.domain.CommunityCategory;
import com.ourdressingtable.community.dto.CommunityCategoryResponse;
import com.ourdressingtable.community.repository.CommunityCategoryRepository;
import com.ourdressingtable.community.service.CommunityCategoryService;
import com.ourdressingtable.exception.ErrorCode;
import com.ourdressingtable.exception.GlobalExceptionHandler;
import com.ourdressingtable.exception.OurDressingTableException;
import com.ourdressingtable.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityCategoryServiceImpl implements CommunityCategoryService {
    private final CommunityCategoryRepository communityCategoryRepository;

    @Override
    public List<CommunityCategoryResponse> getAllCategories() {
        return communityCategoryRepository.findAll().stream()
                .map(CommunityCategoryResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public CommunityCategoryResponse getCategoryById(Long id) {
        CommunityCategory communityCategory = communityCategoryRepository.findById(id).orElseThrow(() -> new OurDressingTableException(ErrorCode.COMMUNITY_CATEGORY_NOT_FOUND));
        return CommunityCategoryResponse.from(communityCategory);
    }

    @Override
    public CommunityCategory getCategoryEntityById(Long id) {
        return communityCategoryRepository.findById(id).orElseThrow(() -> new OurDressingTableException(ErrorCode.COMMUNITY_CATEGORY_NOT_FOUND));
    }
}
