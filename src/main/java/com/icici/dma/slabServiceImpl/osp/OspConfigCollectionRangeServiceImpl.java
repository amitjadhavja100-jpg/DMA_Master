package com.icici.dma.slabServiceImpl.osp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.osp.OspConfigCollectionRangeRequest;
import com.icici.dma.dto.osp.OspConfigCollectionRangeResponse;
import com.icici.dma.dto.osp.OspConfigCompareResponse;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.OspConstants;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.slabEntity.osp.OspConfigCollectionRangeHistory;
import com.icici.dma.slabEntity.osp.OspConfigCollectionRangeMaster;
import com.icici.dma.slabEntity.osp.OspConfigCollectionRangeTemp;
import com.icici.dma.slabEntity.osp.OspConfigDpdMaster;
import com.icici.dma.slabRepository.osp.OspConfigCollectionRangeHistoryRepository;
import com.icici.dma.slabRepository.osp.OspConfigCollectionRangeMasterRepository;
import com.icici.dma.slabRepository.osp.OspConfigCollectionRangeTempRepository;
import com.icici.dma.slabRepository.osp.OspConfigDpdMasterRepository;
import com.icici.dma.slabService.osp.OspConfigCollectionRangeService;


@Service
@Transactional
public class OspConfigCollectionRangeServiceImpl implements OspConfigCollectionRangeService {

    @Autowired
    private OspConfigCollectionRangeMasterRepository masterRepository;

    @Autowired
    private OspConfigCollectionRangeTempRepository tempRepository;

    @Autowired
    private OspConfigCollectionRangeHistoryRepository historyRepository;

    @Autowired
    private OspConfigDpdMasterRepository dpdRepository;

    
    @Override
    public String save(OspConfigCollectionRangeRequest request, String userId) {

        // Validate DPD
        if (request.getDpdId() == null) {
            return "Please select DPD.";
        }

        OspConfigDpdMaster dpd = dpdRepository
                .findByIdAndStatus(
                        request.getDpdId(),
                        StatusConstant.APPROVE)
                .orElse(null);

        if (dpd == null) {
            return "Selected DPD is invalid.";
        }

        // Validate From Amount
        if (request.getFromAmount() == null) {
            return "From Amount is required.";
        }

        // Validate To Amount
//        if (request.getToAmount() == null) {
//            return "To Amount is required.";
//        }
        
        if(!Boolean.TRUE.equals(request.getIsMax())
                && request.getToAmount()==null){
            return "To Amount is required.";
        }

        // Validate Order No
        if (request.getOrderNo() == null) {
            return "Order No is required.";
        }

        // From should be less than To amount
//        if (request.getFromAmount()
//                .compareTo(request.getToAmount()) >= 0) {
//
//            return "From Amount must be less than To Amount.";
//        }
        
        BigDecimal toAmount;

        if (Boolean.TRUE.equals(request.getIsMax())) {
            toAmount = OspConstants.MAX_TO_AMOUNT;
        } else {
            toAmount = request.getToAmount();
        }
        
		if (request.getFromAmount().compareTo(toAmount) >= 0) {
            return "From Amount must be less than To Amount.";
        }

        // Duplicate Order
        if (masterRepository.existsByDpdAndOrderNo(
                dpd,
                request.getOrderNo())) {

            return "Order Number already exists.";
        }

        // Duplicate Range
        if (masterRepository.existsByDpdAndFromAmountAndToAmount(
                dpd,
                request.getFromAmount(),
                toAmount)) {

            return "Collection Range already exists.";
        }

        // Overlap Validation
        if (masterRepository.existsOverlappingRange(
                dpd,
                request.getFromAmount(),
                toAmount)) {

            return "Range overlaps with existing configuration.";
        }

        if (tempRepository.existsByDpdAndOrderNoAndStatus(
                dpd,
                request.getOrderNo(),
                StatusConstant.PENDING)) {

            return "Order Number is already pending for approval.";
        }

        if (tempRepository.existsByDpdAndFromAmountAndToAmountAndStatus(
                dpd,
                request.getFromAmount(),
                toAmount,
                StatusConstant.PENDING)) {

            return "Collection Range is already pending for approval.";
        }
        
		if (Boolean.TRUE.equals(request.getIsMax())) {
			if (masterRepository.existsByDpdAndIsMax(dpd, OspConstants.IS_MAX_Y)) {
				return "MAX Range already exists.";
			}
			if (tempRepository.existsByDpdAndIsMaxAndStatus(dpd, OspConstants.IS_MAX_Y, StatusConstant.PENDING)) {
				return "MAX Range already pending.";
			}
		}
        
        OspConfigCollectionRangeTemp temp = new OspConfigCollectionRangeTemp();

        temp.setDpd(dpd);
        temp.setActionType(ActionConstant.INSERT);

        temp.setFromAmount(request.getFromAmount());        
        temp.setToAmount(toAmount);
        temp.setIsMax(
                Boolean.TRUE.equals(request.getIsMax())
                        ? OspConstants.IS_MAX_Y
                        : OspConstants.IS_MAX_N);
        //request.getToAmount();
        //temp.setToAmount(request.getToAmount());

        temp.setOrderNo(request.getOrderNo());
        temp.setStatus(StatusConstant.PENDING);

        temp.setCreatedBy(userId);
        temp.setCreatedDate(new Date());

        tempRepository.save(temp);

        return "Collection Range sent for approval.";

    }
    
