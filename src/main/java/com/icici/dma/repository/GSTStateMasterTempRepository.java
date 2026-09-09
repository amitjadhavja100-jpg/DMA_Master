package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.GSTStateMasterTemp;

@Repository
public interface GSTStateMasterTempRepository extends JpaRepository<GSTStateMasterTemp, Long> {

	boolean existsByPartnerIdAndStatus(Long partnerId, String status);

	Optional<GSTStateMasterTemp> findByPartnerIdAndStatus(Long partnerId, String status);

	@Query(value = "SELECT t.* FROM TM_VHL_GST_STATE_MST_TEMP t WHERE t.STATUS = :status "
			+ "ORDER BY GREATEST( NVL(t.MODIFIED_DATE, DATE '1900-01-01'), "
			+ "NVL(t.ACTION_DATE, DATE '1900-01-01'), NVL(t.CREATED_DATE, DATE '1900-01-01') "
			+ ") DESC", nativeQuery = true)
	List<GSTStateMasterTemp> findAllByStatus(@Param("status") String status);

	@Query(value = "SELECT t.* FROM TM_VHL_GST_STATE_MST_TEMP t WHERE t.STATUS = :status "
			+ "AND t.ACTION_TYPE IN ('I', 'U') AND t.ACTION_USER IS NOT NULL "
			+ "AND UPPER(TRIM(t.ACTION_USER)) != UPPER(TRIM(:actionUser)) ORDER BY GREATEST( "
			+ "NVL(t.ACTION_DATE, DATE '1900-01-01'), NVL(t.MODIFIED_DATE, DATE '1900-01-01'), "
			+ "NVL(t.CREATED_DATE, DATE '1900-01-01') ) DESC", nativeQuery = true)
	List<GSTStateMasterTemp> findAllByStatusAndActionUserNot(@Param("status") String status,
			@Param("actionUser") String actionUser);

	@Query(value = "SELECT g.PARTNER_ID FROM TM_VHL_GST_STATE_MST_TEMP g WHERE g.PARTNER_ID IN (:ids) AND g.STATUS = :status", nativeQuery = true)
	List<Long> findAllPartnerIdByPartnerIdInANDStatus(@Param("ids") List<Long> batch, @Param("status") String status);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional
	@Query(value = "UPDATE TM_VHL_GST_STATE_MST_TEMP g SET g.STATUS = :status, g.REMARKS = :remark "
			+ "WHERE g.PARTNER_ID IN (:ids) AND g.STATUS = :pendingStatus", nativeQuery = true)
	int updateStatusAndRemarksByPartnerIdInANDStatus(@Param("ids") List<Long> ids, @Param("status") String status,
			@Param("remark") String remark, @Param("pendingStatus") String pendingStatus);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM TM_VHL_GST_STATE_MST_TEMP WHERE STATUS = :status AND PARTNER_ID IN (:partnerId)", nativeQuery = true)
	int deletePendingByPartnerIds(@Param("status") String status, @Param("partnerId") Set<Long> apsCodes);
	
	
	boolean existsByStatus(String status);
	
//	@Modifying
//	@Transactional
//	@Query(value = "DELETE FROM TM_VHL_GST_STATE_MST_TEMP WHERE STATUS = :status AND PARTNER_ID IN (:partnerIds)",
//	       nativeQuery = true)
//	int deletePendingByPartnerIds(@Param("status") String status,
//	                              @Param("partnerIds") Set<Long> partnerIds);
//
//	// Find all records with a given status (for maker/checker listing)
//	List<GSTStateMasterTemp> findByStatus(String status);
//
//	// Checker cannot see records created by himself (4-eyes principle)
//	List<GSTStateMasterTemp> findByStatusAndCreatedByNot(String status, String createdBy);
//
//	// Find a specific pending record by partnerId
//	GSTStateMasterTemp findByPartnerIdAndStatus(Long partnerId, String status);
}
