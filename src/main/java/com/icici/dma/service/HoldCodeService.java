package com.icici.dma.service;

import java.util.List;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.HoldCodeDto;
import com.icici.dma.model.HoldCodeMaster;
import com.icici.dma.model.HoldCodeMasterTemp;

//snz
public interface HoldCodeService {

    List<HoldCodeDto> getHoldCodeMaker();

    void createHoldCodeMaker(
            HoldCodeDto dto,
            String username);

    void updateHoldCodeMaker(HoldCodeDto dto, String username);

    void updateHoldCodeChecker(CheckerDecisionReq requestPayload, String username);

    List<HoldCodeDto> getAllHoldCodeChecker(String user);

    List<HoldCodeDto> getHoldCodeByStatus(String statusType);

    default HoldCodeDto convertMasterToDto( HoldCodeMaster e) {

        HoldCodeDto d = new HoldCodeDto();

        d.setCode(e.getCode());
        d.setHoldReason(e.getHoldReason());
        d.setCreatedBy(e.getCreatedBy());
        d.setCreatedDate(e.getCreatedDate());
        d.setModifiedBy(e.getModifiedBy());
        d.setModifiedDate(e.getModifiedDate());
        d.setStatus(e.getStatus());

        return d;
    }

    default HoldCodeDto convertTempToDto(
            HoldCodeMasterTemp e) {

        HoldCodeDto d = new HoldCodeDto();

        d.setCode(e.getCode());
        d.setHoldReason(e.getHoldReason());
        d.setCreatedBy(e.getCreatedBy());
        d.setCreatedDate(e.getCreatedDate());
        d.setModifiedBy(e.getModifiedBy());
        d.setModifiedDate(e.getModifiedDate());
        d.setRemarks(e.getRemarks());
        d.setStatus(e.getStatus());
        d.setActionType(e.getActionType());
        d.setActionDate(e.getActionDate());
        d.setActionUser(e.getActionUser());

        return d;
    }

    default HoldCodeMasterTemp convertDtoToTemp(
            HoldCodeDto dto) {

        HoldCodeMasterTemp e =
                new HoldCodeMasterTemp();

        e.setCode(dto.getCode());
        e.setHoldReason(dto.getHoldReason());

        return e;
    }
}