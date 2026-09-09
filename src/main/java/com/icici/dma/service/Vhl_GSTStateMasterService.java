package com.icici.dma.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.GSTStateMasterDto;

public interface Vhl_GSTStateMasterService {

	// ---------- Upload ----------
	public Map<String, Object> uploadGSTStateMaster(MultipartFile file, String loginUser);

	// ---------- Error Download ----------
	public ByteArrayInputStream exportErrorExcel(String loginUser);

	// ---------- Maker screen ----------
	public List<GSTStateMasterDto> getGstStateMasterByStatus(String statusType);

	public void createGstStateMasterByMaker(GSTStateMasterDto dto, String username);

	public void updateGstStateMasterByMaker(GSTStateMasterDto dto, String username);

	// ---------- Checker screen ----------
	public List<GSTStateMasterDto> getAllGstStateMasterByChecker(String user);

	public void updateGstStateMasterByChecker(CheckerDecisionReq requestPayload, String username);

	// ---------- Status-wise listing (used by /vhlMaster) ----------
//	public List<GSTStateMasterDto> getGSTStateMasterByStatus(String statusType);

}
