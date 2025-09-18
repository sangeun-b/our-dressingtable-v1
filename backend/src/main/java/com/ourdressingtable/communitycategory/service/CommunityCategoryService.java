package com.ourdressingtable.communitycategory.service;

import com.ourdressingtable.communitycategory.domain.CommunityCategory;
import com.ourdressingtable.communitycategory.dto.CommunityCategoryResponse;

import java.util.List;

public interface CommunityCategoryService {
    List<CommunityCategoryResponse> getAllCategories();
    CommunityCategoryResponse getCategoryById(Long id);
    CommunityCategory getCategoryEntityById(Long id);
}
