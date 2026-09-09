package com.icici.dma.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.icici.dma.model.RCA_CIBIL;

@Repository
public interface RcaCibilRepository extends JpaRepository<RCA_CIBIL, String> {

	boolean existsByLanNo(String lanNo);

	@Query("SELECT COUNT(r) FROM RCA_CIBIL r WHERE r.fromCycleDate >= :fromDate AND r.toCycleDate <= :toDate")
	long rcacibilcountByDateRange(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}