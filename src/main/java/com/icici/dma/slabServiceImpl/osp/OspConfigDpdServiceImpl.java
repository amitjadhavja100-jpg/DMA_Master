package com.icici.dma.slabServiceImpl.osp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.osp.OspConfigCompareResponse;
import com.icici.dma.dto.osp.OspConfigDpdRequest;
import com.icici.dma.dto.osp.OspConfigDpdResponse;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.slabEntity.osp.OspConfigDpdHistory;
import com.icici.dma.slabEntity.osp.OspConfigDpdMaster;
import com.icici.dma.slabEntity.osp.OspConfigDpdTemp;
import com.icici.dma.slabRepository.osp.OspConfigDpdHistoryRepository;
import com.icici.dma.slabRepository.osp.OspConfigDpdMasterRepository;
import com.icici.dma.slabRepository.osp.OspConfigDpdTempRepository;
import com.icici.dma.slabService.osp.OspConfigDpdService;



@Service
@Transactional
public class OspConfigDpdServiceImpl implements OspConfigDpdService {

    @Autowired
    private OspConfigDpdMasterRepository masterRepository;

    @Autowired
    private OspConfigDpdTempRepository tempRepository;

    @Autowired
    private OspConfigDpdHistoryRepository historyRepository;

    
    @Override
    public String save(OspConfigDpdRequest request,  String userId) {

        // Validate DPD Name
        if (request.getDpdName() == null || request.getDpdName().trim().isEmpty()) {
            return "DPD Name is required.";
        }

     // Duplicate DPD in Master
        OspConfigDpdMaster existing =
                masterRepository.findByDpdNameIgnoreCase(
                        request.getDpdName().trim())
                        .orElse(null);

        if (existing != null) {
            return "DPD '" + existing.getDpdName()
                    + "' already exists.";
        }
//        // Duplicate DPD in Master
//        if (masterRepository.existsByDpdNameIgnoreCase(request.getDpdName().trim())) {
//            return "DPD Name already exists.";
//        }	

        
        // Duplicate Pending DPD
        if (tempRepository.existsByDpdNameIgnoreCaseAndStatus(
                request.getDpdName().trim(),
                StatusConstant.PENDING)) {

            return "DPD is already pending for approval.";
        }

        OspConfigDpdTemp temp = convertToTemp(request);

        temp.setActionType(ActionConstant.INSERT);
		temp.setStatus(StatusConstant.PENDING);

		temp.setCreatedBy(userId);
		temp.setCreatedDate(new Date());

        tempRepository.save(temp);
        
        return "DPD saved successfully and sent for approval.";
    }
    
    @Override
    public String update(OspConfigDpdRequest request,  String userId) {

        if (request.getId() == null) {
            return "Invalid DPD Id.";
        }

        OspConfigDpdMaster master = masterRepository.findById(request.getId())
                .orElse(null);

		if (master == null) {
			return "DPD not found.";
		}

		// Validate Name
		if (request.getDpdName() == null || request.getDpdName().trim().isEmpty()) {

			return "DPD Name is required.";
		}

		// Duplicate Name
		if (masterRepository.existsByDpdNameIgnoreCaseAndIdNot(request.getDpdName().trim(), request.getId())) {

			return "DPD Name already exists.";
		}

		// Already Pending
		if (tempRepository.existsByDpdIdAndStatus(request.getId(), StatusConstant.PENDING)) {

			return "This DPD is already pending for approval.";
		}

        OspConfigDpdTemp temp = new OspConfigDpdTemp();

        temp.setDpdId(master.getId());
        temp.setDpdName(request.getDpdName().trim());

        temp.setActionType(ActionConstant.UPDATE);
        temp.setStatus(StatusConstant.PENDING);

        temp.setCreatedBy(master.getCreatedBy());
        temp.setCreatedDate(master.getCreatedDate());

        temp.setModifiedBy(userId);
        temp.setModifiedDate(new Date());

        tempRepository.save(temp);

        return "DPD update sent for approval.";

    }
    
    @Override
    public List<OspConfigDpdResponse> getApprovedDpds() {

        List<OspConfigDpdMaster> dpdList =
                masterRepository.findByStatusOrderByCreatedDateDesc(
                        StatusConstant.APPROVE);

        List<OspConfigDpdResponse> responseList = new ArrayList<>();

        for (OspConfigDpdMaster entity : dpdList) {
            responseList.add(convertToResponse(entity));
        }

        return responseList;

    }

    @Override
    public List<OspConfigDpdResponse> getPendingDpds() {

        List<OspConfigDpdTemp> tempList =
                tempRepository.findByStatusOrderByCreatedDateDesc(
                        StatusConstant.PENDING);

        List<OspConfigDpdResponse> responseList = new ArrayList<>();

        for (OspConfigDpdTemp entity : tempList) {
            responseList.add(convertToResponse(entity));
        }

        return responseList;

    }
    
    @Override
    public String approve(Long tempId,
                          String approvedBy,
                          String remarks) {

        OspConfigDpdTemp temp = tempRepository.findById(tempId)
                .orElse(null);

        if (temp == null) {
            return "Pending record not found.";
        }

        if (!StatusConstant.PENDING.equals(temp.getStatus())) {
            return "Record is already processed.";
        }

        if (ActionConstant.INSERT.equals(temp.getActionType())) {
            approveInsert(temp, approvedBy, remarks);

        } else if (ActionConstant.UPDATE.equals(temp.getActionType())) {
            approveUpdate(temp, approvedBy, remarks);

        } else {
            return "Invalid Action Type.";

        }
        tempRepository.delete(temp);

        return "DPD approved successfully.";

    }

