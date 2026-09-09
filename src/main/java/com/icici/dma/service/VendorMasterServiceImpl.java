package com.icici.dma.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.VendorMasterDto;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.VendorMaster;
import com.icici.dma.model.VendorMasterTemp;
import com.icici.dma.repository.VendorMasterRepository;
import com.icici.dma.repository.VendorMasterTempRepository;

import org.springframework.beans.BeanUtils;

@Service
@Transactional
public class VendorMasterServiceImpl implements VendorMasterService {

	@Autowired
	private VendorMasterRepository vendorMasterRepository;

	@Autowired
	private VendorMasterTempRepository vendorMasterTempRepository;

	@Override
	public List<VendorMasterDto> getVendorMasterMaker() {

		List<VendorMasterDto> dtoList = new ArrayList<>();

		vendorMasterRepository.findAll().forEach(master -> dtoList.add(convertMasterToDto(master)));

		vendorMasterTempRepository.findAllByStatus(StatusConstant.PENDING)
				.forEach(temp -> dtoList.add(convertTempToDto(temp)));

		return dtoList;
	}

	@Override
	public String createVendorMaster(VendorMasterDto dto, String user) {

		if (vendorMasterTempRepository.existsByVendorNoAndStatus(dto.getVendorNo(), StatusConstant.PENDING)) {

			throw new RuntimeException("Vendor already waiting for approval");
		}

		if (vendorMasterRepository.existsByVendorNo(dto.getVendorNo())) {

			throw new RuntimeException("Vendor already approved");
		}

		VendorMasterTemp temp = new VendorMasterTemp();

		temp.setVendorNo(dto.getVendorNo());
		temp.setName1(dto.getName1());
		temp.setName2(dto.getName2());
		temp.setSearchItm(dto.getSearchItm());
		temp.setStreetHouse(dto.getStreetHouse());

		temp.setStatus(StatusConstant.PENDING);

		temp.setActionType(ActionConstant.INSERT);

		temp.setCreatedBy(user);

		temp.setCreatedDate(new Date());

		temp.setActionUser(user);

		temp.setActionDate(new Date());

		vendorMasterTempRepository.save(temp);

		return "Vendor Master created successfully";
	}

	@Override
	public String updateVendorMasterByMaker(VendorMasterDto dto, String user) {

		VendorMaster master = vendorMasterRepository.findById(dto.getVendorNo())
				.orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

		VendorMasterTemp temp = convertMasterToTemp(master);

		temp.setName1(dto.getName1());
		temp.setName2(dto.getName2());
		temp.setSearchItm(dto.getSearchItm());
		temp.setStreetHouse(dto.getStreetHouse());

		temp.setStatus(StatusConstant.PENDING);

		temp.setActionType(ActionConstant.UPDATE);

		temp.setModifiedBy(user);

		temp.setModifiedDate(new Date());

		temp.setActionUser(user);

		temp.setActionDate(new Date());

		vendorMasterTempRepository.save(temp);

		return "Vendor Master updated successfully";
	}

