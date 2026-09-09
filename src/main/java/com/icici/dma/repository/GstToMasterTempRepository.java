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

import com.icici.dma.model.GstToMasterTemp;

@Repository
public interface GstToMasterTempRepository extends JpaRepository<GstToMasterTemp, Long> {

	boolean existsByProcessShopAndStatus(String processShop, String status);

	Optional<GstToMasterTemp> findByProcessShopAndStatus(String processShop, String status);

	@Query(value = "SELECT t.* FROM TM_VHL_GST_TO_MST_TEMP t WHERE t.STATUS = :status "
			+ "ORDER BY GREATEST( NVL(t.MODIFIED_DATE, DATE '1900-01-01'), "
			+ "NVL(t.ACTION_DATE, DATE '1900-01-01'), NVL(t.CREATED_DATE, DATE '1900-01-01') "
			+ ") DESC", nativeQuery = true)
	List<GstToMasterTemp> findAllByStatus(@Param("status") String status);

	@Query(value = "SELECT t.* FROM TM_VHL_GST_TO_MST_TEMP t WHERE t.STATUS = :status "
			+ "AND t.ACTION_TYPE IN ('I', 'U') AND t.ACTION_USER IS NOT NULL "
			+ "AND UPPER(TRIM(t.ACTION_USER)) != UPPER(TRIM(:actionUser)) ORDER BY GREATEST( "
			+ "NVL(t.ACTION_DATE, DATE '1900-01-01'), NVL(t.MODIFIED_DATE, DATE '1900-01-01'), "
			+ "NVL(t.CREATED_DATE, DATE '1900-01-01') ) DESC", nativeQuery = true)
	List<GstToMasterTemp> findAllByStatusAndActionUserNot(@Param("status") String status,
			@Param("actionUser") String actionUser);

	@Query(value = "SELECT g.PROCESS_SHOP FROM TM_VHL_GST_TO_MST_TEMP g WHERE g.PROCESS_SHOP IN (:ids) AND g.STATUS = :status", nativeQuery = true)
	List<String> findAllProcessShopByProcessShopInANDStatus(@Param("ids") List<String> batch, @Param("status") String status);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional
	@Query(value = "UPDATE TM_VHL_GST_TO_MST_TEMP g SET g.STATUS = :status, g.REMARKS = :remark "
			+ "WHERE g.PROCESS_SHOP IN (:ids) AND g.STATUS = :pendingStatus", nativeQuery = true)
	int updateStatusAndRemarksByProcessShopInANDStatus(@Param("ids") List<String> ids, @Param("status") String status,
			@Param("remark") String remark, @Param("pendingStatus") String pendingStatus);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM TM_VHL_GST_TO_MST_TEMP WHERE STATUS = :status AND PROCESS_SHOP IN (:processShop)", nativeQuery = true)
	int deletePendingByProcessShop(@Param("status") String status, @Param("processShop") Set<String> processShops);
	
	boolean existsByStatus(String status);

}
