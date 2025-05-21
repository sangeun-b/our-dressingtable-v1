package com.ourdressingtable.community.service;

import com.ourdressingtable.community.domain.CommunityCategory;
import com.ourdressingtable.community.dto.CommunityCategoryResponse;

import java.util.List;

public interface CommunityCategoryService {
    List<CommunityCategoryResponse> getAllCategories();
    CommunityCategoryResponse getCategoryById(Long id);
    CommunityCategory getCategoryEntityById(Long id);
}
