package com.icici.dma.slabRepository.osp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.osp.OspCollectionRangeMaster;

@Repository
public interface OspCollectionRangeRepo
        extends JpaRepository<OspCollectionRangeMaster, Long>{

    List<OspCollectionRangeMaster>
    findByDpdOrderByOrderNo(String dpd);

}