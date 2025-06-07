package com.ourdressingtable.dressingTable.repository;

import com.ourdressingtable.dressingTable.domain.DressingTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DressingTableRepository extends JpaRepository<DressingTable, Long> {
}
