package com.icici.dma.slabServiceImpl.osp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icici.dma.dto.osp.OspConfigCompareResponse;
import com.icici.dma.dto.osp.OspConfigPendingResponse;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.slabEntity.osp.OspConfigCollectionRangeMaster;
import com.icici.dma.slabEntity.osp.OspConfigCollectionRangeTemp;
import com.icici.dma.slabEntity.osp.OspConfigDpdMaster;
import com.icici.dma.slabEntity.osp.OspConfigDpdTemp;
import com.icici.dma.slabRepository.osp.OspConfigCollectionRangeMasterRepository;
import com.icici.dma.slabRepository.osp.OspConfigCollectionRangeTempRepository;
import com.icici.dma.slabRepository.osp.OspConfigDpdMasterRepository;
import com.icici.dma.slabRepository.osp.OspConfigDpdTempRepository;
import com.icici.dma.slabService.osp.OspConfigCheckerService;
import com.icici.dma.slabService.osp.OspConfigCollectionRangeService;
import com.icici.dma.slabService.osp.OspConfigDpdService;

@Service
public class OspConfigCheckerServiceImpl implements OspConfigCheckerService {

	@Autowired
	private OspConfigDpdTempRepository dpdTempRepository;
	
	@Autowired
	private OspConfigCollectionRangeTempRepository rangeTempRepository;
	
	@Autowired
	private OspConfigDpdService dpdService;

	@Autowired
	private OspConfigCollectionRangeService rangeService;
	
	@Autowired
	private OspConfigDpdMasterRepository dpdMasterRepository;

	@Autowired
	private OspConfigCollectionRangeMasterRepository rangeMasterRepository;
	
	
	@Override
	public List<OspConfigPendingResponse> getPending(String userId) {

		List<OspConfigPendingResponse> list = new ArrayList<>();

		// DPD
//		List<OspConfigDpdTemp> dpds = dpdTempRepository.findByStatusOrderByCreatedDateDesc(StatusConstant.PENDING);
		
		List<OspConfigDpdTemp> dpds =
		        dpdTempRepository
		        .findByStatusAndCreatedByNotOrderByCreatedDateDesc(
		                StatusConstant.PENDING,
		                userId);

		for (OspConfigDpdTemp d : dpds) {

			OspConfigPendingResponse r = new OspConfigPendingResponse();

			r.setTempId(d.getTempId());
			r.setEntityId(d.getDpdId());
			
			r.setType("DPD");
			r.setDpdName(d.getDpdName());
			r.setKey(d.getDpdName());
			
			if (ActionConstant.UPDATE.equals(d.getActionType())
			        && d.getDpdId() != null) {

			    OspConfigDpdMaster master =
			            dpdMasterRepository.findById(d.getDpdId()).orElse(null);

			    if (master != null) {
			        r.setOldValue(master.getDpdName());
			    }
			} else{
			    r.setOldValue("-");
			}

			r.setNewValue(d.getDpdName());
			
			r.setActionType(d.getActionType());
			r.setMakerId(d.getCreatedBy());
			r.setStatus(d.getStatus());
			list.add(r);
		}

		// RANGE
//		List<OspConfigCollectionRangeTemp> ranges = rangeTempRepository
//				.findByStatusOrderByCreatedDateDesc(StatusConstant.PENDING);

		List<OspConfigCollectionRangeTemp> ranges =
		        rangeTempRepository
		        .findByStatusAndCreatedByNotOrderByCreatedDateDesc(
		                StatusConstant.PENDING,
		                userId);
		
		for (OspConfigCollectionRangeTemp rTemp : ranges) {

			OspConfigPendingResponse r = new OspConfigPendingResponse();

			r.setTempId(rTemp.getTempId());
			r.setEntityId(rTemp.getRangeId());
			r.setType("COLLECTION_RANGE");
			
			r.setDpdName(rTemp.getDpd().getDpdName());
			//r.setKey(rTemp.getFromAmount() + " - " + rTemp.getToAmount());
			
			String newTo =
			        "Y".equals(rTemp.getIsMax())
			        ? "MAX"
			        : rTemp.getToAmount().toPlainString();

			r.setKey(
			        rTemp.getFromAmount().toPlainString()
			        + " - "
			        + newTo);
			
			if (ActionConstant.UPDATE.equals(rTemp.getActionType())
			        && rTemp.getRangeId() != null) {

			    OspConfigCollectionRangeMaster master =
			            rangeMasterRepository.findById(rTemp.getRangeId()).orElse(null);

			    if (master != null) {
			    	String oldTo =
			    	        "Y".equals(master.getIsMax())
			    	        ? "MAX"
			    	        : master.getToAmount().toPlainString();

			    	r.setOldValue(
			    	        master.getFromAmount().toPlainString()
			    	        + " - "
			    	        + oldTo);
//			        r.setOldValue(
//			                master.getFromAmount()
//			                + " - "
//			                + master.getToAmount());
			    }
			} else{
			    r.setOldValue("-");
			}
			
			r.setNewValue(
			        rTemp.getFromAmount().toPlainString()
			        + " - "
			        + newTo);
//			r.setNewValue(
//			        rTemp.getFromAmount()
//			        + " - "
//			        + rTemp.getToAmount());
//			
			r.setActionType(rTemp.getActionType());
			r.setMakerId(rTemp.getCreatedBy());
			r.setStatus(rTemp.getStatus());
			list.add(r);
		}
		list.sort((a, b) ->
		b.getTempId().compareTo(a.getTempId()));
		return list;

	}
	
	
	@Override
	public String approve(Long tempId, String type, String userId, String remarks) {

		if ("DPD".equals(type)) {
			return dpdService.approve(tempId, userId, remarks);
		}
		return rangeService.approve(tempId, userId, remarks);
	}
	
	@Override
	public String reject(Long tempId, String type, String userId, String remarks) {

		if ("DPD".equals(type)) {
			return dpdService.reject(tempId, userId, remarks);
		}
		return rangeService.reject(tempId, userId, remarks);

	}
	
	@Override
	public OspConfigCompareResponse makerCompare(Long id, String type){

	    if("DPD".equals(type)){
	        return dpdService.getMakerCompare(id);
	    }
	    return rangeService.getMakerCompare(id);

	}
	
	@Override
	public OspConfigCompareResponse checkerCompare(Long tempId, String type){
	    
		if("DPD".equals(type)){
	        return dpdService.getCheckerCompare(tempId);
	    }
	    return rangeService.getCheckerCompare(tempId);
	}
	
	
}

