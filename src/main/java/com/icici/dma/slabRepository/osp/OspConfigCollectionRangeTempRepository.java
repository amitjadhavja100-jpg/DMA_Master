package com.icici.dma.slabRepository.osp;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.osp.OspConfigCollectionRangeTemp;
import com.icici.dma.slabEntity.osp.OspConfigDpdMaster;


@Repository
public interface OspConfigCollectionRangeTempRepository
        extends JpaRepository<OspConfigCollectionRangeTemp, Long> {

    List<OspConfigCollectionRangeTemp>
    findByStatusOrderByCreatedDateDesc(String status);
    
    boolean existsByDpdAndOrderNoAndStatus(
            OspConfigDpdMaster dpd,
            Integer orderNo,
            String status);

    boolean existsByDpdAndFromAmountAndToAmountAndStatus(
            OspConfigDpdMaster dpd,
            BigDecimal fromAmount,
            BigDecimal toAmount,
            String status);

    boolean existsByRangeIdAndStatus(
            Long rangeId,
            String status);
    
    List<OspConfigCollectionRangeTemp> findByStatusAndCreatedByNotOrderByCreatedDateDesc(
            String status,
            String createdBy);
    
    boolean existsByDpdAndIsMaxAndStatus(
            OspConfigDpdMaster dpd,
            String isMax,
            String status);

}