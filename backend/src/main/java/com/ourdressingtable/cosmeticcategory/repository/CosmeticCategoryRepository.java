package com.ourdressingtable.cosmeticcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ourdressingtable.cosmeticcategory.domain.CosmeticCategory;

public interface CosmeticCategoryRepository extends JpaRepository<CosmeticCategory, Long> {

}
