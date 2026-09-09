package com.icici.dma.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.OutsourceMasterDto;
import com.icici.dma.model.OutsourceMaster;
import com.icici.dma.model.OutsourceMasterTemp;

public interface Vhl_OutsourceMasterService {

	public Map<String, Object> uploadOutsourceMaster(MultipartFile file, String loginUser);

	public ByteArrayInputStream exportErrorExcel(String user) throws Exception;

	public void createOutsourceMasterByMaker(OutsourceMasterDto dto, String username);

	public void updateOutsourceMasterByMaker(OutsourceMasterDto dto, String username);

	public List<OutsourceMasterDto> getAllOutsourceMasterByChecker(String user);

	public void updateOutsourceMasterByChecker(CheckerDecisionReq requestPayload, String username);

	public List<OutsourceMasterDto> getOutsourceMasterByStatus(String statusType);

	/**
	 * @param d
	 * @return
	 */
	public default OutsourceMaster convertDtoToMain(OutsourceMasterDto d) {

		OutsourceMaster m = new OutsourceMaster();

//		m.setSrNo(d.getSrNo());
		m.setEmpCode(d.getEmpCode());
		m.setvSTSCodeCounselorSAPCode(d.getvSTSCodeCounselorSAPCode());
//		m.setOldVSTSCode(d.getOldVSTSCode());
		m.setExecutiveName(d.getExecutiveName());
//		m.setTotalSalary(d.getTotalSalary());
//		m.setTotalIncentive(d.getTotalIncentive());
//		m.setConveyanceforthemonth(d.getConveyanceforthemonth());
//		m.setAdditionalCost(d.getAdditionalCost());
//		m.setTotalPayout(d.getTotalPayout());
//		m.setMainDesignation(d.getMainDesignation());
//		m.setDesignation(d.getDesignation());
//		m.setOldDesignation(d.getOldDesignation());
//		m.setPrimaryProduct(d.getPrimaryProduct());
//		m.setAgencyName(d.getAgencyName());
//		m.setLocation(d.getLocation());
//		m.setOldLocation(d.getOldLocation());
//		m.setZone(d.getZone());
//		m.seteDState(d.geteDState());
//		m.seteDZone(d.geteDZone());
//		m.setmISState(d.getmISState());
//		m.setcState(d.getcState());
//		m.setMonth(d.getMonth());
//		m.setRemark(d.getRemark());
//		m.setiBox(d.getiBox());
//		m.setIprocessDesignation(d.getIprocessDesignation());
//		m.setdOJ(d.getdOJ());
//		m.setTopTierII(d.getTopTierII());
//		m.setOldTopTierII(d.getOldTopTierII());
//		m.setNoOfDay(d.getNoOfDay());
//		m.setArrearDays(d.getArrearDays());
//		m.setBand(d.getBand());
//		m.setInvoiceNo(d.getInvoiceNo());
//		m.setInvoiceDate(d.getInvoiceDate());
//		m.setInvoiceAmt(d.getInvoiceAmt());
//		m.setInvoiceRecdOn(d.getInvoiceRecdOn());
//		m.setResigneddate(d.getResigneddate());
//		m.setePF(d.getePF());
//		m.seteESI(d.geteESI());
//		m.seteLWF(d.geteLWF());
//		m.setRoundingOffDueToEmployerContrToESIC(d.getRoundingOffDueToEmployerContrToESIC());
//		m.setRecoveryAmtRecd(d.getRecoveryAmtRecd());
//		m.setNoticePayDed(d.getNoticePayDed());
//		m.setTotal(d.getTotal());
//		m.setExGratiaBonus(d.getExGratiaBonus());
//		m.setTotalWithIncentiveAndCON(d.getTotalWithIncentiveAndCON());
//		m.setServiceChgsOnTotalCost(d.getServiceChgsOnTotalCost());
//		m.setTotalWithSC(d.getTotalWithSC());
//		m.setgST(d.getgST());
//		m.setTotalBillAmt(d.getTotalBillAmt());
//		m.setTotalSalaryAndIncentiveAndConv(d.getTotalSalaryAndIncentiveAndConv());
//		m.setAdditionalCost(d.getAdditionalCost());
//		m.setAsPerIrProcessLocation(d.getAsPerIrProcessLocation());
//		m.setAsPerIProcessState(d.getAsPerIProcessState());
//		m.setLotNo(d.getLotNo());
//		m.setIprocessGrossSalary(d.getIprocessGrossSalary());
//		m.setSolId(d.getSolId());
//		m.setMailFrom(d.getMailFrom());
//		m.setNoOfCasesNew(d.getNoOfCasesNew());
//		m.setNoOfCasesTotal(d.getNoOfCasesTotal());
//		m.setNoOfCasesUsed(d.getNoOfCasesUsed());
//		m.setLoanMnsNew(d.getLoanMnsNew());
//		m.setLoanMnsTotal(d.getLoanMnsTotal());
//		m.setLoanMnsUsed(d.getLoanMnsUsed());

		return m;
	}