    @Override
    public List<OspConfigCollectionRangeResponse> getApprovedRanges(Long dpdId) {

        OspConfigDpdMaster dpd = dpdRepository
                .findByIdAndStatus(
                        dpdId,
                        StatusConstant.APPROVE)
                .orElse(null);

        if (dpd == null) {
            return new ArrayList<>();
        }

        List<OspConfigCollectionRangeMaster> masterList =
                masterRepository.findByDpdAndStatusOrderByOrderNo(
                        dpd,
                        StatusConstant.APPROVE);

		List<OspConfigCollectionRangeResponse> responseList = new ArrayList<>();

		for (OspConfigCollectionRangeMaster entity : masterList) {

			responseList.add(convertToResponse(entity));

		}

        return responseList;
    }

    @Override
    public List<OspConfigCollectionRangeResponse> getPendingRanges() {

        List<OspConfigCollectionRangeTemp> tempList =
                tempRepository.findByStatusOrderByCreatedDateDesc(
                        StatusConstant.PENDING);

		List<OspConfigCollectionRangeResponse> responseList = new ArrayList<>();

		for (OspConfigCollectionRangeTemp entity : tempList) {
			responseList.add(convertToResponse(entity));

		}

        return responseList;
    }

    @Override
    public String update(OspConfigCollectionRangeRequest request, String userId) {

        if (request.getId() == null) {
            return "Invalid Collection Range Id.";
        }

        OspConfigCollectionRangeMaster master =
                masterRepository.findById(request.getId())
                        .orElse(null);

        if (master == null) {
            return "Collection Range not found.";
        }

        OspConfigDpdMaster dpd = dpdRepository
                .findByIdAndStatus(
                        request.getDpdId(),
                        StatusConstant.APPROVE)
                .orElse(null);

        if (dpd == null) {
            return "Selected DPD is invalid.";
        }

        if (request.getFromAmount() == null) {
            return "From Amount is required.";
        }

        if (!Boolean.TRUE.equals(request.getIsMax())
                && request.getToAmount() == null) {
            return "To Amount is required.";
        }

        if (request.getOrderNo() == null) {
            return "Order No is required.";
        }
        
        BigDecimal toAmount;

        if (Boolean.TRUE.equals(request.getIsMax())) {
            toAmount = OspConstants.MAX_TO_AMOUNT;
        } else {
            toAmount = request.getToAmount();
        }

        if (request.getFromAmount().compareTo(toAmount) >= 0) {
            return "From Amount must be less than To Amount.";
        }

        List<OspConfigCollectionRangeMaster> list =
                masterRepository.findByDpdAndStatusOrderByOrderNo(
                        dpd,
                        StatusConstant.APPROVE);

        for (OspConfigCollectionRangeMaster m : list) {
            System.out.println(
                    "ID=" + m.getId()
                    + ", ORDER=" + m.getOrderNo());
        }

        boolean duplicate = list.stream()
                .anyMatch(m ->
                        !m.getId().equals(request.getId())
                        && m.getOrderNo().equals(request.getOrderNo()));

        if (duplicate) {
            return "Order Number already exists.";
        }
        
//        // Duplicate Order
//        if (masterRepository.existsByDpdAndOrderNoAndIdNot(
//                dpd,
//                request.getOrderNo(),
//                request.getId())) {
//
//            return "Order Number already exists.";
//        }

        // Duplicate Range
        if (masterRepository.existsByDpdAndFromAmountAndToAmountAndIdNot(
                dpd,
                request.getFromAmount(),
                toAmount,
                request.getId())) {

            return "Collection Range already exists.";
        }

        // Overlap
        if (masterRepository.existsOverlappingRangeExcludingId(
                dpd,
                request.getFromAmount(),
                toAmount,
                request.getId())) {

            return "Range overlaps with existing configuration.";
        }
        
        if (Boolean.TRUE.equals(request.getIsMax())) {

            if (!OspConstants.IS_MAX_Y.equals(master.getIsMax())
                    && masterRepository.existsByDpdAndIsMax(
                            dpd,
                            OspConstants.IS_MAX_Y)) {

                return "MAX Range already exists.";
            }

            if (!OspConstants.IS_MAX_Y.equals(master.getIsMax())
                    && tempRepository.existsByDpdAndIsMaxAndStatus(
                            dpd,
                            OspConstants.IS_MAX_Y,
                            StatusConstant.PENDING)) {

                return "MAX Range already pending.";
            }
        }

        // Already Pending
        if (tempRepository.existsByRangeIdAndStatus(
                request.getId(),
                StatusConstant.PENDING)) {

            return "This Collection Range is already pending for approval.";
        }

		OspConfigCollectionRangeTemp temp = new OspConfigCollectionRangeTemp();

        temp.setRangeId(master.getId());
        temp.setDpd(dpd);
        temp.setActionType(ActionConstant.UPDATE);

        temp.setFromAmount(request.getFromAmount());
        //temp.setToAmount(request.getToAmount());
        temp.setToAmount(toAmount);
        temp.setIsMax(
                Boolean.TRUE.equals(request.getIsMax())
                        ? OspConstants.IS_MAX_Y
                        : OspConstants.IS_MAX_N);

        temp.setOrderNo(request.getOrderNo());
        temp.setStatus(StatusConstant.PENDING);

        temp.setCreatedBy(master.getCreatedBy());
        temp.setCreatedDate(master.getCreatedDate());

        temp.setModifiedBy(userId);
        temp.setModifiedDate(new Date());
        tempRepository.save(temp);

        return "Collection Range update sent for approval.";

    }

