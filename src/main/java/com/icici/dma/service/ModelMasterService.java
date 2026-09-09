package com.icici.dma.service;

import java.util.List;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.GstMasterDto;
import com.icici.dma.dto.ModelMasterDto;

public interface ModelMasterService {

	public List<ModelMasterDto> getModelMasMaker();

	public void updateModelMasByMaker(ModelMasterDto modelMasterDto, String username);

	public void createModelMas(ModelMasterDto modelMasterDto, String username);

	public List<ModelMasterDto> getAllModelMasterChecker(String user);

	public void updateModelMasByChecker(CheckerDecisionReq requestPayload,String username);

	public List<?> getModelMasterByStatus(String statusType);
}
