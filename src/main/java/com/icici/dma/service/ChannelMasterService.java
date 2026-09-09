package com.icici.dma.service;

import java.util.List;

import com.icici.dma.dto.ChannelMasterDto;
import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.model.ChannelMaster;
import com.icici.dma.model.ChannelMasterTemp;

public interface ChannelMasterService {

	public List<ChannelMasterDto> getAllChannelMstaerMaker();

	public void updateChannelMasterByMaker(ChannelMasterDto channelMasterDto, String username);

	public void createChannelMaster(ChannelMasterDto channelMasterDto, String username);

	public List<ChannelMasterDto> getAllChannelMasterChecker(String user);

	public void updateChannelMasterByChecker(CheckerDecisionReq requestPayload, String username);

	public List<ChannelMasterDto> getChannelMasterByStatus(String statusType);
	
	default ChannelMasterDto convertChannelMasterToDto(ChannelMaster e) {

		ChannelMasterDto d = new ChannelMasterDto();

		d.setApsCode(e.getApsCode());
		d.setiBoxId(e.getiBoxId());
		d.setSupplierId(e.getSupplierId());
		d.setDates(e.getDates());
		d.setSuspended(e.getSuspended());

		d.setNameOfChannel(e.getNameOfChannel());
		d.setTypeOfDsa(e.getTypeOfDsa());
		d.setPanNo(e.getPanNo());
		d.setYy(e.getYy());

		d.setSupplierId1(e.getSupplierId1());
		d.setSupplierId2(e.getSupplierId2());
		d.setSupplierId3(e.getSupplierId3());
		d.setSupplierId4(e.getSupplierId4());
		d.setSupplierId5(e.getSupplierId5());
		d.setSupplierId6(e.getSupplierId6());
		d.setSupplierId7(e.getSupplierId7());
		d.setSupplierId8(e.getSupplierId8());
		d.setSupplierId9(e.getSupplierId9());
		d.setSupplierId10(e.getSupplierId10());
		d.setSupplierId11(e.getSupplierId11());
		d.setSupplierId12(e.getSupplierId12());
//		d.setSupplierId13(e.getSupplierId13());

		d.setRemark(e.getRemark());
		d.setLocation(e.getLocation());
		d.setMisState(e.getMisState());
		d.setcState(e.getcState());
		d.setEdState(e.getEdState());
		d.setEdZone(e.getEdZone());

		d.setSourcing(e.getSourcing());
		d.setSourcing1(e.getSourcing1());

		d.setManufactuName(e.getManufactuName());
		d.setNewManufactuName(e.getNewManufactuName());
		d.setOldIBoxId(e.getOldIBoxId());

		d.setRcLimit(e.getRcLimit());
		d.setSapCode(e.getSapCode());

		d.setAccountNo(e.getAccountNo());
		d.setIfscCode(e.getIfscCode());
		d.setBankName(e.getBankName());
		d.setiBankYesNonIBankNo(e.getiBankYesNonIBankNo());

//		d.setRemarksBk(e.getRemarksBk());
		d.setCreatedBy(e.getCreatedBy());
		d.setCreatedDate(e.getCreatedDate());
		d.setModifiedBy(e.getModifiedBy());
		d.setModifiedDate(e.getModifiedDate());
//		d.setRemarksBkk(e.getRemarksBkk());

		d.setStatus(e.getStatus());

		return d;
	}

