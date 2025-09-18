package com.ourdressingtable.communitycategory.service;

import com.ourdressingtable.communitycategory.domain.CommunityCategory;
import com.ourdressingtable.communitycategory.dto.CommunityCategoryResponse;
import com.ourdressingtable.communitycategory.repository.CommunityCategoryRepository;
import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
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
