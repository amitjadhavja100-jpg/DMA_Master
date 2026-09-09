package com.icici.dma.service;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.FlowCityMasterDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Repository
public interface FlowCityMasterService {

    Map<String, Object> upload(MultipartFile file, String user);

    ResponseEntity<ByteArrayResource> downloadErrorFile(String uploadId) throws IOException;
    List<FlowCityMasterDto> getRecordsByStatus(String statusType);

    List<FlowCityMasterDto> getAllFlowCityMasterChecker(String user);

    void updateFlowCityMasterByChecker(CheckerDecisionReq requestPayload, String username);

    void updateFlowCityMasterByMaker(FlowCityMasterDto row, String user);

}
