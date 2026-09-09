package com.icici.dma.serviceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.RecoveryCityDto;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.RecoveryCityMaster;
import com.icici.dma.model.RecoveryCityMasterTemp;
import com.icici.dma.repository.RecoveryCityMasterRepository;
import com.icici.dma.repository.RecoveryCityMasterTempRepository;
import com.icici.dma.service.RecoveryCityService;

@Service
@Transactional
public class RecoveryCityServiceImpl implements RecoveryCityService {

    @Autowired
    private RecoveryCityMasterRepository recoveryCityRepo;

    @Autowired
    private RecoveryCityMasterTempRepository recoveryCityTempRepo;

    @Override
    public List<RecoveryCityDto> getRecoveryCityMaker() {

        List<RecoveryCityDto> dtoList = new ArrayList<>();

        List<RecoveryCityMaster> approvedList = recoveryCityRepo.findAllByStatus(StatusConstant.APPROVE);

        if (approvedList != null && !approvedList.isEmpty()) {
            approvedList.forEach(record ->
                    dtoList.add(convertMasterToDto(record)));
        }

		List<RecoveryCityMasterTemp> pendingList = recoveryCityTempRepo.findAllByStatus(StatusConstant.PENDING);

        if (pendingList != null && !pendingList.isEmpty()) {
            pendingList.forEach(record ->
                    dtoList.add(convertTempToDto(record)));
        }

        if (dtoList.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No Recovery City records found");
        }
        
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

        return dtoList;
    }

    @Override
    public void createRecoveryCity(
            RecoveryCityDto dto,
            String username) {

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Request body is null");
        }

        if (dto.getCity() == null) {
            throw new IllegalArgumentException(
                    "ID is mandatory");
        }

        if (recoveryCityRepo.existsById(dto.getCity())) {
            throw new IllegalArgumentException(
                    "Record already approved");
        }

        if (recoveryCityTempRepo.existsByCityAndStatus(
                dto.getCity(),
                StatusConstant.PENDING)) {

            throw new IllegalArgumentException(
                    "Approval already pending");
        }

        RecoveryCityMasterTemp temp =
                convertDtoToTemp(dto);

        temp.setStatus(StatusConstant.PENDING);
        temp.setCreatedBy(username);
        temp.setCreatedDate(new Date());

        temp.setActionType(ActionConstant.INSERT);
        temp.setActionDate(new Date());
        temp.setActionUser(username);