    @Override
    public String reject(Long tempId,
                         String approvedBy,
                         String remarks) {

        OspConfigDpdTemp temp = tempRepository.findById(tempId)
                .orElse(null);

        if (temp == null) {
            return "Pending record not found.";
        }

        if (!StatusConstant.PENDING.equals(temp.getStatus())) {
            return "Record is already processed.";
        }

        OspConfigDpdHistory history = new OspConfigDpdHistory();

        history.setDpdId(temp.getDpdId());

        history.setActionType(temp.getActionType());

        // If it is an UPDATE request, capture old values from Master
        if (ActionConstant.UPDATE.equals(temp.getActionType())) {

            OspConfigDpdMaster master = masterRepository.findById(temp.getDpdId())
                    .orElse(null);

            if (master != null) {
                history.setOldDpdName(master.getDpdName());
            }
        }

        // New values from Temp
        history.setNewDpdName(temp.getDpdName());
        history.setStatus(StatusConstant.REJECTE);

        history.setApprovedBy(approvedBy);
        history.setApprovedDate(new Date());

        history.setRemarks(remarks);

        historyRepository.save(history);

        tempRepository.delete(temp);

        return "DPD rejected successfully.";

    }

    @Override
    public OspConfigDpdResponse getById(Long id) {

        OspConfigDpdMaster master = masterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("DPD not found."));

        return convertToResponse(master);

    }
    
    @Override
    public OspConfigCompareResponse getMakerCompare(Long id){

        OspConfigCompareResponse response = new OspConfigCompareResponse();

        OspConfigDpdMaster master =
                masterRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("DPD not found"));

        response.setTop(master);

        OspConfigDpdHistory history =
                historyRepository
                .findTopByDpdIdOrderByApprovedDateDesc(id);

        response.setBottom(history);

        return response;

    }
    
    @Override
    public OspConfigCompareResponse getCheckerCompare(Long tempId){

        OspConfigCompareResponse response = new OspConfigCompareResponse();

        OspConfigDpdTemp temp =
                tempRepository.findById(tempId)
                .orElseThrow(() ->
                    new RuntimeException("Pending record not found"));

        response.setTop(temp);

        if (ActionConstant.INSERT.equals(temp.getActionType())) {
            response.setBottom(null);
        } else {
            OspConfigDpdMaster master =
                    masterRepository.findById(temp.getDpdId())
                            .orElse(null);

            response.setBottom(master);
        }

        return response;

    }
    
    
    private OspConfigDpdResponse convertToResponse(OspConfigDpdMaster entity) {

        OspConfigDpdResponse response = new OspConfigDpdResponse();

        response.setId(entity.getId());
        response.setDpdName(entity.getDpdName());
        response.setStatus(entity.getStatus());

        return response;
    }
    
    private OspConfigDpdResponse convertToResponse(OspConfigDpdTemp entity) {

        OspConfigDpdResponse response = new OspConfigDpdResponse();

        response.setId(entity.getTempId());
        response.setDpdName(entity.getDpdName());
        response.setStatus(entity.getStatus());

        return response;
    }
    
    private OspConfigDpdTemp convertToTemp(OspConfigDpdRequest request) {

        OspConfigDpdTemp temp = new OspConfigDpdTemp();

        temp.setDpdId(request.getId());
        temp.setDpdName(request.getDpdName());

        return temp;
    }

    
	private void approveInsert(OspConfigDpdTemp temp, String approvedBy, String remarks) {
		
		OspConfigDpdMaster existing =
	            masterRepository.findByDpdNameIgnoreCase(
	                    temp.getDpdName().trim())
	                    .orElse(null);

	    if (existing != null) {
	        throw new RuntimeException(
	                "DPD '" + existing.getDpdName()
	                + "' already exists.");
	    }

		OspConfigDpdMaster master = new OspConfigDpdMaster();

		master.setDpdName(temp.getDpdName());
		master.setStatus(StatusConstant.APPROVE);

		master.setCreatedBy(temp.getCreatedBy());
		master.setCreatedDate(temp.getCreatedDate());

		master.setUpdatedBy(approvedBy);
		master.setUpdatedDate(new Date());

		masterRepository.save(master);

		OspConfigDpdHistory history = new OspConfigDpdHistory();

		history.setDpdId(master.getId());
		history.setNewDpdName(master.getDpdName());
		
		history.setActionType(ActionConstant.INSERT);
		history.setStatus(StatusConstant.APPROVE);

		history.setApprovedBy(approvedBy);
		history.setApprovedDate(new Date());

		history.setRemarks(remarks);

		historyRepository.save(history);

	}
	
	private void approveUpdate(OspConfigDpdTemp temp, String approvedBy, String remarks) {

		OspConfigDpdMaster master = masterRepository.findById(temp.getDpdId())
				.orElseThrow(() -> new RuntimeException("Master record not found"));

		OspConfigDpdHistory history = new OspConfigDpdHistory();

		history.setDpdId(master.getId());
		history.setActionType(ActionConstant.UPDATE);

		history.setOldDpdName(master.getDpdName());
		history.setNewDpdName(temp.getDpdName());

		history.setStatus(StatusConstant.APPROVE);
		history.setApprovedBy(approvedBy);
		history.setApprovedDate(new Date());

		history.setRemarks(remarks);

		historyRepository.save(history);

		master.setDpdName(temp.getDpdName());
		master.setUpdatedBy(approvedBy);
		master.setUpdatedDate(new Date());

		masterRepository.save(master);

	}
	
}