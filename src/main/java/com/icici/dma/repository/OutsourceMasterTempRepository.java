package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.model.OutsourceMasterTemp;

@Repository
public interface OutsourceMasterTempRepository extends JpaRepository<OutsourceMasterTemp, Integer> {

	boolean existsByEmpCodeAndStatus(String EmpCode, String status);

	Optional<OutsourceMasterTemp> findByEmpCodeAndStatus(String EmpCode, String status);

	@Query(value = "SELECT t.* FROM TM_VHL_OUTSOURCE_MST_TEMP t WHERE t.STATUS = :status "
			+ "ORDER BY GREATEST( NVL(t.MODIFIED_DATE, DATE '1900-01-01'), "
			+ "NVL(t.ACTION_DATE, DATE '1900-01-01'), NVL(t.CREATED_DATE, DATE '1900-01-01') "
			+ ") DESC", nativeQuery = true)
	List<OutsourceMasterTemp> findAllByStatus(@Param("status") String status);

	@Query(value = "SELECT t.* FROM TM_VHL_OUTSOURCE_MST_TEMP t WHERE t.STATUS = :status "
			+ "AND t.ACTION_TYPE IN ('I', 'U') AND t.ACTION_USER IS NOT NULL "
			+ "AND UPPER(TRIM(t.ACTION_USER)) != UPPER(TRIM(:actionUser)) ORDER BY GREATEST( "
			+ "NVL(t.ACTION_DATE, DATE '1900-01-01'), NVL(t.MODIFIED_DATE, DATE '1900-01-01'), "
			+ "NVL(t.CREATED_DATE, DATE '1900-01-01') ) DESC", nativeQuery = true)
	List<OutsourceMasterTemp> findAllByStatusAndActionUserNot(@Param("status") String status,
			@Param("actionUser") String actionUser);

	@Query(value = "SELECT g.EMP_CODE FROM TM_VHL_OUTSOURCE_MST_TEMP g WHERE g.EMP_CODE IN (:ids) AND g.STATUS = :status", nativeQuery = true)
	List<String> findAllEmpCodesByEmpCodeInANDStatus(@Param("ids") List<String> batch, @Param("status") String status);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional

	@Query(value = "UPDATE TM_VHL_OUTSOURCE_MST_TEMP g SET g.STATUS = :status, g.REMARKS = :remark "
			+ "WHERE g.EMP_CODE IN (:ids) AND g.STATUS = :pendingStatus", nativeQuery = true)
	int updateStatusAndRemarksByEmpCodeInANDStatus(@Param("ids") List<String> ids, @Param("status") String status,
			@Param("remark") String remark, @Param("pendingStatus") String pendingStatus);
	
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM TM_VHL_OUTSOURCE_MST_TEMP WHERE STATUS = :status AND EMP_CODE IN (:empCode)", nativeQuery = true)
	int deletePendingByPartnerIds(@Param("status") String status, @Param("empCode") Set<String> empCodes);
	
	boolean existsByStatus(String status);
	
}