        recoveryCityTempRepo.save(temp);
    }

    @Override
    public void updateRecoveryCityByMaker(
            RecoveryCityDto dto,
            String username) {

        if (dto == null || dto.getCity() == null) {
            throw new IllegalArgumentException(
                    "CITY is mandatory");
        }

        /*
         * Pending record exists
         */
        if (recoveryCityTempRepo.existsByCityAndStatus(
                dto.getCity(),
                StatusConstant.PENDING)) {

            throw new IllegalArgumentException(
                    "Already waiting for approval");
        }

        /*
         * Rejected record exists
         */
        if (recoveryCityTempRepo.existsByCityAndStatus(
                dto.getCity(),
                StatusConstant.REJECTE)) {

            throw new IllegalArgumentException(
                    "Already Rejected");
        }

        /*
         * Fetch approved record from MASTER
         */
        RecoveryCityMaster master =
                recoveryCityRepo.findById(dto.getCity())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Approved record not found"));

        /*
         * Create TEMP record
         */
        RecoveryCityMasterTemp temp =
                new RecoveryCityMasterTemp();

        temp.setCity(master.getCity());
        temp.setBranchName(master.getBranchName());
        temp.setMainBranch(master.getMainBranch());
        temp.setZone(master.getZone());
        temp.setExistingCategory(master.getExistingCategory());
        temp.setCataegory181360(master.getCataegory181360());
        temp.setZoneCode(master.getZoneCode());

        temp.setCreatedBy(username);
        temp.setCreatedDate(new Date());

        temp.setModifiedBy(master.getModifiedBy());
        temp.setModifiedDate(master.getModifiedDate());

        temp.setStatus(StatusConstant.PENDING);

        temp.setActionType(ActionConstant.UPDATE);

        temp.setActionDate(new Date());

        temp.setActionUser(username);

        if (dto.getCity() != null) {
            temp.setCity(dto.getCity());
        }

        if (dto.getBranchName() != null) {
            temp.setBranchName(dto.getBranchName());
        }

        if (dto.getMainBranch() != null) {
            temp.setMainBranch(dto.getMainBranch());
        }

        if (dto.getZone() != null) {
            temp.setZone(dto.getZone());
        }

        if (dto.getExistingCategory() != null) {
            temp.setExistingCategory(
                    dto.getExistingCategory());
        }

        if (dto.getCataegory181360() != null) {
            temp.setCataegory181360(
                    dto.getCataegory181360());
        }

        if (dto.getZoneCode() != null) {
            temp.setZoneCode(dto.getZoneCode());
        }

        temp.setModifiedBy(username);
        temp.setModifiedDate(new Date());

        recoveryCityTempRepo.save(temp);
    }

    @Override
    public List<RecoveryCityDto> getAllRecoveryCityChecker(
            String user) {

        List<RecoveryCityMasterTemp> list =
                recoveryCityTempRepo.findAllByStatusAndCreatedByNot(
                        StatusConstant.PENDING,
                        user.toUpperCase());

        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No pending Recovery City records");
        }

        return list.stream()
                .map(this::convertTempToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateRecoveryCityByChecker(
            CheckerDecisionReq requestPayload,
            String username) {

        if (requestPayload == null) {
            throw new IllegalArgumentException(
                    "Request payload is null");
        }

        String decision = requestPayload.getDecision();

        if (decision == null || decision.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Decision is mandatory");
        }

        decision = decision.toUpperCase();

        if (!StatusConstant.APPROVE.equals(decision)
                && !StatusConstant.REJECTE.equals(decision)) {

            throw new IllegalArgumentException(
                    "Invalid decision. Use A or R");
        }

        List<String> ids = requestPayload.getPrimaryIds();

        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException(
                    "No IDs selected");
        }

        List<String> validCities = ids.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        List<String> pendingCities = new ArrayList<>();

        for (int i = 0; i < validCities.size(); i += 500) {

            List<String> batch =
                    validCities.subList(
                            i,
                            Math.min(i + 500, validCities.size()));

            pendingCities.addAll(
                    recoveryCityTempRepo.findPendingCities(
                            batch,
                            StatusConstant.PENDING));
        }

        if (pendingCities.size() != validCities.size()) {

            List<String> missing =
                    new ArrayList<>(validCities);

            missing.removeAll(pendingCities);

            throw new ResourceNotFoundException(
                    "Pending records not found: " + missing);
        }

        if (StatusConstant.APPROVE.equals(decision)) {

        	List<RecoveryCityMasterTemp> tempRecords =
        	        new ArrayList<>();

        	for (int i = 0; i < pendingCities.size(); i += 500) {

        	    List<String> batch =
        	            pendingCities.subList(
        	                    i,
        	                    Math.min(i + 500, pendingCities.size()));

        	    tempRecords.addAll(
        	            recoveryCityTempRepo.findAllById(batch));
        	}
        	
            List<RecoveryCityMaster> approvedRecords =
                    tempRecords.stream()
                            .map(this::convertTempToMaster)
                            .collect(Collectors.toList());

            recoveryCityRepo.saveAll(approvedRecords);
        }

        if (StatusConstant.APPROVE.equals(decision)) {

            for (int i = 0; i < pendingCities.size(); i += 500) {

                List<String> batch =
                        pendingCities.subList(
                                i,
                                Math.min(i + 500, pendingCities.size()));

                recoveryCityTempRepo.deleteApprovedRecords(batch);
            }
        }
        else {

            for (int i = 0; i < pendingCities.size(); i += 500) {

                List<String> batch =
                        pendingCities.subList(
                                i,
                                Math.min(i + 500, pendingCities.size()));

                recoveryCityTempRepo.bulkUpdateStatus(
                        batch,
                        StatusConstant.REJECTE,
                        requestPayload.getRemark(),
                        StatusConstant.PENDING);
            }
        }
    }

    @Override
    public List<RecoveryCityDto> getRecoveryCityByStatus(
            String statusType) {

        List<RecoveryCityDto> dtoList =
                new ArrayList<>();

        if (StatusConstant.ALL.equalsIgnoreCase(statusType)) {

            // Approved
            recoveryCityRepo.findAllByStatus(StatusConstant.APPROVE)
                    .forEach(e ->
                            dtoList.add(convertMasterToDto(e)));

            // Pending only
            recoveryCityTempRepo.findAllByStatus(StatusConstant.PENDING)
                    .forEach(e ->
                            dtoList.add(convertTempToDto(e)));

            // Same sorting as SAP
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

            recoveryCityRepo.findAllByStatus(StatusConstant.APPROVE)
                    .forEach(e ->
                            dtoList.add(convertMasterToDto(e)));

        } 
        else if (StatusConstant.PENDING.equalsIgnoreCase(statusType)) {

            recoveryCityTempRepo.findAllByStatus(
                    StatusConstant.PENDING)
                    .forEach(e ->
                            dtoList.add(convertTempToDto(e)));

        } 
        else if (StatusConstant.REJECTE.equalsIgnoreCase(statusType)) {

            recoveryCityTempRepo.findAllByStatus(
                    StatusConstant.REJECTE)
                    .forEach(e ->
                            dtoList.add(convertTempToDto(e)));

        } 
        else {
            throw new IllegalArgumentException("Invalid status");
        }

        if (dtoList.isEmpty()) {
            return Collections.emptyList();

        }

        return dtoList;
    }

    private RecoveryCityDto convertMasterToDto(
            RecoveryCityMaster entity) {

        RecoveryCityDto dto =
                new RecoveryCityDto();

        dto.setCity(entity.getCity());
        dto.setBranchName(entity.getBranchName());
        dto.setMainBranch(entity.getMainBranch());
        dto.setZone(entity.getZone());
        dto.setExistingCategory(
                entity.getExistingCategory());
        dto.setCataegory181360(
                entity.getCataegory181360());
        dto.setZoneCode(entity.getZoneCode());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setModifiedBy(entity.getModifiedBy());
        dto.setModifiedDate(entity.getModifiedDate());
        dto.setStatus(entity.getStatus());

        return dto;
    }

    private RecoveryCityDto convertTempToDto(
            RecoveryCityMasterTemp entity) {

        RecoveryCityDto dto =
                new RecoveryCityDto();

        dto.setCity(entity.getCity());
        dto.setBranchName(entity.getBranchName());
        dto.setMainBranch(entity.getMainBranch());
        dto.setZone(entity.getZone());
        dto.setExistingCategory(
                entity.getExistingCategory());
        dto.setCataegory181360(
                entity.getCataegory181360());
        dto.setZoneCode(entity.getZoneCode());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setModifiedBy(entity.getModifiedBy());
        dto.setModifiedDate(entity.getModifiedDate());
        dto.setRemarks(entity.getRemarks());
        dto.setStatus(entity.getStatus());
        dto.setActionType(entity.getActionType());
        dto.setActionDate(entity.getActionDate());
        dto.setActionUser(entity.getActionUser());

        return dto;
    }

    private RecoveryCityMasterTemp convertDtoToTemp(
            RecoveryCityDto dto) {

        RecoveryCityMasterTemp temp =
                new RecoveryCityMasterTemp();

        temp.setCity(dto.getCity());
        temp.setBranchName(dto.getBranchName());
        temp.setMainBranch(dto.getMainBranch());
        temp.setZone(dto.getZone());
        temp.setExistingCategory(
                dto.getExistingCategory());
        temp.setCataegory181360(
                dto.getCataegory181360());
        temp.setZoneCode(dto.getZoneCode());

        return temp;
    }

    private RecoveryCityMaster convertTempToMaster(
            RecoveryCityMasterTemp temp) {

        RecoveryCityMaster master;

        //Existing Approved Record
        Optional<RecoveryCityMaster> existing =
                recoveryCityRepo.findById(temp.getCity());

        if (existing.isPresent()) {

            master = existing.get();

            //Preserve original creation details
            master.setCreatedBy(
                    existing.get().getCreatedBy());

            master.setCreatedDate(
                    existing.get().getCreatedDate());

            //Update modification details
            master.setModifiedBy(
                    temp.getActionUser());

            master.setModifiedDate(
                    new Date());

        } else {

            // New Insert
            master = new RecoveryCityMaster();

            master.setCreatedBy(
                    temp.getCreatedBy());

            master.setCreatedDate(
                    temp.getCreatedDate());

            master.setModifiedBy(null);

            master.setModifiedDate(null);
        }

        master.setCity(temp.getCity());
        master.setBranchName(temp.getBranchName());
        master.setMainBranch(temp.getMainBranch());
        master.setZone(temp.getZone());
        master.setExistingCategory(
                temp.getExistingCategory());
        master.setCataegory181360(
                temp.getCataegory181360());
        master.setZoneCode(temp.getZoneCode());

        master.setStatus(StatusConstant.APPROVE);

        return master;
    }
}