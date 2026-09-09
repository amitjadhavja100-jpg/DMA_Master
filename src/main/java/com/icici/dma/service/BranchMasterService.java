package com.icici.dma.service;

import java.util.List;

import com.icici.dma.dto.BranchMasterDto;
import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.model.BranchMaster;

public interface BranchMasterService {

	public List<BranchMasterDto> getAllBranchMastermaker();

	public void createBranchMasterbyMaker(BranchMasterDto branchMasterDto, String username);

	public void updateBranchMasterbyMaker(BranchMasterDto branchMasterDto, String username);

	public void updatebranchMasterByChecker(CheckerDecisionReq requestPayload, String username);

	public List<BranchMasterDto> getAllcheckerBranchMaster(String user);
	
	public List<BranchMasterDto> getBranchMasterByStatus(String statusType);

	default BranchMasterDto convertbranchMasterToDto(BranchMaster e) {

		BranchMasterDto d = new BranchMasterDto();

		d.setBranchCode(e.getBranchCode());
		d.setBranchName(e.getBranchName());
		d.setHub(e.getHub());
		d.setaLState(e.getaLState());

		d.setZone(e.getZone());
		d.setrBH(e.getrBH());
		d.seteDState(e.geteDState());
		d.seteDZone(e.geteDZone());

		d.setzHName(e.getzHName());
		d.setStateHead(e.getStateHead());
		d.setmISState(e.getmISState());
		d.setrCState(e.getrCState());

		d.setLocation(e.getLocation());
		d.setStatus(e.getStatus());

		d.setCreatedBy(e.getCreatedBy());
		d.setCreatedDate(e.getCreatedDate());
		d.setModifiedBy(e.getModifiedBy());
		d.setModifiedDate(e.getModifiedDate());

//		d.setRemark(e.getRemark());

		return d;
	}

	default BranchMaster convertDtoToBranchMaster(BranchMasterDto e) {

		BranchMaster d = new BranchMaster();

		d.setBranchCode(e.getBranchCode());
		d.setBranchName(e.getBranchName());
		d.setHub(e.getHub());
		d.setaLState(e.getaLState());

		d.setZone(e.getZone());
		d.setrBH(e.getrBH());
		d.seteDState(e.geteDState());
		d.seteDZone(e.geteDZone());

		d.setzHName(e.getzHName());
		d.setStateHead(e.getStateHead());
		d.setmISState(e.getmISState());
		d.setrCState(e.getrCState());

		d.setLocation(e.getLocation());
		d.setStatus(e.getStatus());

		d.setCreatedBy(e.getCreatedBy());
		d.setCreatedDate(e.getCreatedDate());
		d.setModifiedBy(e.getModifiedBy());
		d.setModifiedDate(e.getModifiedDate());

//		d.setRemark(e.getRemark());

		return d;
	}

}
