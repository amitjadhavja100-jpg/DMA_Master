package com.icici.dma.service;

import java.util.List;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.SapMasterDto;
import com.icici.dma.model.SAPMaster;
import com.icici.dma.model.SAPMasterTemp;

public interface SapMasterService {

    List<SapMasterDto> getSAPMasterMaker();

    void createSAPMaster(SapMasterDto sapMasterDto, String username);

    void updateSAPMasterByMaker(SapMasterDto sapMasterDto, String username);

    void updateSAPMasterByChecker(CheckerDecisionReq requestPayload, String username);

    List<SapMasterDto> getAllSAPMasterChecker(String user);

    List<SapMasterDto> getSAPMasterByStatus(String statusType);

    default SapMasterDto convertSAPMasterToDto(SAPMaster e) {

        SapMasterDto d = new SapMasterDto();

        d.setSrNo(e.getSrNo());
        d.setUnitCode(e.getUnitCode());
        d.setCleanUnitId(e.getCleanUnitId());
        d.setVendorName(e.getVendorName());
        d.setCcaCallCenter(e.getCcaCallCenter());
        d.setPan(e.getPan());
        d.setSapVendorCode(e.getSapVendorCode());
        d.setState(e.getState());
        d.setTdsRate(e.getTdsRate());
        d.setTaxCode(e.getTaxCode());
        d.setFinaliBoxids(e.getFinaliBoxids());
        d.setStatusOfBlocking(e.getStatusOfBlocking());
        d.setAccountStatus(e.getAccountStatus());
        d.setCredInfoNo(e.getCredInfoNo());
        d.setGstnNo(e.getGstnNo());
        d.setSacCode(e.getSacCode());
        d.setServiceProviderIdStatus(e.getServiceProviderIdStatus());
        d.setGstApplicable(e.getGstApplicable());
        d.setHoldStatus(e.getHoldStatus());
        d.setPaymentMode(e.getPaymentMode());

        d.setCreatedBy(e.getCreatedBy());
        d.setCreatedDate(e.getCreatedDate());
        d.setModifiedBy(e.getModifiedBy());
        d.setModifiedDate(e.getModifiedDate());
        d.setStatus(e.getStatus());

        return d;
    }

    default SapMasterDto convertSAPMasterTempToDto(SAPMasterTemp e) {

        SapMasterDto d = new SapMasterDto();

        d.setSrNo(e.getSrNo());
        d.setUnitCode(e.getUnitCode());
        d.setCleanUnitId(e.getCleanUnitId());
        d.setVendorName(e.getVendorName());
        d.setCcaCallCenter(e.getCcaCallCenter());
        d.setPan(e.getPan());
        d.setSapVendorCode(e.getSapVendorCode());
        d.setState(e.getState());
        d.setTdsRate(e.getTdsRate());
        d.setTaxCode(e.getTaxCode());
        d.setFinaliBoxids(e.getFinaliBoxids());
        d.setStatusOfBlocking(e.getStatusOfBlocking());
        d.setAccountStatus(e.getAccountStatus());
        d.setCredInfoNo(e.getCredInfoNo());
        d.setGstnNo(e.getGstnNo());
        d.setSacCode(e.getSacCode());
        d.setServiceProviderIdStatus(e.getServiceProviderIdStatus());
        d.setGstApplicable(e.getGstApplicable());
        d.setHoldStatus(e.getHoldStatus());
        d.setPaymentMode(e.getPaymentMode());

        d.setCreatedBy(e.getCreatedBy());
        d.setCreatedDate(e.getCreatedDate());
        d.setModifiedBy(e.getModifiedBy());
        d.setModifiedDate(e.getModifiedDate());
        d.setStatus(e.getStatus());
        d.setActionType(e.getActionType());
        d.setActionDate(e.getActionDate());
        d.setActionUser(e.getActionUser());

        return d;
    }

    default SAPMasterTemp convertDtoToSAPMasterTemp(SapMasterDto e) {

        SAPMasterTemp d = new SAPMasterTemp();

        d.setSrNo(e.getSrNo());
        d.setUnitCode(e.getUnitCode());
        d.setCleanUnitId(e.getCleanUnitId());
        d.setVendorName(e.getVendorName());
        d.setCcaCallCenter(e.getCcaCallCenter());
        d.setPan(e.getPan());
        d.setSapVendorCode(e.getSapVendorCode());
        d.setState(e.getState());
        d.setTdsRate(e.getTdsRate());
        d.setTaxCode(e.getTaxCode());
        d.setFinaliBoxids(e.getFinaliBoxids());
        d.setStatusOfBlocking(e.getStatusOfBlocking());
        d.setAccountStatus(e.getAccountStatus());
        d.setCredInfoNo(e.getCredInfoNo());
        d.setGstnNo(e.getGstnNo());
        d.setSacCode(e.getSacCode());
        d.setServiceProviderIdStatus(e.getServiceProviderIdStatus());
        d.setGstApplicable(e.getGstApplicable());
        d.setHoldStatus(e.getHoldStatus());
        d.setPaymentMode(e.getPaymentMode());

        return d;
    }
}