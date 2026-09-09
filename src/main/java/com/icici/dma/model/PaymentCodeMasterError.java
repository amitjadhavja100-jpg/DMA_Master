package com.icici.dma.model;

import java.util.Date;
import javax.persistence.*;

//snz

@Entity
@Table(name = "TM_CCR_PAYMENT_CODE_MST_ERROR")
public class PaymentCodeMasterError {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_code_error_seq")
    @SequenceGenerator(
        name = "payment_code_error_seq",
        sequenceName = "PAYMENT_CODE_ERROR_SEQ",
        allocationSize = 1
    )
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CODES")
    private String code;

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

    public PaymentCodeMasterError() {
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