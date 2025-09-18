package com.ourdressingtable.communitycategory.repository;

import com.ourdressingtable.communitycategory.domain.CommunityCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCategoryRepository extends JpaRepository<CommunityCategory, Long> {
}
