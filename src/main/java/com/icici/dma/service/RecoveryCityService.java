package com.icici.dma.service;

import java.util.List;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.RecoveryCityDto;

public interface RecoveryCityService {

    List<RecoveryCityDto> getRecoveryCityMaker();

    void createRecoveryCity( RecoveryCityDto dto, String username);

    void updateRecoveryCityByMaker( RecoveryCityDto dto,String username);

    List<RecoveryCityDto> getAllRecoveryCityChecker( String user);

    void updateRecoveryCityByChecker( CheckerDecisionReq requestPayload, String username);

    List<RecoveryCityDto> getRecoveryCityByStatus(String statusType);
}