package com.icici.dma.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

//@Entity
//@Table(name = "TM_CRD_CARD_BILLING")
public class CreditCardBilling {
	

	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_error_seq")
//	@SequenceGenerator(name = "branch_error_seq", sequenceName = "ISEQ$$_138497", allocationSize = 1)
	public Integer ccBillingId;
	
	//1
	@Column(name = "CUS_CITY_CODE")
	public String cusCityCode;
	
	//2
	@Column(name = "DPD")
	public Integer dpd;
	
	//3
	@Column(name = "BILL_CYCLE")
	public Integer billCycle;
	
	//4
	@Column(name = "BLOCK_CODE")
	public String blockCode;
	
	//5
	@Column(name = "ACCT")
	public Long acct;
	
	//6
	@Column(name = "MSD_CURR_BAL")
	public BigDecimal msdCurrBal;
	
	//7
	@Column(name = "PRINCIPAL_BALANCE")
	public Integer PrincipleBalance;
	
	//8
	@Column(name = "RETENTION")
	public Integer retention;
	
	//9
	@Column(name = "REASON")
	public Integer reason;
	

}
