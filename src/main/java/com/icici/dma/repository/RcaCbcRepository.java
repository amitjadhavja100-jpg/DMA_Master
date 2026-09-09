package com.icici.dma.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.RCA_CBC;

@Repository
public interface RcaCbcRepository extends JpaRepository<RCA_CBC, String> {
	@Query("SELECT COUNT(r) FROM RCA_CBC r WHERE r.fromCycleDate >= :fromDate AND r.toCycleDate <= :toDate")
    long rcacbccountByDateRange(@Param("fromDate") Date fromDate,
                          @Param("toDate") Date toDate);

}
