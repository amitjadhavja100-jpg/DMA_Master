package com.icici.dma.model;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "TM_CCR_HOLD_CODE_MST_ERROR")
public class HoldCodeMasterError {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hold_code_error_seq")
    @SequenceGenerator(
        name = "hold_code_error_seq",
        sequenceName = "HOLD_CODE_ERROR_SEQ",
        allocationSize = 1
    )
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "HOLD_REASON")
    private String holdReason;

    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;

    @Column(name = "UPLOAD_ID")
    private String uploadId;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "ROW_NUMBER")
    private Integer rowNumber;

    public HoldCodeMasterError() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHoldReason() {
        return holdReason;
    }

    public void setHoldReason(String holdReason) {
        this.holdReason = holdReason;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }
}