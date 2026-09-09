package com.icici.dma.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.AlddTransaction;

@Repository
public interface AlddDumpRepository extends JpaRepository<AlddTransaction, String> {

	boolean existsByapplicationNo(String applicationNo);

	@Query("SELECT COUNT(a) FROM AlddTransaction a WHERE a.fromCycleDate >= :fromDate AND a.toCycleDate <= :toDate")
	long AlddcountByDateRange(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}