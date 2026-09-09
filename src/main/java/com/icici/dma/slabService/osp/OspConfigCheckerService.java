package com.icici.dma.slabService.osp;

import java.util.List;

import com.icici.dma.dto.osp.OspConfigCompareResponse;
import com.icici.dma.dto.osp.OspConfigPendingResponse;

public interface OspConfigCheckerService {

//    List<OspConfigPendingResponse> getPending();
    
    List<OspConfigPendingResponse> getPending(String userId);

    String approve(Long tempId,
                   String type,
                   String userId,
                   String remarks);

    String reject(Long tempId,
                  String type,
                  String userId,
                  String remarks);

    OspConfigCompareResponse makerCompare(Long id,
            String type);

    OspConfigCompareResponse checkerCompare(Long tempId,
              String type);
}
