package com.ourdressingtable.dressingTable.service;

import com.ourdressingtable.dressingTable.dto.DressingTableRequest;

public interface DressingTableService {
    Long createDressingTable(DressingTableRequest dressingTableRequest, Long memberId);
}
