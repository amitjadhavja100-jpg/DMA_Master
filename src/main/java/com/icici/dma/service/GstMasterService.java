package com.icici.dma.service;

import java.util.List;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.GstMasterDto;
import com.icici.dma.model.GSTMaster;
import com.icici.dma.model.GSTMasterTemp;

public interface GstMasterService {

	public List<GstMasterDto> getGSTMasterMaker();

	public void updateGSTMasterByMaker(GstMasterDto GSTMasterDto, String username);

	public void createGSTMaster(GstMasterDto GSTMasterDto, String username);

	public List<GstMasterDto> getAllGSTMasterChecker(String user);

	public void updateGSTMasterByChecker(CheckerDecisionReq requestPayload,String username);


	default GSTMaster converDtoToGstMaster(GstMasterDto e) {

		GSTMaster d = new GSTMaster();

		d.setApsCode(e.getApsCode());
		d.setName(e.getName());
		d.setState(e.getState());
		d.setLocation(e.getLocation());
//
//		d.setCreatedBy(e.getCreatedBy());
//		d.setCreatedDate(e.getCreatedDate());
//		d.setModifiedBy(e.getModifiedBy());
//		d.setModifiedDate(e.getModifiedDate());

//		d.setRemarak(e.getRemarak());
		d.setStatus(e.getStatus());
//		d.setActionDate(e.getActionDate());
//		d.setActionType(e.getActionType());
//		d.setActionUser(e.getActionUser());
		return d;
	}

	default GstMasterDto convertGSTMasTempToDto(GSTMasterTemp e) {

		GstMasterDto d = new GstMasterDto();

		d.setApsCode(e.getApsCode());
		d.setName(e.getName());
		d.setState(e.getState());
		d.setLocation(e.getLocation());
//
//		d.setCreatedBy(e.getCreatedBy());
//		d.setCreatedDate(e.getCreatedDate());
//		d.setModifiedBy(e.getModifiedBy());
//		d.setModifiedDate(e.getModifiedDate());

		d.setRemarak(e.getRemarak());
		d.setStatus(e.getStatus());
//
//		d.setActionDate(e.getActionDate());
//		d.setActionType(e.getActionType());
//		d.setActionUser(e.getActionUser());

		return d;
	}

	default GSTMasterTemp converDtoToGstMasTemp(GstMasterDto e) {

		GSTMasterTemp d = new GSTMasterTemp();

		d.setApsCode(e.getApsCode());
		d.setName(e.getName());
		d.setState(e.getState());
		d.setLocation(e.getLocation());
//
//		d.setCreatedBy(e.getCreatedBy());
//		d.setCreatedDate(e.getCreatedDate());
//		d.setModifiedBy(e.getModifiedBy());
//		d.setModifiedDate(e.getModifiedDate());
//
//		d.setRemarak(e.getRemarak());
//		d.setStatus(e.getStatus());
//		d.setActionDate(e.getActionDate());
//		d.setActionType(e.getActionType());
//		d.setActionUser(e.getActionUser());
		return d;
	}
	default GSTMaster converGstMasterTempToGstMaster(GSTMasterTemp e) {

		GSTMaster d = new GSTMaster();

		d.setApsCode(e.getApsCode());
		d.setName(e.getName());
		d.setState(e.getState());
		d.setLocation(e.getLocation());

		d.setCreatedBy(e.getCreatedBy());
		d.setCreatedDate(e.getCreatedDate());
		d.setModifiedBy(e.getModifiedBy());
		d.setModifiedDate(e.getModifiedDate());

//		d.setRemarak(e.getRemarak());
		d.setStatus(e.getStatus());
//		d.setActionDate(e.getActionDate());
//		d.setActionType(e.getActionType());
//		d.setActionUser(e.getActionUser());
		return d;
	}
	
	default GSTMasterTemp converGstMasterToGstMasterTemp(GSTMaster e) {

		GSTMasterTemp d = new GSTMasterTemp();

		d.setApsCode(e.getApsCode());
		d.setName(e.getName());
		d.setState(e.getState());
		d.setLocation(e.getLocation());

		d.setCreatedBy(e.getCreatedBy());
		d.setCreatedDate(e.getCreatedDate());
		d.setModifiedBy(e.getModifiedBy());
		d.setModifiedDate(e.getModifiedDate());

//		d.setRemarak(e.getRemarak());
		d.setStatus(e.getStatus());
//		d.setActionDate(e.getActionDate());
//		d.setActionType(e.getActionType());
//		d.setActionUser(e.getActionUser());
		return d;
	}

	List<GstMasterDto> getGSTMasterByStatus(String status);

}
