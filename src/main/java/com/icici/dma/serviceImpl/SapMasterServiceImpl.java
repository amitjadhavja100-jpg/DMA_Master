package com.icici.dma.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.SapMasterDto;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.SAPMaster;
import com.icici.dma.model.SAPMasterTemp;
import com.icici.dma.repository.SAPMasterRepository;
import com.icici.dma.repository.SAPMasterTempRepository;
import com.icici.dma.service.SapMasterService;

@Service
public class SapMasterServiceImpl implements SapMasterService {

    private static final Logger logger =
            LogManager.getLogger(SapMasterServiceImpl.class);

    @Autowired
    private SAPMasterRepository sapMasterRepo;

    @Autowired
    private SAPMasterTempRepository sapMasterTempRepo;
    
    @Override
    public List<SapMasterDto> getSAPMasterMaker() {

        List<SapMasterDto> dtoList = new ArrayList<>();

        // APPROVED RECORDS FROM MAIN TABLE
        
        List<SAPMaster> approvedList = sapMasterRepo.findAll();

        if (approvedList != null && !approvedList.isEmpty()) {

            approvedList.forEach(record ->
                    dtoList.add(
                            convertSAPMasterToDto(record)));
        }

        // PENDING RECORDS FROM TEMP TABLE
         
		List<SAPMasterTemp> pendingList = sapMasterTempRepo.findAllByStatus(StatusConstant.PENDING);

		if (pendingList != null && !pendingList.isEmpty()) {
			pendingList.forEach(record -> dtoList.add(convertSAPMasterTempToDto(record)));
		}

		// EMPTY CHECK
		if (dtoList.isEmpty()) {
			throw new ResourceNotFoundException("No SAP records found");
		}

        return dtoList;
    }


    @Override
    public void createSAPMaster(SapMasterDto dto, String username) {

        logger.info("Create SAP Master request for {}", dto.getUnitCode());
        
        //snz 15/7
        
		if (sapMasterTempRepo.existsByUnitCode(dto.getUnitCode())) {

			SAPMasterTemp temp = sapMasterTempRepo.findByUnitCode(dto.getUnitCode()).get();

			if (StatusConstant.PENDING.equals(temp.getStatus())) {
				throw new IllegalArgumentException("Already waiting for approval");
			}

			throw new IllegalArgumentException("Already Rejected");
		}

		if (sapMasterRepo.existsByUnitCode(dto.getUnitCode())) {

			throw new IllegalArgumentException("Record Already Approved");
		}

        SAPMasterTemp temp = convertDtoToSAPMasterTemp(dto);

        temp.setStatus(StatusConstant.PENDING);
        temp.setCreatedBy(username);
        temp.setCreatedDate(new Date());

        temp.setActionType(ActionConstant.INSERT);
        temp.setActionDate(new Date());
        temp.setActionUser(username);

        sapMasterTempRepo.save(temp);
    }

    @Override
    public void updateSAPMasterByMaker(
            SapMasterDto dto,
            String username) {

        String unitCode = dto.getUnitCode();

        Optional<SAPMasterTemp> tempOpt =
                sapMasterTempRepo.findByUnitCode(unitCode);

        if (tempOpt.isPresent()) {

            SAPMasterTemp existingTemp = tempOpt.get();

            if (StatusConstant.PENDING.equals(existingTemp.getStatus())) {
                throw new IllegalArgumentException(
                        "Record already waiting for approval");
            }

            throw new IllegalArgumentException(
                    "Rejected records cannot be edited");
        }

        SAPMaster master =
                sapMasterRepo.findByUnitCode(unitCode)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Approved record not found"));

        SAPMasterTemp temp =
                convertMasterToTemp(master);

        temp.setSrNo(dto.getSrNo());
        temp.setUnitCode(dto.getUnitCode());
        temp.setCleanUnitId(dto.getCleanUnitId());
        temp.setVendorName(dto.getVendorName());
        temp.setPan(dto.getPan());
        temp.setState(dto.getState());
        temp.setGstnNo(dto.getGstnNo());
        temp.setCcaCallCenter(dto.getCcaCallCenter());
        temp.setTdsRate(dto.getTdsRate());
        temp.setTaxCode(dto.getTaxCode());
        temp.setFinaliBoxids(dto.getFinaliBoxids());
        temp.setStatusOfBlocking(dto.getStatusOfBlocking());
        temp.setAccountStatus(dto.getAccountStatus());
        temp.setCredInfoNo(dto.getCredInfoNo());
        temp.setSacCode(dto.getSacCode());
        temp.setServiceProviderIdStatus(dto.getServiceProviderIdStatus());
        temp.setGstApplicable(dto.getGstApplicable());
        temp.setHoldStatus(dto.getHoldStatus());
        temp.setPaymentMode(dto.getPaymentMode());

        temp.setStatus(StatusConstant.PENDING);
        
        temp.setCreatedBy(username);
        temp.setCreatedDate(new Date());
        
        temp.setActionType(ActionConstant.UPDATE);
        temp.setActionDate(new Date());
        temp.setActionUser(username);

        temp.setModifiedBy(username);
        temp.setModifiedDate(new Date());

        sapMasterTempRepo.save(temp);
    }
    