	public default OutsourceMasterTemp convertDtoToTemp(OutsourceMasterDto d) {

		OutsourceMasterTemp m = new OutsourceMasterTemp();

//		m.setSrNo(d.getSrNo());
		m.setEmpCode(d.getEmpCode());
		m.setvSTSCodeCounselorSAPCode(d.getvSTSCodeCounselorSAPCode());
//		m.setOldVSTSCode(d.getOldVSTSCode());
		m.setExecutiveName(d.getExecutiveName());
//		m.setTotalSalary(d.getTotalSalary());
//		m.setTotalIncentive(d.getTotalIncentive());
//		m.setConveyanceforthemonth(d.getConveyanceforthemonth());
//		m.setAdditionalCost(d.getAdditionalCost());
//		m.setTotalPayout(d.getTotalPayout());
//		m.setMainDesignation(d.getMainDesignation());
//		m.setDesignation(d.getDesignation());
//		m.setOldDesignation(d.getOldDesignation());
//		m.setPrimaryProduct(d.getPrimaryProduct());
//		m.setAgencyName(d.getAgencyName());
//		m.setLocation(d.getLocation());
//		m.setOldLocation(d.getOldLocation());
//		m.setZone(d.getZone());
//		m.seteDState(d.geteDState());
//		m.seteDZone(d.geteDZone());
//		m.setmISState(d.getmISState());
//		m.setcState(d.getcState());
//		m.setMonth(d.getMonth());
//		m.setRemark(d.getRemark());
//		m.setiBox(d.getiBox());
//		m.setIprocessDesignation(d.getIprocessDesignation());
//		m.setdOJ(d.getdOJ());
//		m.setTopTierII(d.getTopTierII());
//		m.setOldTopTierII(d.getOldTopTierII());
//		m.setNoOfDay(d.getNoOfDay());
//		m.setArrearDays(d.getArrearDays());
//		m.setBand(d.getBand());
//		m.setInvoiceNo(d.getInvoiceNo());
//		m.setInvoiceDate(d.getInvoiceDate());
//		m.setInvoiceAmt(d.getInvoiceAmt());
//		m.setInvoiceRecdOn(d.getInvoiceRecdOn());
//		m.setResigneddate(d.getResigneddate());
//		m.setePF(d.getePF());
//		m.seteESI(d.geteESI());
//		m.seteLWF(d.geteLWF());
//		m.setRoundingOffDueToEmployerContrToESIC(d.getRoundingOffDueToEmployerContrToESIC());
//		m.setRecoveryAmtRecd(d.getRecoveryAmtRecd());
//		m.setNoticePayDed(d.getNoticePayDed());
//		m.setTotal(d.getTotal());
//		m.setExGratiaBonus(d.getExGratiaBonus());
//		m.setTotalWithIncentiveAndCON(d.getTotalWithIncentiveAndCON());
//		m.setServiceChgsOnTotalCost(d.getServiceChgsOnTotalCost());
//		m.setTotalWithSC(d.getTotalWithSC());
//		m.setgST(d.getgST());
//		m.setTotalBillAmt(d.getTotalBillAmt());
//		m.setTotalSalaryAndIncentiveAndConv(d.getTotalSalaryAndIncentiveAndConv());
//		m.setAdditionalCost(d.getAdditionalCost());
//		m.setAsPerIrProcessLocation(d.getAsPerIrProcessLocation());
//		m.setAsPerIProcessState(d.getAsPerIProcessState());
//		m.setLotNo(d.getLotNo());
//		m.setIprocessGrossSalary(d.getIprocessGrossSalary());
//		m.setSolId(d.getSolId());
//		m.setMailFrom(d.getMailFrom());
//		m.setNoOfCasesNew(d.getNoOfCasesNew());
//		m.setLoanMnsNew(d.getLoanMnsNew());
//		m.setNoOfCasesUsed(d.getNoOfCasesUsed());
//		m.setLoanMnsUsed(d.getLoanMnsUsed());
//		m.setNoOfCasesTotal(d.getNoOfCasesTotal());
//		m.setLoanMnsTotal(d.getLoanMnsTotal());

		return m;
	}

