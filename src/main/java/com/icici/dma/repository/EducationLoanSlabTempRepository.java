package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icici.dma.slabEntity.EducationLoanSlabTemp;

public interface EducationLoanSlabTempRepository extends JpaRepository<EducationLoanSlabTemp, Long> {
	List<EducationLoanSlabTemp> findByRecordStatusOrderByMakerDateDesc(String recordStatus);

	List<EducationLoanSlabTemp> findByTempIdIn(List<Long> tempIds);

}