    @Override
    public String approve(Long tempId,
                          String approvedBy,
                          String remarks) {

        OspConfigCollectionRangeTemp temp = tempRepository.findById(tempId)
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

        return "Collection Range approved successfully.";
    }

    @Override
    public String reject(Long tempId,
                         String approvedBy,
                         String remarks) {

        OspConfigCollectionRangeTemp temp = tempRepository
                .findById(tempId)
                .orElse(null);

        if (temp == null) {
            return "Pending record not found.";
        }

        if (!StatusConstant.PENDING.equals(temp.getStatus())) {
            return "Record is already processed.";
        }

		OspConfigCollectionRangeHistory history = new OspConfigCollectionRangeHistory();

        history.setRangeId(temp.getRangeId());
        history.setDpd(temp.getDpd());
        history.setActionType(temp.getActionType());

        // If UPDATE, capture old values from Master
        if (ActionConstant.UPDATE.equals(temp.getActionType())) {

            OspConfigCollectionRangeMaster master =
                    masterRepository.findById(temp.getRangeId())
                            .orElse(null);

            if (master != null) {

                history.setOldFromAmount(master.getFromAmount());
                history.setOldToAmount(master.getToAmount());
                history.setOldOrderNo(master.getOrderNo());

            }
        }

        // New values from Temp
        history.setNewFromAmount(temp.getFromAmount());
        history.setNewToAmount(temp.getToAmount());
        history.setNewIsMax(temp.getIsMax());
        history.setNewOrderNo(temp.getOrderNo());
        history.setStatus(StatusConstant.REJECTE);
        history.setApprovedBy(approvedBy);
        history.setApprovedDate(new Date());
        history.setRemarks(remarks);

        historyRepository.save(history);

        tempRepository.delete(temp);

        return "Collection Range rejected successfully.";

    }

