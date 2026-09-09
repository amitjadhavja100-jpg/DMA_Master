package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.ChannelMasterTemp;
@Repository
public interface ChannnelMasterTempRepository extends JpaRepository<ChannelMasterTemp, Integer> {
//	public interface ChannnelMasterTempRepository {
	boolean existsByApsCodeAndStatus(Integer apsCode, String status);

	List<ChannelMasterTemp> findByCreatedByAndStatus(String createdBy, String status);

	@Query(value = "SELECT t.* " +
	        "FROM TM_VHL_CHANNEL_MST_TEMP t " +
	        "WHERE t.status = :status " +
	        "ORDER BY GREATEST( " +
	        "NVL(t.action_date, DATE '1900-01-01'), " +
	        "NVL(t.modified_date, DATE '1900-01-01'), " +
	        "NVL(t.created_date, DATE '1900-01-01') " +
	        ") DESC",
	        nativeQuery = true)
	List<ChannelMasterTemp> findAllByStatus(@Param("status") String status);

	Optional<ChannelMasterTemp> findByApsCodeAndStatus(Integer apsCode, String status);
	
	@Query(value = "SELECT t.* " +
	        "FROM TM_VHL_CHANNEL_MST_TEMP t " +
	        "WHERE t.STATUS = :status " +
	    	"AND t.ACTION_TYPE IN ('I', 'U') " +
	        "AND t.ACTION_USER IS NOT NULL " +
	    	"AND UPPER(TRIM(t.ACTION_USER)) != UPPER(TRIM(:actionUser)) " +
	        "ORDER BY GREATEST( " +
	        "NVL(t.ACTION_DATE, DATE '1900-01-01'), " +
	        "NVL(t.MODIFIED_DATE, DATE '1900-01-01'), " +
	        "NVL(t.CREATED_DATE, DATE '1900-01-01') " +
	        ") DESC",
	        nativeQuery = true)
	List<ChannelMasterTemp> findAllByStatusAndCreatedByNot(@Param("status") String status, @Param("actionUser") String user);
	
	@Query("SELECT b.apsCode FROM ChannelMasterTemp b WHERE b.apsCode IN :codes")
	List<Integer> findExistingApsCode(@Param("codes") List<Integer> codes);
	
	boolean existsByStatus(String status);
}
