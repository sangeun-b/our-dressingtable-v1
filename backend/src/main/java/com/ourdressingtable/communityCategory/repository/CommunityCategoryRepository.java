package com.ourdressingtable.communityCategory.repository;

import com.ourdressingtable.communityCategory.domain.CommunityCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCategoryRepository extends JpaRepository<CommunityCategory, Long> {
}
