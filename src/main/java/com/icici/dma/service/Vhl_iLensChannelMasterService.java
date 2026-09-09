package com.icici.dma.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.ILensChannelMasterDto;
import com.icici.dma.model.ILensChannelMaster;
import com.icici.dma.model.ILensChannelMasterTemp;

public interface Vhl_iLensChannelMasterService {

	public Map<String, Object> uploadILensChannelMaster(MultipartFile file, String loginUser);

	public ByteArrayInputStream exportErrorExcel(String user) throws Exception;

	
	
	
	public void createILensChannelMasterByMaker(ILensChannelMasterDto dto, String username);
	
	public void updateILensChannelMasterByMaker(ILensChannelMasterDto dto, String username);
	
	public List<ILensChannelMasterDto> getAlliLensChannelMasterByChecker(String user);
	
	public void updateiLensChannelMasterByChecker(CheckerDecisionReq requestPayload, String username);
	
	public List<ILensChannelMasterDto> getiLensChannelMasterByStatus(String statusType);

	
	
	
	
	
	public default ILensChannelMaster convertDtoToMain(ILensChannelMasterDto d) {

		ILensChannelMaster m = new ILensChannelMaster();
		
		m.setSrNo(d.getSrNo());
		m.setUserID(d.getUserID());
		m.setUserName(d.getUserName());
		m.setiDCreationDate(d.getiDCreationDate());
		m.setStatus(m.getStatus());
		m.setEmailID(d.getEmailID());
		m.setMobileNumber(d.getMobileNumber());
		m.setAgencyID(d.getAgencyID());
		m.setAgencyType(d.getAgencyType());
		m.setvPTSID(d.getvPTSID());
		m.setvPTSApprovalStatus(d.getvPTSApprovalStatus());
		m.setAnnualReviewDueDate(d.getAnnualReviewDueDate());
		m.setmSMERegistered(d.getmSMERegistered());
		m.setUdyogAadharNumber(d.getUdyogAadharNumber());
		m.setAgencyPanNumber(d.getAgencyPanNumber());
		m.setUserGroup(d.getUserGroup());
		m.setChannelName(d.getChannelName());
		m.setProductType(d.getProductType());
		m.setBaseCPCProcessShop(d.getBaseCPCProcessShop());
		m.setMappedEmployeeID(d.getMappedEmployeeID());
		m.setMappedEmployeeName(d.getMappedEmployeeName());
		m.setInboundOutBoundType(d.getInboundOutBoundType());
		m.setMappedSolIDs(d.getMappedSolIDs());
		m.setCounsellorIDs(d.getCounsellorIDs());
		m.settSMID(d.gettSMID());
		m.settSMName(d.gettSMName());
		m.setChildAllowed(d.getChildAllowed());
		m.setOperatingLocations(d.getOperatingLocations());
		m.setRoleCode(d.getRoleCode());
		m.setvSTSID(d.getvSTSID());
		m.setvSTSName(d.getvSTSName());
		m.setvSTSStatus(d.getvSTSStatus());
		m.setProduct(d.getProduct());
		return m;
	}

	public default ILensChannelMasterTemp convertDtoToTemp(ILensChannelMasterDto d) {

		ILensChannelMasterTemp m = new ILensChannelMasterTemp();
		m.setSrNo(d.getSrNo());
		m.setUserID(d.getUserID());
		m.setUserName(d.getUserName());
		m.setiDCreationDate(d.getiDCreationDate());
		m.setStatus(m.getStatus());
		m.setEmailID(d.getEmailID());
		m.setMobileNumber(d.getMobileNumber());
		m.setAgencyID(d.getAgencyID());
		m.setAgencyType(d.getAgencyType());
		m.setvPTSID(d.getvPTSID());
		m.setvPTSApprovalStatus(d.getvPTSApprovalStatus());
		m.setAnnualReviewDueDate(d.getAnnualReviewDueDate());
		m.setmSMERegistered(d.getmSMERegistered());
		m.setUdyogAadharNumber(d.getUdyogAadharNumber());
		m.setAgencyPanNumber(d.getAgencyPanNumber());
		m.setUserGroup(d.getUserGroup());
		m.setChannelName(d.getChannelName());
		m.setProductType(d.getProductType());
		m.setBaseCPCProcessShop(d.getBaseCPCProcessShop());
		m.setMappedEmployeeID(d.getMappedEmployeeID());
		m.setMappedEmployeeName(d.getMappedEmployeeName());
		m.setInboundOutBoundType(d.getInboundOutBoundType());
		m.setMappedSolIDs(d.getMappedSolIDs());
		m.setCounsellorIDs(d.getCounsellorIDs());
		m.settSMID(d.gettSMID());
		m.settSMName(d.gettSMName());
		m.setChildAllowed(d.getChildAllowed());
		m.setOperatingLocations(d.getOperatingLocations());
		m.setRoleCode(d.getRoleCode());
		m.setvSTSID(d.getvSTSID());
		m.setvSTSName(d.getvSTSName());
		m.setvSTSStatus(d.getvSTSStatus());
		m.setProduct(d.getProduct());
		return m;
	}

