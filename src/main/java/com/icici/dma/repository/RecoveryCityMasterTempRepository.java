package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.model.RecoveryCityMasterTemp;

@Repository
public interface RecoveryCityMasterTempRepository
        extends JpaRepository<RecoveryCityMasterTemp, String> {

    Optional<RecoveryCityMasterTemp> findByCityAndStatus(String city, String status);

    boolean existsByCityAndStatus(String city,String status);
    
    List<RecoveryCityMasterTemp> findAllByStatusAndCreatedByNot(
            String status,
            String user);
    
    @Query("SELECT r.city FROM RecoveryCityMasterTemp r " +
           "WHERE r.city IN :cities AND r.status = :status")
    List<String> findPendingCities(@Param("cities") List<String> cities, @Param("status") String status);

    @Transactional
    @Modifying
    @Query("UPDATE RecoveryCityMasterTemp r " +
           "SET r.status = :status, " +
           "r.remarks = :remark " +
           "WHERE r.city IN :cities " +
           "AND r.status = :pendingStatus")
    int bulkUpdateStatus(
            @Param("cities") List<String> cities,
            @Param("status") String status,
            @Param("remark") String remark,
            @Param("pendingStatus") String pendingStatus);
    
    
    
    @Transactional
    @Modifying
    @Query("DELETE FROM RecoveryCityMasterTemp r WHERE r.city IN :cities")
    int deleteApprovedRecords(
            @Param("cities") List<String> cities);
    
    
    @Query(
    	    value =
    	        "SELECT t.* " +
    	        "FROM TM_CCR_RECOVERY_CITY_MST_TEMP t " +
    	        "WHERE t.status = :status " +
    	        "ORDER BY GREATEST( " +
    	        "NVL(t.action_date, DATE '1900-01-01'), " +
    	        "NVL(t.modified_date, DATE '1900-01-01'), " +
    	        "NVL(t.created_date, DATE '1900-01-01') " +
    	        ") DESC",
    	    nativeQuery = true
    	)
    	List<RecoveryCityMasterTemp> findAllByStatus(
    	        @Param("status") String status);
    
}




