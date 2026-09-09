package com.icici.dma.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.PaymentCodeDto;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.model.PaymentCodeMaster;
import com.icici.dma.model.PaymentCodeMasterTemp;
import com.icici.dma.repository.PaymentCodeMasterRepository;
import com.icici.dma.repository.PaymentCodeMasterTempRepository;
import com.icici.dma.service.PaymentCodeService;

@Service
@Transactional
public class PaymentCodeServiceImpl
        implements PaymentCodeService {

	//snz
	
    @Autowired
    private PaymentCodeMasterRepository paymentCodeRepo;

    @Autowired
    private PaymentCodeMasterTempRepository paymentCodeTempRepo;

    @Override
    public List<PaymentCodeDto> getPaymentCodeMaker() {

        List<PaymentCodeDto> dtoList = new ArrayList<>();

        paymentCodeRepo.findAllByStatus(
                StatusConstant.APPROVE)
                .forEach(e -> dtoList.add(convertMasterToDto(e)));

        paymentCodeTempRepo.findAllByStatus(
                StatusConstant.PENDING)
                .forEach(e -> dtoList.add(convertTempToDto(e)));

        dtoList.sort((a, b) -> {

            Date dateA =
                    a.getActionDate() != null ? a.getActionDate()
                    : a.getModifiedDate() != null ? a.getModifiedDate()
                    : a.getCreatedDate();

            Date dateB =
                    b.getActionDate() != null ? b.getActionDate()
                    : b.getModifiedDate() != null ? b.getModifiedDate()
                    : b.getCreatedDate();

            if (dateA == null && dateB == null) return 0;
            if (dateA == null) return 1;
            if (dateB == null) return -1;

            return dateB.compareTo(dateA);
        });

        return dtoList;
    }

    @Override
    public void createPaymentCodeMaker(
            PaymentCodeDto dto,
            String username) {

        if (dto.getCode() == null ||
                dto.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Code is mandatory");
        }

        if(paymentCodeTempRepo.existsById(dto.getCode())) {

            PaymentCodeMasterTemp temp =
                    paymentCodeTempRepo.findById(dto.getCode()).get();

            if(StatusConstant.PENDING.equals(temp.getStatus())) {

                throw new IllegalArgumentException(
                        "Already waiting for approval");
            }

            throw new IllegalArgumentException(
                    "Already Rejected");
        }

        if(paymentCodeRepo.existsById(dto.getCode())) {

            throw new IllegalArgumentException(
                    "Record Already Approved");
        }

        PaymentCodeMasterTemp entity =
                convertDtoToTemp(dto);

        entity.setCreatedBy(username);
        entity.setCreatedDate(new Date());
        entity.setStatus(StatusConstant.PENDING);
        entity.setActionType("CREATE");
        entity.setActionDate(new Date());
        entity.setActionUser(username);

        paymentCodeTempRepo.save(entity);
    }

    @Override
    public void updatePaymentCodeMaker(
            PaymentCodeDto dto,
            String username) {

    	Optional<PaymentCodeMasterTemp> tempOpt =
    	        paymentCodeTempRepo.findById(dto.getCode());

    	if(tempOpt.isPresent()) {

    	    PaymentCodeMasterTemp existingTemp =
    	            tempOpt.get();

    	    if(StatusConstant.PENDING.equals(existingTemp.getStatus())) {

    	        throw new IllegalArgumentException(
    	                "Record already waiting for approval");
    	    }

    	    throw new IllegalArgumentException(
    	            "Rejected records cannot be edited");
    	}

    	PaymentCodeMaster master =
    	        paymentCodeRepo.findById(dto.getCode())
    	        .orElseThrow(() ->
    	                new ResourceNotFoundException(
    	                        "Approved record not found"));

    	PaymentCodeMasterTemp temp =
    	        convertMasterToTemp(master);

    	temp.setStatus(StatusConstant.PENDING);

    	temp.setCreatedBy(master.getCreatedBy());
    	temp.setCreatedDate(master.getCreatedDate());

    	temp.setActionType(ActionConstant.UPDATE);
    	temp.setActionDate(new Date());
    	temp.setActionUser(username);

    	temp.setModifiedBy(username);
    	temp.setModifiedDate(new Date());

    	paymentCodeTempRepo.save(temp);
    }

    @Override
    public List<PaymentCodeDto> getAllPaymentCodeChecker(
            String user) {

        List<PaymentCodeDto> dtoList =
                new ArrayList<>();

        paymentCodeTempRepo
        .findAllByStatusAndActionUserNot(
                        StatusConstant.PENDING,
                        user)
                .forEach(e ->
                        dtoList.add(convertTempToDto(e)));

        return dtoList;
    }

    @Override
    public List<PaymentCodeDto> getPaymentCodeByStatus(
            String statusType) {

        List<PaymentCodeDto> dtoList =
                new ArrayList<>();

        if (StatusConstant.ALL.equalsIgnoreCase(statusType)) {

            paymentCodeRepo.findAllByStatus(
                    StatusConstant.APPROVE)
                    .forEach(e ->
                            dtoList.add(convertMasterToDto(e)));

            paymentCodeTempRepo.findAllByStatus(
                    StatusConstant.PENDING)
                    .forEach(e ->
                            dtoList.add(convertTempToDto(e)));

            dtoList.sort((a, b) -> {

                Date dateA =
                        a.getActionDate() != null ? a.getActionDate()
                        : a.getModifiedDate() != null ? a.getModifiedDate()
                        : a.getCreatedDate();

                Date dateB =
                        b.getActionDate() != null ? b.getActionDate()
                        : b.getModifiedDate() != null ? b.getModifiedDate()
                        : b.getCreatedDate();

                if (dateA == null && dateB == null) {
                    return 0;
                }

                if (dateA == null) {
                    return 1;
                }

                if (dateB == null) {
                    return -1;
                }

                return dateB.compareTo(dateA);
            });
        }
		else if (StatusConstant.APPROVE.equalsIgnoreCase(statusType)) {

			paymentCodeRepo.findAllByStatus(StatusConstant.APPROVE).forEach(e -> dtoList.add(convertMasterToDto(e)));
		} else if (StatusConstant.PENDING.equalsIgnoreCase(statusType)) {

			paymentCodeTempRepo.findAllByStatus(StatusConstant.PENDING).forEach(e -> dtoList.add(convertTempToDto(e)));
		} else if (StatusConstant.REJECTE.equalsIgnoreCase(statusType)) {

			paymentCodeTempRepo.findAllByStatus(StatusConstant.REJECTE).forEach(e -> dtoList.add(convertTempToDto(e)));
		} else {
			throw new IllegalArgumentException("Invalid status");
		}

		if (dtoList.isEmpty()) {
			throw new ResourceNotFoundException("No records found");
		}

        return dtoList;
    }

    @Override
    public void updatePaymentCodeChecker(
            CheckerDecisionReq requestPayload,
            String username) {

        List<String> codes =
                requestPayload.getPrimaryIds();

        
        List<String> validCodes =
                codes.stream()
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
        
        if(validCodes.isEmpty()) {
            throw new IllegalArgumentException(
                    "No records selected");
        }

        List<String> pendingCodes =
                paymentCodeTempRepo.findPendingCodes(
                        validCodes,
                        StatusConstant.PENDING);

        if (pendingCodes.size() != validCodes.size()) {

            List<String> missing =
                    new ArrayList<>(validCodes);

            missing.removeAll(pendingCodes);

            throw new ResourceNotFoundException(
                    "Pending records not found: " + missing);
        }

        List<PaymentCodeMasterTemp> tempRecords =
                paymentCodeTempRepo.findAllById(
                        pendingCodes);

        if (StatusConstant.APPROVE.equalsIgnoreCase(
                requestPayload.getDecision())) {

            List<PaymentCodeMaster> approved =
                    tempRecords.stream()
                            .map(t -> {
                            	Optional<PaymentCodeMaster> existing =
                            	        paymentCodeRepo.findById(t.getCode());

                            	PaymentCodeMaster m;

                            	if(existing.isPresent()) {

                            	    m = existing.get();

                            	} else {

                            	    m = new PaymentCodeMaster();

                            	    m.setCreatedBy(t.getCreatedBy());
                            	    m.setCreatedDate(t.getCreatedDate());
                            	}

                            	m.setCode(t.getCode());

                            	m.setModifiedBy(username);
                            	m.setModifiedDate(new Date());

                            	m.setStatus(StatusConstant.APPROVE);

                            	return m;
                            })
                            .collect(Collectors.toList());
            paymentCodeRepo.saveAll(approved);

            paymentCodeTempRepo.deleteApprovedRecords(
                    pendingCodes);

        }else {

                paymentCodeTempRepo.bulkUpdateStatus(
                        pendingCodes,
                        StatusConstant.REJECTE,
                        requestPayload.getRemark(),
                        username,
                        StatusConstant.PENDING);
        }
      
    }
    
    private PaymentCodeMasterTemp convertMasterToTemp(
            PaymentCodeMaster master) {

        PaymentCodeMasterTemp temp =
                new PaymentCodeMasterTemp();

        temp.setCode(master.getCode());

        return temp;
    }
}