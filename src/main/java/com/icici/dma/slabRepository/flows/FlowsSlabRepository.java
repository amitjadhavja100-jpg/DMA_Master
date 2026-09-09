package com.icici.dma.slabRepository.flows;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.flows.FlowsSlabMaster;

@Repository
public interface FlowsSlabRepository
        extends JpaRepository<FlowsSlabMaster, Long> {

    Optional<FlowsSlabMaster> findTopByParentIdOrderByVersionNoDesc(Long parentId);

    List<FlowsSlabMaster> findByStatus(String status);
    
	List<FlowsSlabMaster> findByStatusOrderByCreatedDateDesc(String status);
	
	List<FlowsSlabMaster> findByStatusAndCreatedByNotOrderByCreatedDateDesc(
	        String status,
	        String createdBy);
    
	@Query("FROM FlowsSlabMaster f "
		     + "WHERE f.product = :product "
		     + "AND f.subProduct = :subProduct "
		     + "AND f.categoryId = :categoryId "
		     + "AND (f.cityId = :cityId OR (:cityId IS NULL AND f.cityId IS NULL)) "
		     + "AND f.bucketId = :bucketId "
		     + "AND f.fromDate = :fromDate "
		     + "AND f.toDate = :toDate "
		     + "AND f.status = 'APPROVED' "
		     + "ORDER BY f.versionNo DESC")
	List<FlowsSlabMaster> findLatestApproved(
		        @Param("product") String product,
		        @Param("subProduct") String subProduct,
		        @Param("categoryId") Long categoryId,
		        @Param("cityId") Long cityId,
		        @Param("bucketId") Long bucketId,
		        @Param("fromDate") LocalDate fromDate,
		        @Param("toDate") LocalDate toDate);
	
	
	@Query("FROM FlowsSlabMaster f "
		     + "WHERE f.product = :product "
		     + "AND f.subProduct = :subProduct "
		     + "AND f.categoryId = :categoryId "
		     + "AND (f.cityId = :cityId OR (:cityId IS NULL AND f.cityId IS NULL)) "
		     + "AND f.bucketId = :bucketId "
		     + "AND f.fromDate = :fromDate "
		     + "AND f.toDate = :toDate "
		     + "AND f.status IN ('APPROVED', 'INACTIVE') "
		     + "AND f.versionNo < :versionNo "
		     + "ORDER BY f.versionNo DESC")
	List<FlowsSlabMaster> findPreviousApproved(
		        @Param("product") String product,
		        @Param("subProduct") String subProduct,
		        @Param("categoryId") Long categoryId,
		        @Param("cityId") Long cityId,
		        @Param("bucketId") Long bucketId,
		        @Param("fromDate") LocalDate fromDate,
		        @Param("toDate") LocalDate toDate,
		        @Param("versionNo") Integer versionNo);
    
    
	@Query("FROM FlowsSlabMaster f "
		     + "WHERE f.product = :product "
		     + "AND f.subProduct = :subProduct "
		     + "AND f.categoryId = :categoryId "
		     + "AND f.cityId = :cityId "
		     + "AND f.bucketId = :bucketId "
		     + "AND f.fromDate = :fromDate "
		     + "AND f.toDate = :toDate "
		     + "AND f.status = 'PENDING'")
	List<FlowsSlabMaster> findPending(
		        @Param("product") String product,
		        @Param("subProduct") String subProduct,
		        @Param("categoryId") Long categoryId,
		        @Param("cityId") Long cityId,
		        @Param("bucketId") Long bucketId,
		        @Param("fromDate") LocalDate fromDate,
		        @Param("toDate") LocalDate toDate);

}