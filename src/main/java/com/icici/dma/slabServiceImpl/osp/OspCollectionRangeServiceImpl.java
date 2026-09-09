package com.icici.dma.slabServiceImpl.osp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icici.dma.slabEntity.osp.OspCollectionRangeMaster;
import com.icici.dma.slabRepository.osp.OspCollectionRangeRepo;
import com.icici.dma.slabService.osp.OspCollectionRangeService;

@Service
public class OspCollectionRangeServiceImpl
        implements OspCollectionRangeService{

    @Autowired
    private OspCollectionRangeRepo repo;

    @Override
    public List<OspCollectionRangeMaster> getRanges(String dpd) {

        return repo.findByDpdOrderByOrderNo(dpd);

    }

}