    @Override
    public List<SapMasterDto> getAllSAPMasterChecker(String user) {

        List<SAPMasterTemp> list =
                sapMasterTempRepo.findAllByStatusAndCreatedByNot( 
                        StatusConstant.PENDING,
                        user);

        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException("No pending SAP records");
        }

        return list.stream()
                .map(this::convertSAPMasterTempToDto)
                .collect(Collectors.toList());
    }

    
    @Override
    @Transactional
    public void updateSAPMasterByChecker(
            CheckerDecisionReq requestPayload,
            String username) {

        if (requestPayload == null) {
            throw new IllegalArgumentException("Request payload is null");
        }

        String decision = requestPayload.getDecision();

        if (decision == null || decision.trim().isEmpty()) {
            throw new IllegalArgumentException("Decision is mandatory");
        }

        decision = decision.toUpperCase();

        List<String> ids = requestPayload.getPrimaryIds();

        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("No Unit Codes selected");
        }

        List<String> pendingIds = new ArrayList<>();

        for (int i = 0; i < ids.size(); i += 500) {
            List<String> batch = ids.subList(i,Math.min(i + 500, ids.size()));
            
            pendingIds.addAll(sapMasterTempRepo.findPendingIds(batch,StatusConstant.PENDING));
        }

        if (pendingIds.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No pending records found");
        }

        // APPROVE
        if (StatusConstant.APPROVE.equals(decision)) {

            for (String unitCode : pendingIds) {

                SAPMasterTemp temp =
                        sapMasterTempRepo
                                .findByUnitCodeAndStatus(
                                		unitCode,
                                        StatusConstant.PENDING)
                                .orElseThrow(() ->
                                        new ResourceNotFoundException(
                                                "Record not found : "
                                                        + unitCode));

                Optional<SAPMaster> existing =
                        sapMasterRepo.findByUnitCode(
                                temp.getUnitCode());

                SAPMaster master;

                if(existing.isPresent()) {

                    master = existing.get();
                    
                    master.setId(existing.get().getId());

                    master.setModifiedBy(username);
                    master.setModifiedDate(new Date());

                }
                else {

                    master = new SAPMaster();

                    master.setCreatedBy(temp.getCreatedBy());
                    master.setCreatedDate(temp.getCreatedDate());

                }

                master.setSrNo(temp.getSrNo());
                master.setUnitCode(temp.getUnitCode());
                master.setCleanUnitId(temp.getCleanUnitId());
                master.setVendorName(temp.getVendorName());
                master.setCcaCallCenter(temp.getCcaCallCenter());
                master.setPan(temp.getPan());
                master.setSapVendorCode(temp.getSapVendorCode());
                master.setState(temp.getState());
                master.setTdsRate(temp.getTdsRate());
                master.setTaxCode(temp.getTaxCode());
                master.setFinaliBoxids(temp.getFinaliBoxids());
                master.setStatusOfBlocking(temp.getStatusOfBlocking());
                master.setAccountStatus(temp.getAccountStatus());
                master.setCredInfoNo(temp.getCredInfoNo());
                master.setGstnNo(temp.getGstnNo());
                master.setSacCode(temp.getSacCode());
                master.setServiceProviderIdStatus(temp.getServiceProviderIdStatus());
                master.setGstApplicable(temp.getGstApplicable());
                master.setHoldStatus(temp.getHoldStatus());
                master.setPaymentMode(temp.getPaymentMode());
                
                if (!existing.isPresent()) {
                    master.setCreatedBy(temp.getCreatedBy());
                    master.setCreatedDate(temp.getCreatedDate());
                }

                master.setModifiedBy(username);
                master.setModifiedDate(new Date());

                master.setStatus(
                        StatusConstant.APPROVE);

                sapMasterRepo.save(master);
            }

			for (int i = 0; i < pendingIds.size(); i += 500) {

				List<String> batch = pendingIds.subList(i, Math.min(i + 500, pendingIds.size()));

				sapMasterTempRepo.deleteApprovedRecords(batch);
			}
        }

        // REJECT
		else if (StatusConstant.REJECTE.equals(decision)) {

			for (int i = 0; i < pendingIds.size(); i += 500) {

				List<String> batch = pendingIds.subList(i, Math.min(i + 500, pendingIds.size()));

				sapMasterTempRepo.bulkUpdateStatus(batch, StatusConstant.REJECTE, requestPayload.getRemark(),
						StatusConstant.PENDING);
			}
		}

		else {

			throw new IllegalArgumentException("Invalid decision. Use A or R");
		}
    }
    
    @Override
    public List<SapMasterDto> getSAPMasterByStatus(String statusType) {

        List<SapMasterDto> dtoList = new ArrayList<>();

        if (StatusConstant.ALL.equalsIgnoreCase(statusType)) {

            // Approved records
            sapMasterRepo.findAllByStatus(StatusConstant.APPROVE)
                    .forEach(e -> dtoList.add(convertSAPMasterToDto(e)));

            // Pending records
            sapMasterTempRepo.findAllByStatus(StatusConstant.PENDING)
                    .forEach(e -> dtoList.add(convertSAPMasterTempToDto(e)));

            // Final sorting like GST
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

        } else if (StatusConstant.APPROVE.equals(statusType)) {

            sapMasterRepo.findAllByStatus(StatusConstant.APPROVE)
                    .forEach(e -> dtoList.add(convertSAPMasterToDto(e)));

        } else if (StatusConstant.PENDING.equals(statusType)) {

            sapMasterTempRepo.findAllByStatus(StatusConstant.PENDING)
                    .forEach(e -> dtoList.add(convertSAPMasterTempToDto(e)));

        } else if (StatusConstant.REJECTE.equals(statusType)) {

            sapMasterTempRepo.findAllByStatus(StatusConstant.REJECTE)
                    .forEach(e -> dtoList.add(convertSAPMasterTempToDto(e)));

        } else {
            throw new IllegalArgumentException("Invalid status");
        }

        return dtoList;
    }

    
    private SAPMasterTemp convertMasterToTemp(
            SAPMaster master) {

        SAPMasterTemp temp = new SAPMasterTemp();

        temp.setSrNo(master.getSrNo());
        temp.setUnitCode(master.getUnitCode());
        temp.setCleanUnitId(master.getCleanUnitId());
        temp.setVendorName(master.getVendorName());
        temp.setCcaCallCenter(master.getCcaCallCenter());
        temp.setPan(master.getPan());
        temp.setSapVendorCode(master.getSapVendorCode());
        temp.setState(master.getState());
        temp.setTdsRate(master.getTdsRate());
        temp.setTaxCode(master.getTaxCode());
        temp.setFinaliBoxids(master.getFinaliBoxids());
        temp.setStatusOfBlocking(master.getStatusOfBlocking());
        temp.setAccountStatus(master.getAccountStatus());
        temp.setCredInfoNo(master.getCredInfoNo());
        temp.setGstnNo(master.getGstnNo());
        temp.setSacCode(master.getSacCode());
        temp.setServiceProviderIdStatus(master.getServiceProviderIdStatus());
        temp.setGstApplicable(master.getGstApplicable());
        temp.setHoldStatus(master.getHoldStatus());
        temp.setPaymentMode(master.getPaymentMode());

        return temp;
    }
}