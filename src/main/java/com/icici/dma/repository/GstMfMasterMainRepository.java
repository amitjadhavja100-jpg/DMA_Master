package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.GstMfMaster;
import com.icici.dma.model.OutsourceMaster;

@Repository
public interface GstMfMasterMainRepository extends JpaRepository<GstMfMaster, Long> {

	@Query(value = "SELECT m.* FROM TM_VHL_GST_MF_MST m WHERE m.STATUS = :status ORDER BY GREATEST( "
			+ "NVL(m.MODIFIED_DATE, DATE '1900-01-01'), NVL(m.CREATED_DATE, DATE '1900-01-01') "
			+ ") DESC", nativeQuery = true)
	List<GstMfMaster> findAllByStatus(@Param("status") String status);

	boolean existsByApsCode(Long apsCode);

	Optional<GstMfMaster> findByApsCode(Long apsCode);
	
	boolean existsBy();

}
