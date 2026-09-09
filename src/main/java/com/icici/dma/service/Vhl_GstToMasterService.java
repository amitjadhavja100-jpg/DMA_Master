package com.icici.dma.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.GstToMasterDto;

public interface Vhl_GstToMasterService {

	Map<String, Object> uploadGstToMaster(MultipartFile file, String loginUser);

	ByteArrayInputStream exportErrorExcel(String loginUser);

	List<GstToMasterDto> getAllGstToMasterMaker();

	public void createGstToMasterByMaker(GstToMasterDto dto, String username);

	public void updateGstToMasterByMaker(GstToMasterDto dto, String username);

	public List<GstToMasterDto> getAllGstToMasterByChecker(String user);

	public void updateGstStateMasterByChecker(CheckerDecisionReq requestPayload, String username);

	public List<GstToMasterDto> getGstToMasterByStatus(String statusType);

}