    @Override
    public OspConfigCollectionRangeResponse getById(Long id) {

        OspConfigCollectionRangeMaster master = masterRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Collection Range not found."));

        return convertToResponse(master);

    }
    
    @Override
    public OspConfigCompareResponse getMakerCompare(Long id){

        OspConfigCompareResponse response = new OspConfigCompareResponse();

        OspConfigCollectionRangeMaster master =
                masterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Range not found"));

        response.setTop(convertToResponse(master));

		OspConfigCollectionRangeHistory history = historyRepository.findTopByRangeIdOrderByApprovedDateDesc(id);

		if (history != null && history.getOldFromAmount() != null) {

			OspConfigCollectionRangeResponse historyResponse = new OspConfigCollectionRangeResponse();

		    historyResponse.setRangeId(history.getRangeId());

		    historyResponse.setDpdId(history.getDpd().getId());
		    historyResponse.setDpdName(history.getDpd().getDpdName());

		    // Previous approved values
		    historyResponse.setFromAmount(history.getOldFromAmount());
		    historyResponse.setToAmount(history.getOldToAmount());
			historyResponse.setIsMax(OspConstants.IS_MAX_Y.equals(history.getOldIsMax()));
		    historyResponse.setOrderNo(history.getOldOrderNo());

		    historyResponse.setStatus(history.getStatus());
		    historyResponse.setActionType(history.getActionType());

		    response.setBottom(historyResponse);

		} else {

		    // First approval - no previous approved record
		    response.setBottom(null);
		}

        return response;
    }
    
    @Override
    public OspConfigCompareResponse getCheckerCompare(Long tempId){

        OspConfigCompareResponse response =
                new OspConfigCompareResponse();

        OspConfigCollectionRangeTemp temp =
                tempRepository.findById(tempId)
                .orElseThrow(() ->
                        new RuntimeException("Pending record not found"));

        response.setTop(convertToResponse(temp));
        
     // INSERT- No approved record exists
        if (ActionConstant.INSERT.equals(temp.getActionType())) {
            response.setBottom(null);
        } else {
            OspConfigCollectionRangeMaster master =
                    masterRepository.findById(temp.getRangeId())
                            .orElse(null);

            if (master != null) {
                response.setBottom(convertToResponse(master));
            }
        }
        return response;
    }
    
    private OspConfigCollectionRangeResponse convertToResponse(
            OspConfigCollectionRangeMaster entity) {

        OspConfigCollectionRangeResponse response =
                new OspConfigCollectionRangeResponse();

        response.setId(entity.getId());
        response.setDpdId(entity.getDpd().getId());
        response.setDpdName(entity.getDpd().getDpdName());

        response.setFromAmount(entity.getFromAmount());
        response.setToAmount(entity.getToAmount());
        response.setIsMax(
                OspConstants.IS_MAX_Y.equals(entity.getIsMax()));

        response.setOrderNo(entity.getOrderNo());
        response.setStatus(entity.getStatus());

        return response;
    }
	
