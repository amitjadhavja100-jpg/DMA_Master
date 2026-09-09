package com.icici.dma.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.FinnoneDump;
 
@Repository
public interface FinnoneDumpRepository
        extends JpaRepository<FinnoneDump, Long> {
	
	 long countByStatusAndCycleFromDateBetween(String status, Date cycleFromDate, Date cycleToDate);
}
 