	default ChannelMaster convertDtoToChannelMaster(ChannelMasterDto d) {

		ChannelMaster e = new ChannelMaster();

		e.setApsCode(d.getApsCode());
		e.setiBoxId(d.getiBoxId());
		e.setSupplierId(d.getSupplierId());
		e.setDates(d.getDates());

		e.setSuspended(d.getSuspended());
		e.setNameOfChannel(d.getNameOfChannel());
		e.setTypeOfDsa(d.getTypeOfDsa());
		e.setPanNo(d.getPanNo());
		e.setYy(d.getYy());

		e.setSupplierId1(d.getSupplierId1());
		e.setSupplierId2(d.getSupplierId2());
		e.setSupplierId3(d.getSupplierId3());
		e.setSupplierId4(d.getSupplierId4());
		e.setSupplierId5(d.getSupplierId5());
		e.setSupplierId6(d.getSupplierId6());
		e.setSupplierId7(d.getSupplierId7());
		e.setSupplierId8(d.getSupplierId8());
		e.setSupplierId9(d.getSupplierId9());
		e.setSupplierId10(d.getSupplierId10());
		e.setSupplierId11(d.getSupplierId11());
		e.setSupplierId12(d.getSupplierId12());
//		e.setSupplierId13(d.getSupplierId13());

		e.setRemark(d.getRemark());
		e.setLocation(d.getLocation());
		e.setMisState(d.getMisState());
		e.setcState(d.getcState());
		e.setEdState(d.getEdState());
		e.setEdZone(d.getEdZone());

		e.setSourcing(d.getSourcing());
		e.setSourcing1(d.getSourcing1());

		e.setManufactuName(d.getManufactuName());
		e.setNewManufactuName(d.getNewManufactuName());
		e.setOldIBoxId(d.getOldIBoxId());

		e.setRcLimit(d.getRcLimit());
		e.setSapCode(d.getSapCode());

		e.setAccountNo(d.getAccountNo());
		e.setIfscCode(d.getIfscCode());
		e.setBankName(d.getBankName());
		e.setiBankYesNonIBankNo(d.getiBankYesNonIBankNo());

//		e.setRemarksBk(d.getRemarksBk());
		e.setCreatedBy(d.getCreatedBy());
		e.setCreatedDate(d.getCreatedDate());
		e.setModifiedBy(d.getModifiedBy());
		e.setModifiedDate(d.getModifiedDate());
//		e.setRemarksBkk(d.getRemarksBkk());

		e.setStatus(d.getStatus());

		return e;
	}

	default ChannelMasterDto convertChannelMasterTempToDto(ChannelMasterTemp e) {

		ChannelMasterDto d = new ChannelMasterDto();

		d.setApsCode(e.getApsCode());
		d.setiBoxId(e.getiBoxId());
		d.setSupplierId(e.getSupplierId());
		d.setDates(e.getDates());
		d.setSuspended(e.getSuspended());

		d.setNameOfChannel(e.getNameOfChannel());
		d.setTypeOfDsa(e.getTypeOfDsa());
		d.setPanNo(e.getPanNo());
		d.setYy(e.getYy());

		d.setSupplierId1(e.getSupplierId1());
		d.setSupplierId2(e.getSupplierId2());
		d.setSupplierId3(e.getSupplierId3());
		d.setSupplierId4(e.getSupplierId4());
		d.setSupplierId5(e.getSupplierId5());
		d.setSupplierId6(e.getSupplierId6());
		d.setSupplierId7(e.getSupplierId7());
		d.setSupplierId8(e.getSupplierId8());
		d.setSupplierId9(e.getSupplierId9());
		d.setSupplierId10(e.getSupplierId10());
		d.setSupplierId11(e.getSupplierId11());
		d.setSupplierId12(e.getSupplierId12());
//		d.setSupplierId13(e.getSupplierId13());

		d.setRemark(e.getRemark());
		d.setLocation(e.getLocation());
		d.setMisState(e.getMisState());
		d.setcState(e.getcState());
		d.setEdState(e.getEdState());
		d.setEdZone(e.getEdZone());

		d.setSourcing(e.getSourcing());
		d.setSourcing1(e.getSourcing1());

		d.setManufactuName(e.getManufactuName());
		d.setNewManufactuName(e.getNewManufactuName());
		d.setOldIBoxId(e.getOldIBoxId());

		d.setRcLimit(e.getRcLimit());
		d.setSapCode(e.getSapCode());

		d.setAccountNo(e.getAccountNo());
		d.setIfscCode(e.getIfscCode());
		d.setBankName(e.getBankName());
		d.setiBankYesNonIBankNo(e.getiBankYesNonIBankNo());

//		d.setRemarksBk(e.getRemarksBk());
		d.setCreatedBy(e.getCreatedBy());
		d.setCreatedDate(e.getCreatedDate());
		d.setModifiedBy(e.getModifiedBy());
		d.setModifiedDate(e.getModifiedDate());
//		d.setRemarksBkk(e.getRemarksBkk());
		d.setRemarkByChk(e.getRemarkByChk());
		d.setStatus(e.getStatus());

		return d;
	}

