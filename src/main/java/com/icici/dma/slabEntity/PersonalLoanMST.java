package com.icici.dma.slabEntity;
 
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
 
import com.fasterxml.jackson.annotation.JsonFormat;
 
@Entity
@Table(name = "PAYOUT_SLAB_PERSONAL_LOAN_MST")
public class PersonalLoanMST {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(name = "payout_type")
    private String payoutType; // 'A', 'B', 'C'
 
    @Column(name = "min_val")
    private String min;
 
    @Column(name = "max_val")
    private String max;
 
    @Column(name = "payout")
    private String payout; // e.g., "2.07%"
 
    @Column(name = "status")
    private String status = "Approved";
 
    @Column(name = "category")
    private String category;
 
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "CYCLE_FROM")
    private Date cycleFrom;
 
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "CYCLE_TO")
    private Date cycleTo;
 
    // Getters and Setters
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public String getPayoutType() {
        return payoutType;
    }
 
    public void setPayoutType(String payoutType) {
        this.payoutType = payoutType;
    }
 
    public String getMin() {
        return min;
    }
 
    public void setMin(String min) {
        this.min = min;
    }
 
    public String getMax() {
        return max;
    }
 
    public void setMax(String max) {
        this.max = max;
    }
 
    public String getPayout() {
        return payout;
    }
 
    public void setPayout(String payout) {
        this.payout = payout;
    }
 
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }
 
    public String getCategory() {
        return category;
    }
 
    public void setCategory(String category) {
        this.category = category;
    }
 
    public Date getCycleFrom() {
        return cycleFrom;
    }
 
    public void setCycleFrom(Date cycleFrom) {
        this.cycleFrom = cycleFrom;
    }
 
    public Date getCycleTo() {
        return cycleTo;
    }
 
    public void setCycleTo(Date cycleTo) {
        this.cycleTo = cycleTo;
    }
}
 