	public default OutsourceMasterDto convertTempToDto(OutsourceMasterTemp d) {

		OutsourceMasterDto m = new OutsourceMasterDto();

//		m.setSrNo(d.getSrNo());
		m.setEmpCode(d.getEmpCode());
		m.setvSTSCodeCounselorSAPCode(d.getvSTSCodeCounselorSAPCode());
//		m.setOldVSTSCode(d.getOldVSTSCode());
		m.setExecutiveName(d.getExecutiveName());
//		m.setTotalSalary(d.getTotalSalary());
//		m.setTotalIncentive(d.getTotalIncentive());
//		m.setConveyanceforthemonth(d.getConveyanceforthemonth());
//		m.setAdditionalCost(d.getAdditionalCost());
//		m.setTotalPayout(d.getTotalPayout());
//		m.setMainDesignation(d.getMainDesignation());
//		m.setDesignation(d.getDesignation());
//		m.setOldDesignation(d.getOldDesignation());
//		m.setPrimaryProduct(d.getPrimaryProduct());
//		m.setAgencyName(d.getAgencyName());
//		m.setLocation(d.getLocation());
//		m.setOldLocation(d.getOldLocation());
//		m.setZone(d.getZone());
//		m.seteDState(d.geteDState());
//		m.seteDZone(d.geteDZone());
//		m.setmISState(d.getmISState());
//		m.setcState(d.getcState());
//		m.setMonth(d.getMonth());
//		m.setRemark(d.getRemark());
//		m.setiBox(d.getiBox());
//		m.setIprocessDesignation(d.getIprocessDesignation());
//		m.setdOJ(d.getdOJ());
//		m.setTopTierII(d.getTopTierII());
//		m.setOldTopTierII(d.getOldTopTierII());
//		m.setNoOfDay(d.getNoOfDay());
//		m.setArrearDays(d.getArrearDays());
//		m.setBand(d.getBand());
//		m.setInvoiceNo(d.getInvoiceNo());
//		m.setInvoiceDate(d.getInvoiceDate());
//		m.setInvoiceAmt(d.getInvoiceAmt());
//		m.setInvoiceRecdOn(d.getInvoiceRecdOn());
//		m.setResigneddate(d.getResigneddate());
//		m.setePF(d.getePF());
//		m.seteESI(d.geteESI());
//		m.seteLWF(d.geteLWF());
//		m.setRoundingOffDueToEmployerContrToESIC(d.getRoundingOffDueToEmployerContrToESIC());
//		m.setRecoveryAmtRecd(d.getRecoveryAmtRecd());
//		m.setNoticePayDed(d.getNoticePayDed());
//		m.setTotal(d.getTotal());
//		m.setExGratiaBonus(d.getExGratiaBonus());
//		m.setTotalWithIncentiveAndCON(d.getTotalWithIncentiveAndCON());
//		m.setServiceChgsOnTotalCost(d.getServiceChgsOnTotalCost());
//		m.setTotalWithSC(d.getTotalWithSC());
//		m.setgST(d.getgST());
//		m.setTotalBillAmt(d.getTotalBillAmt());
//		m.setTotalSalaryAndIncentiveAndConv(d.getTotalSalaryAndIncentiveAndConv());
//		m.setAdditionalCost(d.getAdditionalCost());
//		m.setAsPerIrProcessLocation(d.getAsPerIrProcessLocation());
//		m.setAsPerIProcessState(d.getAsPerIProcessState());
//		m.setLotNo(d.getLotNo());
//		m.setIprocessGrossSalary(d.getIprocessGrossSalary());
//		m.setSolId(d.getSolId());
//		m.setMailFrom(d.getMailFrom());
//		m.setNoOfCasesNew(d.getNoOfCasesNew());
//		m.setLoanMnsNew(d.getLoanMnsNew());
//		m.setNoOfCasesUsed(d.getNoOfCasesUsed());
//		m.setLoanMnsUsed(d.getLoanMnsUsed());
//		m.setNoOfCasesTotal(d.getNoOfCasesTotal());
//		m.setLoanMnsTotal(d.getLoanMnsTotal());

		m.setStatus(d.getStatus());
		return m;
	}