	default ChannelMasterTemp convertDtoToChannelMasTemp(ChannelMasterDto e) {

		ChannelMasterTemp d = new ChannelMasterTemp();

		d.setApsCode(e.getApsCode());
		d.setiBoxId(e.getiBoxId());
		d.setSupplierId(e.getSupplierId());
		d.setDates(e.getDates());
		d.setSuspended(e.getSuspended());

		d.setNameOfChannel(e.getNameOfChannel());
		d.setTypeOfDsa(e.getTypeOfDsa());
		d.setPanNo(e.getPanNo());
		d.setYy(e.getYy());

		d.setSupplierId1(e.getSupplierId1());
		d.setSupplierId2(e.getSupplierId2());
		d.setSupplierId3(e.getSupplierId3());
		d.setSupplierId4(e.getSupplierId4());
		d.setSupplierId5(e.getSupplierId5());
		d.setSupplierId6(e.getSupplierId6());
		d.setSupplierId7(e.getSupplierId7());
		d.setSupplierId8(e.getSupplierId8());
		d.setSupplierId9(e.getSupplierId9());
		d.setSupplierId10(e.getSupplierId10());
		d.setSupplierId11(e.getSupplierId11());
		d.setSupplierId12(e.getSupplierId12());
//		d.setSupplierId13(e.getSupplierId13());

		d.setRemark(e.getRemark());
		d.setLocation(e.getLocation());
		d.setMisState(e.getMisState());
		d.setcState(e.getcState());
		d.setEdState(e.getEdState());
		d.setEdZone(e.getEdZone());

		d.setSourcing(e.getSourcing());
		d.setSourcing1(e.getSourcing1());

		d.setManufactuName(e.getManufactuName());
		d.setNewManufactuName(e.getNewManufactuName());
		d.setOldIBoxId(e.getOldIBoxId());

		d.setRcLimit(e.getRcLimit());
		d.setSapCode(e.getSapCode());

		d.setAccountNo(e.getAccountNo());
		d.setIfscCode(e.getIfscCode());
		d.setBankName(e.getBankName());
		d.setiBankYesNonIBankNo(e.getiBankYesNonIBankNo());

//		d.setRemarksBk(e.getRemarksBk());
		d.setCreatedBy(e.getCreatedBy());
		d.setCreatedDate(e.getCreatedDate());
		d.setModifiedBy(e.getModifiedBy());
		d.setModifiedDate(e.getModifiedDate());
//		d.setRemarksBkk(e.getRemarksBkk());

		d.setRemarkByChk(e.getRemarkByChk());

		d.setStatus(e.getStatus());

		return d;
	}

