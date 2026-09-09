package com.icici.dma.slabEntity.osp;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "TC_OSP_DPD_HISTORY")
public class OspConfigDpdHistory {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "osp_dpd_history_seq")
    @SequenceGenerator(
            name = "osp_dpd_history_seq",
            sequenceName = "TC_OSP_DPD_HISTORY_SEQ",
            allocationSize = 1)
    @Column(name = "HISTORY_ID")
    private Long historyId;

    @Column(name = "DPD_ID")
    private Long dpdId;

    @Column(name = "ACTION_TYPE")
    private String actionType;

    @Column(name = "OLD_DPD_NAME")
    private String oldDpdName;

    @Column(name = "NEW_DPD_NAME")
    private String newDpdName;


    @Column(name = "STATUS")
    private String status;

    @Column(name = "APPROVED_BY")
    private String approvedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPROVED_DATE")
    private Date approvedDate;

    @Column(name = "REMARKS")
    private String remarks;

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public Long getDpdId() {
        return dpdId;
    }

    public void setDpdId(Long dpdId) {
        this.dpdId = dpdId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getOldDpdName() {
        return oldDpdName;
    }

    public void setOldDpdName(String oldDpdName) {
        this.oldDpdName = oldDpdName;
    }

    public String getNewDpdName() {
        return newDpdName;
    }

    public void setNewDpdName(String newDpdName) {
        this.newDpdName = newDpdName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
