package com.ourdressingtable.communityCategory.service;

import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import com.ourdressingtable.communityCategory.dto.CommunityCategoryResponse;

import java.util.List;

public interface CommunityCategoryService {
    List<CommunityCategoryResponse> getAllCategories();
    CommunityCategoryResponse getCategoryById(Long id);
    CommunityCategory getCategoryEntityById(Long id);
}
