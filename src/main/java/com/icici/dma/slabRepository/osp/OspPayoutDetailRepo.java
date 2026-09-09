package com.icici.dma.slabRepository.osp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.osp.OspPayoutDetail;

@Repository
public interface OspPayoutDetailRepo
        extends JpaRepository<OspPayoutDetail,Long>{

    List<OspPayoutDetail>
    findByMasterIdOrderByOrderNo(Long masterId);

}