package com.icici.dma.service;

import java.util.List;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.PaymentCodeDto;
import com.icici.dma.model.PaymentCodeMaster;
import com.icici.dma.model.PaymentCodeMasterTemp;

public interface PaymentCodeService {
	
	//snz

    List<PaymentCodeDto> getPaymentCodeMaker();

    void createPaymentCodeMaker(PaymentCodeDto dto, String username);

    void updatePaymentCodeMaker(PaymentCodeDto dto, String username);

    void updatePaymentCodeChecker(CheckerDecisionReq requestPayload, String username);

    List<PaymentCodeDto> getAllPaymentCodeChecker( String user);

    List<PaymentCodeDto> getPaymentCodeByStatus(String statusType);

    default PaymentCodeDto convertMasterToDto(PaymentCodeMaster e) {

        PaymentCodeDto d = new PaymentCodeDto();

        d.setCode(e.getCode());
        d.setCreatedBy(e.getCreatedBy());
        d.setCreatedDate(e.getCreatedDate());
        d.setModifiedBy(e.getModifiedBy());
        d.setModifiedDate(e.getModifiedDate());
        d.setStatus(e.getStatus());

        return d;
    }

   
    default PaymentCodeDto convertTempToDto(
            PaymentCodeMasterTemp e) {

        PaymentCodeDto d = new PaymentCodeDto();

        d.setCode(e.getCode());
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

    default PaymentCodeMasterTemp convertDtoToTemp(
            PaymentCodeDto dto) {

        PaymentCodeMasterTemp e =
                new PaymentCodeMasterTemp();

        e.setCode(dto.getCode());

        return e;
    }
}