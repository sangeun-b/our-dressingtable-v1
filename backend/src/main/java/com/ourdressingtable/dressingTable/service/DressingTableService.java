package com.ourdressingtable.dressingTable.service;

import com.ourdressingtable.dressingTable.dto.CreateDressingTableRequest;
import com.ourdressingtable.dressingTable.dto.UpdateDressingTableRequest;

public interface DressingTableService {
    Long createDressingTable(CreateDressingTableRequest dressingTableRequest);
    void updateDressingTable(UpdateDressingTableRequest dressingTableRequest, Long id);
    void deleteDressingTable(Long id);
}