	default ChannelMaster convertChannelMasterTempToChannelMaster(ChannelMasterTemp e) {

		ChannelMaster d = new ChannelMaster();

		d.setApsCode(e.getApsCode());
		d.setiBoxId(e.getiBoxId());
		d.setSupplierId(e.getSupplierId());
		d.setDates(e.getDates());
		d.setSuspended(e.getSuspended());

		d.setNameOfChannel(e.getNameOfChannel());
		d.setTypeOfDsa(e.getTypeOfDsa());
		d.setPanNo(e.getPanNo());
		d.setYy(e.getYy());

		d.setSupplierId1(e.getSupplierId1());
		d.setSupplierId2(e.getSupplierId2());
		d.setSupplierId3(e.getSupplierId3());
		d.setSupplierId4(e.getSupplierId4());
		d.setSupplierId5(e.getSupplierId5());
		d.setSupplierId6(e.getSupplierId6());
		d.setSupplierId7(e.getSupplierId7());
		d.setSupplierId8(e.getSupplierId8());
		d.setSupplierId9(e.getSupplierId9());
		d.setSupplierId10(e.getSupplierId10());
		d.setSupplierId11(e.getSupplierId11());
		d.setSupplierId12(e.getSupplierId12());
//		d.setSupplierId13(e.getSupplierId13());

		d.setRemark(e.getRemark());
		d.setLocation(e.getLocation());
		d.setMisState(e.getMisState());
		d.setcState(e.getcState());
		d.setEdState(e.getEdState());
		d.setEdZone(e.getEdZone());

		d.setSourcing(e.getSourcing());
		d.setSourcing1(e.getSourcing1());

		d.setManufactuName(e.getManufactuName());
		d.setNewManufactuName(e.getNewManufactuName());
		d.setOldIBoxId(e.getOldIBoxId());

		d.setRcLimit(e.getRcLimit());
		d.setSapCode(e.getSapCode());

		d.setAccountNo(e.getAccountNo());
		d.setIfscCode(e.getIfscCode());
		d.setBankName(e.getBankName());
		d.setiBankYesNonIBankNo(e.getiBankYesNonIBankNo());

//		d.setRemarksBk(e.getRemarksBk());
		d.setCreatedBy(e.getCreatedBy());
		d.setCreatedDate(e.getCreatedDate());
		d.setModifiedBy(e.getModifiedBy());
		d.setModifiedDate(e.getModifiedDate());
//		d.setRemarksBkk(e.getRemarksBkk());

		d.setStatus(e.getStatus());

		return d;
	}

	default ChannelMasterTemp convertChannelMasterToChannelMasterTemp(ChannelMaster e) {

		ChannelMasterTemp d = new ChannelMasterTemp();

		d.setApsCode(e.getApsCode());
		d.setiBoxId(e.getiBoxId());
		d.setSupplierId(e.getSupplierId());
		d.setDates(e.getDates());
		d.setSuspended(e.getSuspended());

		d.setNameOfChannel(e.getNameOfChannel());
		d.setTypeOfDsa(e.getTypeOfDsa());
		d.setPanNo(e.getPanNo());
		d.setYy(e.getYy());

		d.setSupplierId1(e.getSupplierId1());
		d.setSupplierId2(e.getSupplierId2());
		d.setSupplierId3(e.getSupplierId3());
		d.setSupplierId4(e.getSupplierId4());
		d.setSupplierId5(e.getSupplierId5());
		d.setSupplierId6(e.getSupplierId6());
		d.setSupplierId7(e.getSupplierId7());
		d.setSupplierId8(e.getSupplierId8());
		d.setSupplierId9(e.getSupplierId9());
		d.setSupplierId10(e.getSupplierId10());
		d.setSupplierId11(e.getSupplierId11());
		d.setSupplierId12(e.getSupplierId12());
//		d.setSupplierId13(e.getSupplierId13());

		d.setRemark(e.getRemark());
		d.setLocation(e.getLocation());
		d.setMisState(e.getMisState());
		d.setcState(e.getcState());
		d.setEdState(e.getEdState());
		d.setEdZone(e.getEdZone());

		d.setSourcing(e.getSourcing());
		d.setSourcing1(e.getSourcing1());

		d.setManufactuName(e.getManufactuName());
		d.setNewManufactuName(e.getNewManufactuName());
		d.setOldIBoxId(e.getOldIBoxId());

		d.setRcLimit(e.getRcLimit());
		d.setSapCode(e.getSapCode());

		d.setAccountNo(e.getAccountNo());
		d.setIfscCode(e.getIfscCode());
		d.setBankName(e.getBankName());
		d.setiBankYesNonIBankNo(e.getiBankYesNonIBankNo());

//		d.setRemarksBk(e.getRemarksBk());
		d.setCreatedBy(e.getCreatedBy());
		d.setCreatedDate(e.getCreatedDate());
		d.setModifiedBy(e.getModifiedBy());
		d.setModifiedDate(e.getModifiedDate());
//		d.setRemarksBkk(e.getRemarksBkk());

		d.setStatus(e.getStatus());

		return d;
	}


}
