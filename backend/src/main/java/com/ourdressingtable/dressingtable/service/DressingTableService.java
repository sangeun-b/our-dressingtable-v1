package com.ourdressingtable.dressingtable.service;

import com.ourdressingtable.dressingtable.domain.DressingTable;
import com.ourdressingtable.dressingtable.dto.CreateDressingTableRequest;
import com.ourdressingtable.dressingtable.dto.DressingTableDetailResponse;
import com.ourdressingtable.dressingtable.dto.DressingTableResponse;
import com.ourdressingtable.dressingtable.dto.UpdateDressingTableRequest;
import java.util.List;

public interface DressingTableService {
    Long createDressingTable(CreateDressingTableRequest dressingTableRequest);
    void updateDressingTable(UpdateDressingTableRequest dressingTableRequest, Long id);
    void deleteDressingTable(Long id);
    List<DressingTableResponse> getAllMyDressingTables();
    DressingTableDetailResponse getDressingTableDetail(Long id);
    DressingTable getDressingTableEntityById(Long id);
}