    private OspConfigCollectionRangeResponse convertToResponse(
            OspConfigCollectionRangeTemp entity) {

        OspConfigCollectionRangeResponse response =
                new OspConfigCollectionRangeResponse();

        response.setId(entity.getTempId());

        response.setRangeId(entity.getRangeId());
        response.setDpdId(entity.getDpd().getId());
        response.setDpdName(entity.getDpd().getDpdName());

        response.setFromAmount(entity.getFromAmount());
        response.setToAmount(entity.getToAmount());
        response.setIsMax(
                OspConstants.IS_MAX_Y.equals(entity.getIsMax()));

        response.setOrderNo(entity.getOrderNo());
        response.setStatus(entity.getStatus());

        response.setActionType(entity.getActionType());

        return response;
    }
    
	private void approveInsert(OspConfigCollectionRangeTemp temp, String approvedBy, String remarks) {

		OspConfigCollectionRangeMaster master = new OspConfigCollectionRangeMaster();

		master.setDpd(temp.getDpd());

		master.setFromAmount(temp.getFromAmount());
		master.setToAmount(temp.getToAmount());
		master.setIsMax(temp.getIsMax());
		master.setOrderNo(temp.getOrderNo());

		master.setStatus(StatusConstant.APPROVE);

		master.setCreatedBy(temp.getCreatedBy());
		master.setCreatedDate(temp.getCreatedDate());

		master.setUpdatedBy(approvedBy);
		master.setUpdatedDate(new Date());

		masterRepository.save(master);

		OspConfigCollectionRangeHistory history = new OspConfigCollectionRangeHistory();

		history.setRangeId(master.getId());
		history.setDpd(master.getDpd());
		history.setActionType(ActionConstant.INSERT);

		history.setNewFromAmount(master.getFromAmount());
		history.setNewToAmount(master.getToAmount());
		history.setNewIsMax(master.getIsMax());
		history.setNewOrderNo(master.getOrderNo());
		history.setStatus(StatusConstant.APPROVE);

		history.setApprovedBy(approvedBy);
		history.setApprovedDate(new Date());

		history.setRemarks(remarks);

		historyRepository.save(history);

	}
	
	private void approveUpdate(OspConfigCollectionRangeTemp temp, String approvedBy, String remarks) {

		OspConfigCollectionRangeMaster master = masterRepository.findById(temp.getRangeId())
				.orElseThrow(() -> new RuntimeException("Collection Range not found"));

		OspConfigCollectionRangeHistory history = new OspConfigCollectionRangeHistory();

		history.setRangeId(master.getId());
		history.setDpd(master.getDpd());
		history.setActionType(ActionConstant.UPDATE);

		history.setOldFromAmount(master.getFromAmount());
		history.setOldToAmount(master.getToAmount());
		history.setOldIsMax(master.getIsMax());
		history.setOldOrderNo(master.getOrderNo());

		history.setNewFromAmount(temp.getFromAmount());
		history.setNewToAmount(temp.getToAmount());
		history.setNewIsMax(temp.getIsMax());
		history.setNewOrderNo(temp.getOrderNo());

		history.setStatus(StatusConstant.APPROVE);

		history.setApprovedBy(approvedBy);
		history.setApprovedDate(new Date());
		history.setRemarks(remarks);

		historyRepository.save(history);

		master.setDpd(temp.getDpd());
		master.setFromAmount(temp.getFromAmount());
		master.setToAmount(temp.getToAmount());
		master.setIsMax(temp.getIsMax());

		master.setOrderNo(temp.getOrderNo());

		master.setUpdatedBy(approvedBy);
		master.setUpdatedDate(new Date());

		masterRepository.save(master);

	}
	
	

}