	public default ILensChannelMasterDto convertTempToDto(ILensChannelMasterTemp d) {

		ILensChannelMasterDto m = new ILensChannelMasterDto();
		
		m.setSrNo(d.getSrNo());
		m.setUserID(d.getUserID());
		m.setUserName(d.getUserName());
		m.setiDCreationDate(d.getiDCreationDate());
		m.setStatus(m.getStatus());
		m.setEmailID(d.getEmailID());
		m.setMobileNumber(d.getMobileNumber());
		m.setAgencyID(d.getAgencyID());
		m.setAgencyType(d.getAgencyType());
		m.setvPTSID(d.getvPTSID());
		m.setvPTSApprovalStatus(d.getvPTSApprovalStatus());
		m.setAnnualReviewDueDate(d.getAnnualReviewDueDate());
		m.setmSMERegistered(d.getmSMERegistered());
		m.setUdyogAadharNumber(d.getUdyogAadharNumber());
		m.setAgencyPanNumber(d.getAgencyPanNumber());
		m.setUserGroup(d.getUserGroup());
		m.setChannelName(d.getChannelName());
		m.setProductType(d.getProductType());
		m.setBaseCPCProcessShop(d.getBaseCPCProcessShop());
		m.setMappedEmployeeID(d.getMappedEmployeeID());
		m.setMappedEmployeeName(d.getMappedEmployeeName());
		m.setInboundOutBoundType(d.getInboundOutBoundType());
		m.setMappedSolIDs(d.getMappedSolIDs());
		m.setCounsellorIDs(d.getCounsellorIDs());
		m.settSMID(d.gettSMID());
		m.settSMName(d.gettSMName());
		m.setChildAllowed(d.getChildAllowed());
		m.setOperatingLocations(d.getOperatingLocations());
		m.setRoleCode(d.getRoleCode());
		m.setvSTSID(d.getvSTSID());
		m.setvSTSName(d.getvSTSName());
		m.setvSTSStatus(d.getvSTSStatus());
		m.setProduct(d.getProduct());
		
		m.setStatusA(d.getStatusA());
		return m;
	}
	
	public default ILensChannelMasterDto convertMainToDto(ILensChannelMaster d) {

		ILensChannelMasterDto m = new ILensChannelMasterDto();
		
		m.setSrNo(d.getSrNo());
		m.setUserID(d.getUserID());
		m.setUserName(d.getUserName());
		m.setiDCreationDate(d.getiDCreationDate());
		m.setStatus(m.getStatus());
		m.setEmailID(d.getEmailID());
		m.setMobileNumber(d.getMobileNumber());
		m.setAgencyID(d.getAgencyID());
		m.setAgencyType(d.getAgencyType());
		m.setvPTSID(d.getvPTSID());
		m.setvPTSApprovalStatus(d.getvPTSApprovalStatus());
		m.setAnnualReviewDueDate(d.getAnnualReviewDueDate());
		m.setmSMERegistered(d.getmSMERegistered());
		m.setUdyogAadharNumber(d.getUdyogAadharNumber());
		m.setAgencyPanNumber(d.getAgencyPanNumber());
		m.setUserGroup(d.getUserGroup());
		m.setChannelName(d.getChannelName());
		m.setProductType(d.getProductType());
		m.setBaseCPCProcessShop(d.getBaseCPCProcessShop());
		m.setMappedEmployeeID(d.getMappedEmployeeID());
		m.setMappedEmployeeName(d.getMappedEmployeeName());
		m.setInboundOutBoundType(d.getInboundOutBoundType());
		m.setMappedSolIDs(d.getMappedSolIDs());
		m.setCounsellorIDs(d.getCounsellorIDs());
		m.settSMID(d.gettSMID());
		m.settSMName(d.gettSMName());
		m.setChildAllowed(d.getChildAllowed());
		m.setOperatingLocations(d.getOperatingLocations());
		m.setRoleCode(d.getRoleCode());
		m.setvSTSID(d.getvSTSID());
		m.setvSTSName(d.getvSTSName());
		m.setvSTSStatus(d.getvSTSStatus());
		m.setProduct(d.getProduct());
		
		m.setStatusA(d.getStatusA());
		
		return m;
	}
	
	public default ILensChannelMaster convertTempToMain(ILensChannelMasterTemp d) {

		ILensChannelMaster m = new ILensChannelMaster();
		
		m.setSrNo(d.getSrNo());
		m.setUserID(d.getUserID());
		m.setUserName(d.getUserName());
		m.setiDCreationDate(d.getiDCreationDate());
		m.setStatus(m.getStatus());
		m.setEmailID(d.getEmailID());
		m.setMobileNumber(d.getMobileNumber());
		m.setAgencyID(d.getAgencyID());
		m.setAgencyType(d.getAgencyType());
		m.setvPTSID(d.getvPTSID());
		m.setvPTSApprovalStatus(d.getvPTSApprovalStatus());
		m.setAnnualReviewDueDate(d.getAnnualReviewDueDate());
		m.setmSMERegistered(d.getmSMERegistered());
		m.setUdyogAadharNumber(d.getUdyogAadharNumber());
		m.setAgencyPanNumber(d.getAgencyPanNumber());
		m.setUserGroup(d.getUserGroup());
		m.setChannelName(d.getChannelName());
		m.setProductType(d.getProductType());
		m.setBaseCPCProcessShop(d.getBaseCPCProcessShop());
		m.setMappedEmployeeID(d.getMappedEmployeeID());
		m.setMappedEmployeeName(d.getMappedEmployeeName());
		m.setInboundOutBoundType(d.getInboundOutBoundType());
		m.setMappedSolIDs(d.getMappedSolIDs());
		m.setCounsellorIDs(d.getCounsellorIDs());
		m.settSMID(d.gettSMID());
		m.settSMName(d.gettSMName());
		m.setChildAllowed(d.getChildAllowed());
		m.setOperatingLocations(d.getOperatingLocations());
		m.setRoleCode(d.getRoleCode());
		m.setvSTSID(d.getvSTSID());
		m.setvSTSName(d.getvSTSName());
		m.setvSTSStatus(d.getvSTSStatus());
		m.setProduct(d.getProduct());

		return m;
	}

}
