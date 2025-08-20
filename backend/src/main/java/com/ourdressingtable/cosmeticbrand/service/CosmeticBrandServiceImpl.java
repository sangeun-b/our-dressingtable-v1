package com.ourdressingtable.cosmeticbrand.service;

import com.ourdressingtable.common.exception.ErrorCode;
import com.ourdressingtable.common.exception.OurDressingTableException;
import com.ourdressingtable.cosmeticbrand.domain.CosmeticBrand;
import com.ourdressingtable.cosmeticbrand.repository.CosmeticBrandRepository;
import com.ourdressingtable.dressingtable.service.DressingTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CosmeticBrandServiceImpl implements CosmeticBrandService {

    private final CosmeticBrandRepository cosmeticBrandRepository;
    @Override
    public CosmeticBrand getCosmeticBrandEntityById(Long id) {
        return cosmeticBrandRepository.findById(id).orElseThrow(() ->
                new OurDressingTableException(ErrorCode.COSMETIC_BRAND_NOT_FOUND));
    }
}
