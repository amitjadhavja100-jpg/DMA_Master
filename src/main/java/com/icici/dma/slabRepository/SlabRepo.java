package com.icici.dma.slabRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.CollectionSlabMaster;
@Repository
public interface SlabRepo extends JpaRepository<CollectionSlabMaster, Long> {

	 List<CollectionSlabMaster> findByCategoryAndDpd(String category, String dpd);

//	    @Query(value =
//	            "SELECT SLAB_VALUE " +
//	            "FROM TS_CCR_SLAB_COLL_MST " +
//	            "WHERE CATEGORY = :category " +
//	            "AND DPD = :dpd " +
//	            "ORDER BY ORDER_NO",
//	            nativeQuery = true)
//	    List<String> getCollectionSlabs(
//	            @Param("category") String category,
//	            @Param("dpd") String dpd);

	 @Query(value =
			    "SELECT SLAB_VALUE " +
			    "FROM TS_CCR_SLAB_COLL_MST " +
			    "WHERE CATEGORY = :category " +
			    "AND DPD = :dpd " +
			    "AND STATUS = 'Y' " +
			    "ORDER BY ORDER_NO",
			    nativeQuery = true)
			List<String> getCollectionSlabs(
			        @Param("category") String category,
			        @Param("dpd") String dpd);

@Query(value =
        "SELECT DPD " +
        "FROM ( " +
        "   SELECT DPD, MIN(ORDER_NO) ORD " +
        "   FROM TS_CCR_SLAB_COLL_MST " +
        "   WHERE CATEGORY = :category " +
        "   GROUP BY DPD " +
        ") " +
        "ORDER BY ORD",
        nativeQuery = true)
List<String> findDistinctDpds(
        @Param("category") String category);

/*
 * @Query(value= "SELECT DISTINCT SLAB_VALUE " + "FROM TS_CCR_SLAB_COLL_MST " +
 * "WHERE CATEGORY=:category " + "AND DPD=:dpd " + "AND STATUS='Y' " +
 * "ORDER BY ORDER_NO", nativeQuery=true) List<String> findCollections( String
 * category, String dpd);
 */

// new req
/*
 * Optional<CollectionSlabMaster> findByCategoryAndDpdAndSlabValue( String
 * category, String dpd, String slabValue);
 */

Optional<CollectionSlabMaster>
findByCategoryAndDpdAndOrderNo(
        String category,
        String dpd,
        Integer orderNo);

List<CollectionSlabMaster> findByCategoryAndDpdOrderByOrderNo(
        String category,
        String dpd);
	
Optional<CollectionSlabMaster> findByCategoryAndDpdAndSlabValue(
        String category,
        String dpd,
        String slabValue);


List<CollectionSlabMaster> findByStatusOrderByCategoryAscDpdAscOrderNoAsc(
        String status);

List<CollectionSlabMaster> findByCategoryAndStatusOrderByDpdAscOrderNoAsc(
        String category,
        String status);

List<CollectionSlabMaster> findByCategoryAndDpdAndStatusOrderByOrderNoAsc(
        String category,
        String dpd,
        String status);

Optional<CollectionSlabMaster> findFirstByCategoryAndDpd(
        String category,
        String dpd);

List<CollectionSlabMaster> findAllByOrderByCategoryAscDpdAscOrderNoAsc();

Optional<CollectionSlabMaster>
findByCategoryAndDpdAndSlabValueIsNull(
        String category,
        String dpd);

Optional<CollectionSlabMaster> findFirstByCategory(String category);

List<CollectionSlabMaster> findByCategory(String category);

}