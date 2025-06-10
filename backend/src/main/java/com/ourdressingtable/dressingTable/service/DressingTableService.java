package com.ourdressingtable.dressingTable.service;

import com.ourdressingtable.dressingTable.dto.CreateDressingTableRequest;
import com.ourdressingtable.dressingTable.dto.UpdateDressingTableRequest;

public interface DressingTableService {
    Long createDressingTable(CreateDressingTableRequest dressingTableRequest, Long memberId);
    void updateDressingTable(UpdateDressingTableRequest dressingTableRequest, Long id, Long memberId);
    void deleteDressingTable(Long id, Long memberId);
}
