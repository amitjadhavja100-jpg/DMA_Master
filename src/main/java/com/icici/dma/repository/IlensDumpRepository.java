package com.icici.dma.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.IlensDump;

@Repository
public interface IlensDumpRepository extends JpaRepository<IlensDump, String> {

	boolean existsByApplicationNumber(String applicationNumber);

	@Query("SELECT COUNT(a) FROM IlensDump a WHERE a.cycleFromDate >= :fromDate AND a.cycleToDate <= :toDate")
	long ilnescountByDateRange(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}