package com.icici.dma.slabRepository.osp;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.osp.OspConfigCollectionRangeMaster;
import com.icici.dma.slabEntity.osp.OspConfigDpdMaster;

@Repository
public interface OspConfigCollectionRangeMasterRepository
        extends JpaRepository<OspConfigCollectionRangeMaster, Long> {

    List<OspConfigCollectionRangeMaster>
    findByDpdAndStatusOrderByOrderNo(
            OspConfigDpdMaster dpd,
            String status);

    boolean existsByDpdAndOrderNo(
            OspConfigDpdMaster dpd,
            Integer orderNo);

    boolean existsByDpdAndFromAmountAndToAmount(
            OspConfigDpdMaster dpd,
            BigDecimal fromAmount,
            BigDecimal toAmount);

    @Query("SELECT CASE WHEN COUNT(r)>0 THEN TRUE ELSE FALSE END "
         + "FROM OspConfigCollectionRangeMaster r "
         + "WHERE r.dpd = :dpd "
         + "AND r.fromAmount <= :toAmount "
         + "AND r.toAmount >= :fromAmount")
    boolean existsOverlappingRange(
            OspConfigDpdMaster dpd,
            BigDecimal fromAmount,
            BigDecimal toAmount);
    
    boolean existsByDpdAndOrderNoAndIdNot(
            OspConfigDpdMaster dpd,
            Integer orderNo,
            Long id);

    boolean existsByDpdAndFromAmountAndToAmountAndIdNot(
            OspConfigDpdMaster dpd,
            BigDecimal fromAmount,
            BigDecimal toAmount,
            Long id);
    
    @Query("SELECT CASE WHEN COUNT(r)>0 THEN true ELSE false END " +
    	       "FROM OspConfigCollectionRangeMaster r " +
    	       "WHERE r.dpd=:dpd " +
    	       "AND r.id<>:id " +
    	       "AND r.fromAmount<=:toAmount " +
    	       "AND r.toAmount>=:fromAmount")
    	boolean existsOverlappingRangeExcludingId(
    	        @Param("dpd") OspConfigDpdMaster dpd,
    	        @Param("fromAmount") BigDecimal fromAmount,
    	        @Param("toAmount") BigDecimal toAmount,
    	        @Param("id") Long id);

    boolean existsByDpdAndIsMax(
            OspConfigDpdMaster dpd,
            String isMax);
}