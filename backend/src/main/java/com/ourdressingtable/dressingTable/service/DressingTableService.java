package com.ourdressingtable.dressingTable.service;

import com.ourdressingtable.dressingTable.dto.CreateDressingTableRequest;

public interface DressingTableService {
    Long createDressingTable(CreateDressingTableRequest dressingTableRequest, Long memberId);
    void updateDressingTable(CreateDressingTableRequest dressingTableRequest, Long id, Long memberId);
}
