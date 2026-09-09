package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.icici.dma.model.GstMfMasterTemp;
import com.icici.dma.model.OutsourceMasterTemp;

public interface GstMfMasterTempRepository extends JpaRepository<GstMfMasterTemp, Long> {

	boolean existsByApsCodeAndStatus(Long apsCode, String status);

	Optional<GstMfMasterTemp> findByApsCodeAndStatus(Long apsCode, String status);

	@Query(value = "SELECT t.* FROM TM_VHL_GST_MF_MST_TEMP t WHERE t.STATUS = :status "
			+ "ORDER BY GREATEST( NVL(t.MODIFIED_DATE, DATE '1900-01-01'), "
			+ "NVL(t.ACTION_DATE, DATE '1900-01-01'), NVL(t.CREATED_DATE, DATE '1900-01-01') "
			+ ") DESC", nativeQuery = true)
	List<GstMfMasterTemp> findAllByStatus(@Param("status") String status);

	@Query(value = "SELECT t.* FROM TM_VHL_GST_MF_MST_TEMP t WHERE t.STATUS = :status "
			+ "AND t.ACTION_TYPE IN ('I', 'U') AND t.ACTION_USER IS NOT NULL "
			+ "AND UPPER(TRIM(t.ACTION_USER)) != UPPER(TRIM(:actionUser)) ORDER BY GREATEST( "
			+ "NVL(t.ACTION_DATE, DATE '1900-01-01'), NVL(t.MODIFIED_DATE, DATE '1900-01-01'), "
			+ "NVL(t.CREATED_DATE, DATE '1900-01-01') ) DESC", nativeQuery = true)
	List<GstMfMasterTemp> findAllByStatusAndActionUserNot(@Param("status") String status,
			@Param("actionUser") String actionUser);

	@Query(value = "SELECT g.APSCODE FROM TM_VHL_GST_MF_MST_TEMP g WHERE g.APSCODE IN (:ids) AND g.STATUS = :status", nativeQuery = true)
	List<Long> findAllApsCodesByApsCodeInANDStatus(@Param("ids") List<Long> batch, @Param("status") String status);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional
	@Query(value = "UPDATE TM_VHL_GST_MF_MST_TEMP g SET g.STATUS = :status, g.REMARKS = :remark "
			+ "WHERE g.APSCODE IN (:ids) AND g.STATUS = :pendingStatus", nativeQuery = true)
	int updateStatusAndRemarksByEmpCodeInANDStatus(@Param("ids") List<Long> ids, @Param("status") String status,
			@Param("remark") String remark, @Param("pendingStatus") String pendingStatus);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM TM_VHL_GST_MF_MST_TEMP WHERE STATUS = :status AND APSCODE IN (:apsCodes)", nativeQuery = true)
	int deletePendingByApsCodes(@Param("status") String status, @Param("apsCodes") Set<Long> apsCodes);
	
	boolean existsByStatus(String status);

}