	@Override
	public List<VendorMasterDto> getAllVendorMasterChecker(String user) {

		return vendorMasterTempRepository.findAllByStatusAndCreatedByNot(StatusConstant.PENDING, user).stream()
				.map(this::convertTempToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public String updateVendorMasterByChecker(CheckerDecisionReq req, String user) {

		if (req.getPrimaryIds() == null || req.getPrimaryIds().isEmpty()) {

			throw new RuntimeException("No records selected");
		}

		if (StatusConstant.APPROVE.equalsIgnoreCase(req.getDecision())) {

			for (String vendorNo : req.getPrimaryIds()) {

				VendorMasterTemp temp = vendorMasterTempRepository
						.findByVendorNoAndStatus(vendorNo, StatusConstant.PENDING)
						.orElseThrow(() -> new RuntimeException("Record not found"));

				/*
				 * VendorMaster master = new VendorMaster();
				 * 
				 * master.setVendorNo( temp.getVendorNo());
				 * 
				 * master.setName1( temp.getName1());
				 * 
				 * master.setName2( temp.getName2());
				 * 
				 * master.setSearchItm( temp.getSearchItm());
				 * 
				 * master.setStreetHouse( temp.getStreetHouse());
				 * 
				 * master.setStatus( StatusConstant.APPROVE);
				 * 
				 * master.setCreatedBy( temp.getCreatedBy());
				 * 
				 * master.setCreatedDate( temp.getCreatedDate());
				 * 
				 * master.setModifiedBy( user);
				 * 
				 * master.setModifiedDate( new Date());
				 * 
				 * vendorMasterRepository .save(master);
				 * 
				 * vendorMasterTempRepository .delete(temp);
				 */

				VendorMaster master = new VendorMaster();

				BeanUtils.copyProperties(temp, master);

				master.setStatus(StatusConstant.APPROVE);

				master.setModifiedBy(user);
				master.setModifiedDate(new Date());

				master.setApprovedBy(user);
				master.setApprovedDate(new Date());

				vendorMasterRepository.save(master);

				vendorMasterTempRepository.delete(temp);
			}

			return "Approved Successfully";
		}

		for (String vendorNo : req.getPrimaryIds()) {

			VendorMasterTemp temp = vendorMasterTempRepository.findByVendorNoAndStatus(vendorNo, StatusConstant.PENDING)
					.orElseThrow(() -> new RuntimeException("Record not found"));

			temp.setStatus(StatusConstant.REJECTE);

			temp.setRemarks(req.getRemark());

			temp.setActionUser(user);

			temp.setActionDate(new Date());

			vendorMasterTempRepository.save(temp);
		}

		return "Rejected Successfully";
	}

	@Override
	public List<VendorMasterDto> getVendorMasterByStatus(String status) {

		List<VendorMasterDto> dtoList = new ArrayList<>();

		if (StatusConstant.ALL.equalsIgnoreCase(status)) {

			vendorMasterRepository.findAll().forEach(master -> dtoList.add(convertMasterToDto(master)));

			vendorMasterTempRepository.findAll().forEach(temp -> dtoList.add(convertTempToDto(temp)));

			return dtoList;
		}

		if (StatusConstant.APPROVE.equalsIgnoreCase(status)) {

			return vendorMasterRepository.findAllByStatus(StatusConstant.APPROVE).stream().map(this::convertMasterToDto)
					.collect(Collectors.toList());
		}

		return vendorMasterTempRepository.findAllByStatus(status).stream().map(this::convertTempToDto)
				.collect(Collectors.toList());
	}

	/*
	 * private VendorMasterTemp convertMasterToTemp( VendorMaster master) {
	 * 
	 * VendorMasterTemp temp = new VendorMasterTemp();
	 * 
	 * temp.setVendorNo( master.getVendorNo());
	 * 
	 * temp.setName1( master.getName1());
	 * 
	 * temp.setName2( master.getName2());
	 * 
	 * temp.setSearchItm( master.getSearchItm());
	 * 
	 * temp.setStreetHouse( master.getStreetHouse());
	 * 
	 * temp.setStreet4(master.getStreet4()); temp.setStreet5(master.getStreet5());
	 * temp.setPostCode(master.getPostCode()); temp.setCity(master.getCity());
	 * temp.setCountry(master.getCountry()); temp.setRegion(master.getRegion());
	 * temp.setStateName(master.getStateName()); temp.setTelNo(master.getTelNo());
	 * temp.setMobileNo(master.getMobileNo()); temp.setFax(master.getFax());
	 * temp.setCtr(master.getCtr()); temp.setBankKey(master.getBankKey());
	 * temp.setBankAccount(master.getBankAccount());
	 * temp.setAccountHolder(master.getAccountHolder());
	 * temp.setControlKey(master.getControlKey());
	 * temp.setBankType(master.getBankType());
	 * temp.setReferenceDetails(master.getReferenceDetails());
	 * temp.setRecAc(master.getRecAc()); temp.setPaymMethd(master.getPaymMethd());
	 * temp.setAlterPay(master.getAlterPay()); temp.setPb(master.getPb());
	 * temp.sethBank(master.gethBank());
	 * temp.setExtraTextPanNumber(master.getExtraTextPanNumber());
	 * temp.setCinPanNumber(master.getCinPanNumber());
	 * temp.setExciseRegNumber(master.getExciseRegNumber());
	 * temp.setCentralSalesTaxNumber(master.getCentralSalesTaxNumber());
	 * temp.setLocalSalesTaxNumber(master.getLocalSalesTaxNumber());
	 * temp.setServiceTaxRegisNumber(master.getServiceTaxRegisNumber());
	 * temp.setServiceTaxNo(master.getServiceTaxNo());
	 * temp.setSalesTaxNo(master.getSalesTaxNo()); temp.setName3(master.getName3());
	 * temp.setName4(master.getName4()); temp.setBankName(master.getBankName());
	 * temp.setBankBranch(master.getBankBranch());
	 * temp.setBranchAddress(master.getBranchAddress());
	 * temp.setTaxCode(master.getTaxCode()); temp.setWctCode(master.getWctCode());
	 * temp.setEmailAddress(master.getEmailAddress());
	 * temp.setOutSourcingActivity(master.getOutSourcingActivity());
	 * temp.setVptsId(master.getVptsId());
	 * temp.setActivityNo(master.getActivityNo());
	 * temp.setGstVendorClassification(master.getGstVendorClassification());
	 * temp.setGstVendorClassificationDesc(master.getGstVendorClassificationDesc());
	 * temp.setTaxNumber3(master.getTaxNumber3());
	 * temp.setBlackListingReason(master.getBlackListingReason());
	 * temp.setSearchTerm2(master.getSearchTerm2());
	 * temp.setWithholdingTaxType1(master.getWithholdingTaxType1());
	 * temp.setWithholdingTaxCode1(master.getWithholdingTaxCode1());
	 * temp.setWithholdingTaxType2(master.getWithholdingTaxType2());
	 * temp.setWithholdingTaxCode2(master.getWithholdingTaxCode2());
	 * temp.setWithholdingTaxType3(master.getWithholdingTaxType3());
	 * temp.setWithholdingTaxCode3(master.getWithholdingTaxCode3());
	 * temp.setWithholdingTaxType4(master.getWithholdingTaxType4());
	 * temp.setWithholdingTaxCode4(master.getWithholdingTaxCode4());
	 * temp.setMsmedStatus(master.getMsmedStatus());
	 * temp.setVendorTagging(master.getVendorTagging());
	 * temp.setComments(master.getComments());
	 * temp.setVendorBlock(master.getVendorBlock());
	 * temp.setCredInfoNo(master.getCredInfoNo());
	 * temp.setSpecificPerson206ab206cca(master.getSpecificPerson206ab206cca());
	 * temp.setAdhaarPanLinked(master.getAdhaarPanLinked());
	 * temp.setVendorReturnFiling(master.getVendorReturnFiling());
	 * temp.setPoBoxNumber(master.getPoBoxNumber());
	 * temp.setTaxNumber1(master.getTaxNumber1());
	 * temp.setDepartment(master.getDepartment()); temp.setUdyam(master.getUdyam());
	 * temp.setExemptionNumber(master.getExemptionNumber());
	 * temp.setTaxNumber12(master.getTaxNumber12());
	 * temp.setDepartment2(master.getDepartment2());
	 * temp.setCreateDateStr(master.getCreateDateStr());
	 * temp.setLastExtReview(master.getLastExtReview());
	 * temp.setExemptionFrom(master.getExemptionFrom());
	 * temp.setExemptionTo(master.getExemptionTo());
	 * temp.setExemptionPercentage(master.getExemptionPercentage());
	 * temp.setThresholdAmountExemption(master.getThresholdAmountExemption());
	 * temp.setLowerTdsRate(master.getLowerTdsRate());
	 * temp.setStandardRateRelatedParty(master.getStandardRateRelatedParty());
	 * 
	 * temp.setStatus(master.getStatus());
	 * 
	 * temp.setRemarks(master.getRemarks());
	 * temp.setCreatedBy(master.getCreatedBy());
	 * temp.setCreatedDate(master.getCreatedDate());
	 * temp.setModifiedBy(master.getModifiedBy());
	 * temp.setModifiedDate(master.getModifiedDate());
	 * temp.setActionType(master.getActionType());
	 * temp.setActionUser(master.getActionUser());
	 * temp.setActionDate(master.getActionDate());
	 * temp.setUploadId(master.getUploadId());
	 * temp.setFileName(master.getFileName());
	 * 
	 * 
	 * 
	 * return temp; }
	 * 
	 * 
	 * private VendorMasterDto convertMasterToDto(VendorMaster master) {
	 * 
	 * VendorMasterDto dto = new VendorMasterDto();
	 * 
	 * dto.setVendorNo(master.getVendorNo()); dto.setName1(master.getName1());
	 * dto.setName2(master.getName2()); dto.setSearchItm(master.getSearchItm());
	 * dto.setStreetHouse(master.getStreetHouse());
	 * 
	 * dto.setStreet4(master.getStreet4()); dto.setStreet5(master.getStreet5());
	 * dto.setPostCode(master.getPostCode()); dto.setCity(master.getCity());
	 * dto.setCountry(master.getCountry()); dto.setRegion(master.getRegion());
	 * dto.setStateName(master.getStateName()); dto.setTelNo(master.getTelNo());
	 * dto.setMobileNo(master.getMobileNo()); dto.setFax(master.getFax());
	 * dto.setCtr(master.getCtr()); dto.setBankKey(master.getBankKey());
	 * dto.setBankAccount(master.getBankAccount());
	 * dto.setAccountHolder(master.getAccountHolder());
	 * dto.setControlKey(master.getControlKey());
	 * dto.setBankType(master.getBankType());
	 * dto.setReferenceDetails(master.getReferenceDetails());
	 * dto.setRecAc(master.getRecAc()); dto.setPaymMethd(master.getPaymMethd());
	 * dto.setAlterPay(master.getAlterPay()); dto.setPb(master.getPb());
	 * dto.setHbank(master.gethBank());
	 * dto.setExtraTextPanNumber(master.getExtraTextPanNumber());
	 * dto.setCinPanNumber(master.getCinPanNumber());
	 * dto.setExciseRegNumber(master.getExciseRegNumber());
	 * dto.setCentralSalesTaxNumber(master.getCentralSalesTaxNumber());
	 * dto.setLocalSalesTaxNumber(master.getLocalSalesTaxNumber());
	 * dto.setServiceTaxRegisNumber(master.getServiceTaxRegisNumber());
	 * dto.setServiceTaxNo(master.getServiceTaxNo());
	 * dto.setSalesTaxNo(master.getSalesTaxNo()); dto.setName3(master.getName3());
	 * dto.setName4(master.getName4()); dto.setBankName(master.getBankName());
	 * dto.setBankBranch(master.getBankBranch());
	 * dto.setBranchAddress(master.getBranchAddress());
	 * dto.setTaxCode(master.getTaxCode()); dto.setWctCode(master.getWctCode());
	 * dto.setEmailAddress(master.getEmailAddress());
	 * dto.setOutSourcingActivity(master.getOutSourcingActivity());
	 * dto.setVptsId(master.getVptsId()); dto.setActivityNo(master.getActivityNo());
	 * dto.setGstVendorClassification(master.getGstVendorClassification());
	 * dto.setGstVendorClassificationDescription(master.getGstVendorClassification()
	 * ); dto.setTaxNumber3(master.getTaxNumber3());
	 * dto.setBlackListingReason(master.getBlackListingReason());
	 * dto.setSearchTerm2(master.getSearchTerm2());
	 * dto.setWithholdingTaxType1(master.getWithholdingTaxType1());
	 * dto.setWithholdingTaxCode1(master.getWithholdingTaxCode1());
	 * dto.setWithholdingTaxType2(master.getWithholdingTaxType2());
	 * dto.setWithholdingTaxCode2(master.getWithholdingTaxCode2());
	 * dto.setWithholdingTaxType3(master.getWithholdingTaxType3());
	 * dto.setWithholdingTaxCode3(master.getWithholdingTaxCode3());
	 * dto.setWithholdingTaxType4(master.getWithholdingTaxType4());
	 * dto.setWithholdingTaxCode4(master.getWithholdingTaxCode4());
	 * dto.setMsmedStatus(master.getMsmedStatus());
	 * dto.setVendorTagging(master.getVendorTagging());
	 * dto.setComments(master.getComments());
	 * dto.setVendorBlock(master.getVendorBlock());
	 * dto.setCredInfoNo(master.getCredInfoNo());
	 * dto.setSpecificPerson206ab206cca(master.getSpecificPerson206ab206cca());
	 * dto.setAdhaarPanLinked(master.getAdhaarPanLinked());
	 * dto.setVendorReturnFiling(master.getVendorReturnFiling());
	 * dto.setPoBoxNumber(master.getPoBoxNumber());
	 * dto.setTaxNumber1(master.getTaxNumber1());
	 * dto.setDepartment(master.getDepartment()); dto.setUdyam(master.getUdyam());
	 * dto.setExemptionNumber(master.getExemptionNumber());
	 * dto.setTaxNumber1Second(master.getTaxNumber12());
	 * dto.setDepartmentSecond(master.getDepartment2());
	 * dto.setCreateDate(master.getCreateDateStr());
	 * dto.setLastExtReview(master.getLastExtReview());
	 * dto.setExemptionFrom(master.getExemptionFrom());
	 * dto.setExemptionTo(master.getExemptionTo());
	 * dto.setExemptionPercentage(master.getExemptionPercentage());
	 * dto.setThresholdAmountForExemption(master.getThresholdAmountExemption());
	 * dto.setLowerTdsRate(master.getLowerTdsRate());
	 * dto.setStandardRateRelatedParty(master.getStandardRateRelatedParty());
	 * 
	 * dto.setStatus(master.getStatus());
	 * 
	 * dto.setRemarks(master.getRemarks()); dto.setCreatedBy(master.getCreatedBy());
	 * dto.setCreatedDate(master.getCreatedDate());
	 * dto.setModifiedBy(master.getModifiedBy());
	 * dto.setModifiedDate(master.getModifiedDate());
	 * dto.setActionType(master.getActionType());
	 * dto.setActionUser(master.getActionUser());
	 * dto.setActionDate(master.getActionDate());
	 * dto.setUploadId(master.getUploadId()); dto.setFileName(master.getFileName());
	 * 
	 * 
	 * 
	 * return dto; }
	 * 
	 * private VendorMasterDto convertTempToDto(VendorMasterTemp temp) {
	 * 
	 * VendorMasterDto dto = new VendorMasterDto();
	 * 
	 * dto.setVendorNo(temp.getVendorNo()); dto.setName1(temp.getName1());
	 * dto.setName2(temp.getName2()); dto.setSearchItm(temp.getSearchItm());
	 * dto.setStreetHouse(temp.getStreetHouse());
	 * 
	 * dto.setStreet4(temp.getStreet4()); dto.setStreet5(temp.getStreet5());
	 * dto.setPostCode(temp.getPostCode()); dto.setCity(temp.getCity());
	 * dto.setCountry(temp.getCountry()); dto.setRegion(temp.getRegion());
	 * dto.setStateName(temp.getStateName()); dto.setTelNo(temp.getTelNo());
	 * dto.setMobileNo(temp.getMobileNo()); dto.setFax(temp.getFax());
	 * dto.setCtr(temp.getCtr()); dto.setBankKey(temp.getBankKey());
	 * dto.setBankAccount(temp.getBankAccount());
	 * dto.setAccountHolder(temp.getAccountHolder());
	 * dto.setControlKey(temp.getControlKey()); dto.setBankType(temp.getBankType());
	 * dto.setReferenceDetails(temp.getReferenceDetails());
	 * dto.setRecAc(temp.getRecAc()); dto.setPaymMethd(temp.getPaymMethd());
	 * dto.setAlterPay(temp.getAlterPay()); dto.setPb(temp.getPb());
	 * dto.setHbank(temp.gethBank());
	 * dto.setExtraTextPanNumber(temp.getExtraTextPanNumber());
	 * dto.setCinPanNumber(temp.getCinPanNumber());
	 * dto.setExciseRegNumber(temp.getExciseRegNumber());
	 * dto.setCentralSalesTaxNumber(temp.getCentralSalesTaxNumber());
	 * dto.setLocalSalesTaxNumber(temp.getLocalSalesTaxNumber());
	 * dto.setServiceTaxRegisNumber(temp.getServiceTaxRegisNumber());
	 * dto.setServiceTaxNo(temp.getServiceTaxNo());
	 * dto.setSalesTaxNo(temp.getSalesTaxNo()); dto.setName3(temp.getName3());
	 * dto.setName4(temp.getName4()); dto.setBankName(temp.getBankName());
	 * dto.setBankBranch(temp.getBankBranch());
	 * dto.setBranchAddress(temp.getBranchAddress());
	 * dto.setTaxCode(temp.getTaxCode()); dto.setWctCode(temp.getWctCode());
	 * dto.setEmailAddress(temp.getEmailAddress());
	 * dto.setOutSourcingActivity(temp.getOutSourcingActivity());
	 * dto.setVptsId(temp.getVptsId()); dto.setActivityNo(temp.getActivityNo());
	 * dto.setGstVendorClassification(temp.getGstVendorClassification());
	 * dto.setGstVendorClassificationDescription(temp.getGstVendorClassificationDesc
	 * ()); dto.setTaxNumber3(temp.getTaxNumber3());
	 * dto.setBlackListingReason(temp.getBlackListingReason());
	 * dto.setSearchTerm2(temp.getSearchTerm2());
	 * dto.setWithholdingTaxType1(temp.getWithholdingTaxType1());
	 * dto.setWithholdingTaxCode1(temp.getWithholdingTaxCode1());
	 * dto.setWithholdingTaxType2(temp.getWithholdingTaxType2());
	 * dto.setWithholdingTaxCode2(temp.getWithholdingTaxCode2());
	 * dto.setWithholdingTaxType3(temp.getWithholdingTaxType3());
	 * dto.setWithholdingTaxCode3(temp.getWithholdingTaxCode3());
	 * dto.setWithholdingTaxType4(temp.getWithholdingTaxType4());
	 * dto.setWithholdingTaxCode4(temp.getWithholdingTaxCode4());
	 * dto.setMsmedStatus(temp.getMsmedStatus());
	 * dto.setVendorTagging(temp.getVendorTagging());
	 * dto.setComments(temp.getComments());
	 * dto.setVendorBlock(temp.getVendorBlock());
	 * dto.setCredInfoNo(temp.getCredInfoNo());
	 * dto.setSpecificPerson206ab206cca(temp.getSpecificPerson206ab206cca());
	 * dto.setAdhaarPanLinked(temp.getAdhaarPanLinked());
	 * dto.setVendorReturnFiling(temp.getVendorReturnFiling());
	 * dto.setPoBoxNumber(temp.getPoBoxNumber());
	 * dto.setTaxNumber1(temp.getTaxNumber1());
	 * dto.setDepartment(temp.getDepartment()); dto.setUdyam(temp.getUdyam());
	 * dto.setExemptionNumber(temp.getExemptionNumber());
	 * dto.setTaxNumber1Second(temp.getTaxNumber12());
	 * dto.setDepartmentSecond(temp.getDepartment2());
	 * dto.setCreateDate(temp.getCreateDateStr());
	 * dto.setLastExtReview(temp.getLastExtReview());
	 * dto.setExemptionFrom(temp.getExemptionFrom());
	 * dto.setExemptionTo(temp.getExemptionTo());
	 * dto.setExemptionPercentage(temp.getExemptionPercentage());
	 * dto.setThresholdAmountForExemption(temp.getThresholdAmountExemption());
	 * dto.setLowerTdsRate(temp.getLowerTdsRate());
	 * dto.setStandardRateRelatedParty(temp.getStandardRateRelatedParty());
	 * 
	 * dto.setStatus(temp.getStatus());
	 * 
	 * dto.setRemarks(temp.getRemarks()); dto.setCreatedBy(temp.getCreatedBy());
	 * dto.setCreatedDate(temp.getCreatedDate());
	 * dto.setModifiedBy(temp.getModifiedBy());
	 * dto.setModifiedDate(temp.getModifiedDate());
	 * dto.setActionType(temp.getActionType());
	 * dto.setActionUser(temp.getActionUser());
	 * dto.setActionDate(temp.getActionDate()); dto.setUploadId(temp.getUploadId());
	 * dto.setFileName(temp.getFileName());
	 * 
	 * 
	 * 
	 * return dto; }
	 */
	
	private VendorMasterDto convertMasterToDto(VendorMaster master) {
	    VendorMasterDto dto = new VendorMasterDto();
	    BeanUtils.copyProperties(master, dto);
	    return dto;
	}
	 
	private VendorMasterDto convertTempToDto(VendorMasterTemp temp) {
	    VendorMasterDto dto = new VendorMasterDto();
	    BeanUtils.copyProperties(temp, dto);
	    return dto;
	}
	 
	private VendorMasterTemp convertMasterToTemp(VendorMaster master) {
	    VendorMasterTemp temp = new VendorMasterTemp();
	    BeanUtils.copyProperties(master, temp);
	    return temp;
	}

}
