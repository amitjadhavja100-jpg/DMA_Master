package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.GstToMaster;

@Repository
public interface GstToMasterMainRepository extends JpaRepository<GstToMaster, String> {

	@Query(value = "SELECT m.* FROM TM_VHL_GST_TO_MST m WHERE m.STATUS = :status ORDER BY GREATEST( "
			+ "NVL(m.MODIFIED_DATE, DATE '1900-01-01'), NVL(m.CREATED_DATE, DATE '1900-01-01') "
			+ ") DESC", nativeQuery = true)
	List<GstToMaster> findAllByStatus(@Param("status") String status);

	boolean existsByProcessShop(String processShop);

	Optional<GstToMaster> findByProcessShop(String processShop);
	
	boolean existsBy();

}
