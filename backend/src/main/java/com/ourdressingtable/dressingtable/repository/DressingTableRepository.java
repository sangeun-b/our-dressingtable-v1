package com.ourdressingtable.dressingtable.repository;

import com.ourdressingtable.dressingtable.domain.DressingTable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DressingTableRepository extends JpaRepository<DressingTable, Long> {

    List<DressingTable> findAllByMemberId(Long memberId);
}
