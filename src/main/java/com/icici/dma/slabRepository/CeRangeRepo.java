package com.icici.dma.slabRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.CeRangeMaster;
@Repository
public interface CeRangeRepo extends JpaRepository<CeRangeMaster, Long> {
    
	 List<CeRangeMaster> findByCategoryAndDpd( String category, String dpd);

//	 @Query(value =
//		        "SELECT RANGE_VALUE " +
//		        "FROM TS_CCR_SLAB_RANGE_MST " +
//		        "WHERE CATEGORY = :category " +
//		        "AND DPD = :dpd " +
//		        "AND ORDER_NO IS NOT NULL " +
//		        "ORDER BY ORDER_NO",
//		        nativeQuery = true)
//
//		List<String> getCeRanges(
//		        @Param("category") String category,
//		        @Param("dpd") String dpd);
	 
	 @Query(value =
			    "SELECT RANGE_VALUE " +
			    "FROM TS_CCR_SLAB_RANGE_MST " +
			    "WHERE CATEGORY = :category " +
			    "AND DPD = :dpd " +
			    "AND STATUS = 'Y' " +
			    "AND ORDER_NO IS NOT NULL " +
			    "ORDER BY ORDER_NO",
			    nativeQuery = true)
			List<String> getCeRanges(
			        @Param("category") String category,
			        @Param("dpd") String dpd);
	 
//////new req
	 
	 Optional<CeRangeMaster> findByCategoryAndDpdAndRangeValue(
		        String category,
		        String dpd,
		        String rangeValue);

		List<CeRangeMaster> findByCategoryAndDpdOrderByOrderNo(
		        String category,
		        String dpd);
	
		Optional<CeRangeMaster> findByCategoryAndDpdAndOrderNo(
		        String category,
		        String dpd,
		        Integer orderNo);
		
		List<CeRangeMaster> findByStatusOrderByCategoryAscDpdAscOrderNoAsc(
		        String status);

		List<CeRangeMaster> findByCategoryAndStatusOrderByDpdAscOrderNoAsc(
		        String category,
		        String status);

		List<CeRangeMaster> findByCategoryAndDpdAndStatusOrderByOrderNoAsc(
		        String category,
		        String dpd,
		        String status);
		
		List<CeRangeMaster> findAllByOrderByCategoryAscDpdAscOrderNoAsc();
		
		Optional<CeRangeMaster>
		findByCategoryAndDpdAndRangeValueIsNull(
		        String category,
		        String dpd);
		
		Optional<CeRangeMaster> findFirstByCategoryAndDpd(String category,String dpd);
		
		List<CeRangeMaster> findByCategory(String category);

}