	public default OutsourceMasterDto convertMainToDto(OutsourceMaster d) {

		OutsourceMasterDto m = new OutsourceMasterDto();

//		m.setSrNo(d.getSrNo());
		m.setEmpCode(d.getEmpCode());
		m.setvSTSCodeCounselorSAPCode(d.getvSTSCodeCounselorSAPCode());
//		m.setOldVSTSCode(d.getOldVSTSCode());
		m.setExecutiveName(d.getExecutiveName());
//		m.setTotalSalary(d.getTotalSalary());
//		m.setTotalIncentive(d.getTotalIncentive());
//		m.setConveyanceforthemonth(d.getConveyanceforthemonth());
//		m.setAdditionalCost(d.getAdditionalCost());
//		m.setTotalPayout(d.getTotalPayout());
//		m.setMainDesignation(d.getMainDesignation());
//		m.setDesignation(d.getDesignation());
//		m.setOldDesignation(d.getOldDesignation());
//		m.setPrimaryProduct(d.getPrimaryProduct());
//		m.setAgencyName(d.getAgencyName());
//		m.setLocation(d.getLocation());
//		m.setOldLocation(d.getOldLocation());
//		m.setZone(d.getZone());
//		m.seteDState(d.geteDState());
//		m.seteDZone(d.geteDZone());
//		m.setmISState(d.getmISState());
//		m.setcState(d.getcState());
//		m.setMonth(d.getMonth());
//		m.setRemark(d.getRemark());
//		m.setiBox(d.getiBox());
//		m.setIprocessDesignation(d.getIprocessDesignation());
//		m.setdOJ(d.getdOJ());
//		m.setTopTierII(d.getTopTierII());
//		m.setOldTopTierII(d.getOldTopTierII());
//		m.setNoOfDay(d.getNoOfDay());
//		m.setArrearDays(d.getArrearDays());
//		m.setBand(d.getBand());
//		m.setInvoiceNo(d.getInvoiceNo());
//		m.setInvoiceDate(d.getInvoiceDate());
//		m.setInvoiceAmt(d.getInvoiceAmt());
//		m.setInvoiceRecdOn(d.getInvoiceRecdOn());
//		m.setResigneddate(d.getResigneddate());
//		m.setePF(d.getePF());
//		m.seteESI(d.geteESI());
//		m.seteLWF(d.geteLWF());
//		m.setRoundingOffDueToEmployerContrToESIC(d.getRoundingOffDueToEmployerContrToESIC());
//		m.setRecoveryAmtRecd(d.getRecoveryAmtRecd());
//		m.setNoticePayDed(d.getNoticePayDed());
//		m.setTotal(d.getTotal());
//		m.setExGratiaBonus(d.getExGratiaBonus());
//		m.setTotalWithIncentiveAndCON(d.getTotalWithIncentiveAndCON());
//		m.setServiceChgsOnTotalCost(d.getServiceChgsOnTotalCost());
//		m.setTotalWithSC(d.getTotalWithSC());
//		m.setgST(d.getgST());
//		m.setTotalBillAmt(d.getTotalBillAmt());
//		m.setTotalSalaryAndIncentiveAndConv(d.getTotalSalaryAndIncentiveAndConv());
//		m.setAdditionalCost(d.getAdditionalCost());
//		m.setAsPerIrProcessLocation(d.getAsPerIrProcessLocation());
//		m.setAsPerIProcessState(d.getAsPerIProcessState());
//		m.setLotNo(d.getLotNo());
//		m.setIprocessGrossSalary(d.getIprocessGrossSalary());
//		m.setSolId(d.getSolId());
//		m.setMailFrom(d.getMailFrom());
//		m.setNoOfCasesNew(d.getNoOfCasesNew());
//		m.setNoOfCasesTotal(d.getNoOfCasesTotal());
//		m.setNoOfCasesUsed(d.getNoOfCasesUsed());
//		m.setLoanMnsNew(d.getLoanMnsNew());
//		m.setLoanMnsTotal(d.getLoanMnsTotal());
//		m.setLoanMnsUsed(d.getLoanMnsUsed());

		m.setStatus(d.getStatus());

		return m;
	}

