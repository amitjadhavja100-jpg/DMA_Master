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

import com.icici.dma.model.ILensChannelMasterTemp;

@Repository
public interface ILensChannelMasterTempRepository extends JpaRepository<ILensChannelMasterTemp, Integer> {

	boolean existsByUserIDAndStatusA(String userID, String statusA);

	Optional<ILensChannelMasterTemp> findByUserIDAndStatusA(String userID, String statusA);

	@Query(value = "SELECT t.* FROM TM_VHL_ILENS_CHANNEL_MST_TEMP t WHERE t.STATUS_A = :status "
			+ "ORDER BY GREATEST( NVL(t.MODIFIED_DATE, DATE '1900-01-01'), "
			+ "NVL(t.ACTION_DATE, DATE '1900-01-01'), NVL(t.CREATED_DATE, DATE '1900-01-01') "
			+ ") DESC", nativeQuery = true)
	List<ILensChannelMasterTemp> findAllByStatusA(@Param("status") String statusA);

	@Query(value = "SELECT t.* FROM TM_VHL_ILENS_CHANNEL_MST_TEMP t WHERE t.STATUS_A = :status "
			+ "AND t.ACTION_TYPE IN ('I', 'U') AND t.ACTION_USER IS NOT NULL "
			+ "AND UPPER(TRIM(t.ACTION_USER)) != UPPER(TRIM(:actionUser)) ORDER BY GREATEST( "
			+ "NVL(t.ACTION_DATE, DATE '1900-01-01'), NVL(t.MODIFIED_DATE, DATE '1900-01-01'), "
			+ "NVL(t.CREATED_DATE, DATE '1900-01-01') ) DESC", nativeQuery = true)
	List<ILensChannelMasterTemp> findAllByStatusAndActionUserNot(@Param("status") String statusA,
			@Param("actionUser") String actionUser);

	@Query(value = "SELECT g.USER_ID FROM TM_VHL_ILENS_CHANNEL_MST_TEMP g WHERE g.USER_ID IN (:ids) AND g.STATUS_A = :status", nativeQuery = true)
	List<String> findAllUserIDsByUserIDInANDStatusA(@Param("ids") List<String> batch, @Param("status") String statusA);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional

	@Query(value = "UPDATE TM_VHL_ILENS_CHANNEL_MST_TEMP g SET g.STATUS_A = :status, g.REMARKS = :remark "
			+ "WHERE g.USER_ID IN (:ids) AND g.STATUS_A = :pendingStatus", nativeQuery = true)
	int updateStatusAndRemarksByUserIDInANDStatusA(@Param("ids") List<String> ids, @Param("status") String statusA,
			@Param("remark") String remark, @Param("pendingStatus") String pendingStatus);

	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM TM_VHL_ILENS_CHANNEL_MST_TEMP WHERE STATUS_A = :status AND USER_ID IN (:userId)", nativeQuery = true)
	int deletePendingByPartnerIds(@Param("status") String status, @Param("userId") Set<String> userIds);
	
	boolean existsByStatusA(String statusA);
}
