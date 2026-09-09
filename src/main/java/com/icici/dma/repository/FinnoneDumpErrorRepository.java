package com.icici.dma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.FinnoneDumpError;
@Repository
public interface FinnoneDumpErrorRepository extends JpaRepository<FinnoneDumpError, Integer> {
	
	@Query(value = " SELECT upload_id FROM tm_vhl_finnone_dump_error WHERE created_by = :createdBy ORDER BY created_date DESC FETCH FIRST 1 ROW ONLY", nativeQuery = true)
	String findTopUploadIdByCreatedByOrderByCreatedDateDesc(@Param("createdBy")String createdBy);

}