	public default OutsourceMaster convertTempToMain(OutsourceMasterTemp d) {

		OutsourceMaster m = new OutsourceMaster();

//		m.setSrNo(d.getSrNo());
		m.setEmpCode(d.getEmpCode());
		m.setvSTSCodeCounselorSAPCode(d.getvSTSCodeCounselorSAPCode());
//		m.setOldVSTSCode(d.getOldVSTSCode());
//		m.setExecutiveName(d.getExecutiveName());
//		m.setTotalSalary(d.getTotalSalary());
//		m.setTotalIncentive(d.getTotalIncentive());
//		m.setConveyanceforthemonth(d.getConveyanceforthemonth());
//		m.setAdditionalCost(d.getAdditionalCost());
//		m.setTotalPayout(d.getTotalPayout());
//		m.setMainDesignation(d.getMainDesignation());
//		m.setDesignation(d.getDesignation());
//		m.setOldDesignation(d.getOldDesignation());
//		m.setPrimaryProduct(d.getPrimaryProduct());
//		m.setAgencyName(d.getAgencyName());
//		m.setLocation(d.getLocation());
//		m.setOldLocation(d.getOldLocation());
//		m.setZone(d.getZone());
//		m.seteDState(d.geteDState());
//		m.seteDZone(d.geteDZone());
//		m.setmISState(d.getmISState());
//		m.setcState(d.getcState());
//		m.setMonth(d.getMonth());
//		m.setRemark(d.getRemark());
//		m.setiBox(d.getiBox());
//		m.setIprocessDesignation(d.getIprocessDesignation());
//		m.setdOJ(d.getdOJ());
//		m.setTopTierII(d.getTopTierII());
//		m.setOldTopTierII(d.getOldTopTierII());
//		m.setNoOfDay(d.getNoOfDay());
//		m.setArrearDays(d.getArrearDays());
//		m.setBand(d.getBand());
//		m.setInvoiceNo(d.getInvoiceNo());
//		m.setInvoiceDate(d.getInvoiceDate());
//		m.setInvoiceAmt(d.getInvoiceAmt());
//		m.setInvoiceRecdOn(d.getInvoiceRecdOn());
//		m.setResigneddate(d.getResigneddate());
//		m.setePF(d.getePF());
//		m.seteESI(d.geteESI());
//		m.seteLWF(d.geteLWF());
//		m.setRoundingOffDueToEmployerContrToESIC(d.getRoundingOffDueToEmployerContrToESIC());
//		m.setRecoveryAmtRecd(d.getRecoveryAmtRecd());
//		m.setNoticePayDed(d.getNoticePayDed());
//		m.setTotal(d.getTotal());
//		m.setExGratiaBonus(d.getExGratiaBonus());
//		m.setTotalWithIncentiveAndCON(d.getTotalWithIncentiveAndCON());
//		m.setServiceChgsOnTotalCost(d.getServiceChgsOnTotalCost());
//		m.setTotalWithSC(d.getTotalWithSC());
//		m.setgST(d.getgST());
//		m.setTotalBillAmt(d.getTotalBillAmt());
//		m.setTotalSalaryAndIncentiveAndConv(d.getTotalSalaryAndIncentiveAndConv());
//		m.setAdditionalCost(d.getAdditionalCost());
//		m.setAsPerIrProcessLocation(d.getAsPerIrProcessLocation());
//		m.setAsPerIProcessState(d.getAsPerIProcessState());
//		m.setLotNo(d.getLotNo());
//		m.setIprocessGrossSalary(d.getIprocessGrossSalary());
//		m.setSolId(d.getSolId());
//		m.setMailFrom(d.getMailFrom());
//		m.setNoOfCasesNew(d.getNoOfCasesNew());
//		m.setNoOfCasesTotal(d.getNoOfCasesTotal());
//		m.setNoOfCasesUsed(d.getNoOfCasesUsed());
//		m.setLoanMnsNew(d.getLoanMnsNew());
//		m.setLoanMnsTotal(d.getLoanMnsTotal());
//		m.setLoanMnsUsed(d.getLoanMnsUsed());

		return m;
	}

}
