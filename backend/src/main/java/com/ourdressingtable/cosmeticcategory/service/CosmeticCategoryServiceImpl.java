package com.ourdressingtable.cosmeticcategory.service;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.cosmeticcategory.domain.CosmeticCategory;
import com.ourdressingtable.cosmeticcategory.repository.CosmeticCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CosmeticCategoryServiceImpl implements CosmeticCategoryService {

    private final CosmeticCategoryRepository cosmeticCategoryRepository;

    @Override
    public CosmeticCategory getCosmeticCategoryEntityById(Long id) {
        return cosmeticCategoryRepository.findById(id).orElseThrow(() ->
                new OurDressingTableException(ErrorCode.COSMETIC_CATEGORY_NOT_FOUND));
    }
}
