package com.icici.dma.slabService.osp;

import java.util.List;

import com.icici.dma.slabEntity.osp.OspCollectionRangeMaster;

public interface OspCollectionRangeService {

    List<OspCollectionRangeMaster> getRanges(String dpd);

}