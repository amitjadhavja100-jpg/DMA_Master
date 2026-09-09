package com.icici.dma.slabEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CONFIG_HISTORY")
public class ConfigHistory {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "config_history_gen")
    @SequenceGenerator(
            name = "config_history_gen",
            sequenceName = "CONFIG_HISTORY_SEQ",
            allocationSize = 1)
    @Column(name = "HISTORY_ID")
    private Long historyId;

    @Column(name = "CONFIG_ID")
    private Long configId;

    @Column(name = "MODULE_NAME")
    private String moduleName;

    @Column(name = "SCREEN_NAME")
    private String screenName;

    @Column(name = "CONFIG_TYPE")
    private String configType;

    @Column(name = "CONFIG_KEY")
    private String configKey;

    @Column(name = "ACTION_TYPE")
    private String actionType;

    @Column(name = "OLD_VALUE")
    private String oldValue;

    @Column(name = "NEW_VALUE")
    private String newValue;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CHECKER_ID")
    private String checkerId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHECKER_DATE")
    private Date checkerDate;

    @Column(name = "REMARKS")
    private String remarks;
    
    @Column(name="CATEGORY")
    private String category;

    @Column(name="CITY")
    private String city;

    @Column(name="DPD")
    private String dpd;
    
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDpd() {
		return dpd;
	}

	public void setDpd(String dpd) {
		this.dpd = dpd;
	}

	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	public Long getConfigId() {
		return configId;
	}

	public void setConfigId(Long configId) {
		this.configId = configId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCheckerId() {
		return checkerId;
	}

	public void setCheckerId(String checkerId) {
		this.checkerId = checkerId;
	}

	public Date getCheckerDate() {
		return checkerDate;
	}

	public void setCheckerDate(Date checkerDate) {
		this.checkerDate = checkerDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

    
}
