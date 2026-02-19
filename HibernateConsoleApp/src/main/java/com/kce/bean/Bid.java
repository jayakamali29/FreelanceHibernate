package com.kce.bean;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "BID_TBL")
@SequenceGenerator(
        name = "bid_seq",
        sequenceName = "bid_seq",
        allocationSize = 1
)
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bid_seq")
    @Column(name = "bidID")
    private int bidID;

    @Column(name = "projectID", nullable = false)
    private int projectID;

    @Column(name = "freelancerUserID", nullable = false, length = 50)
    private String freelancerUserID;

    @Column(name = "bidAmount", nullable = false, precision = 10, scale = 2)
    private BigDecimal bidAmount;

    @Column(name = "deliveryDays", nullable = false)
    private int deliveryDays;

    @Column(name = "coverLetter", length = 1000)
    private String coverLetter;

    @Column(name = "bidDate")
    private Date bidDate;

    @Column(name = "bidStatus", length = 20)
    private String bidStatus;

    public Bid() {}

    // Getters and Setters
    public int getBidID() { return bidID; }
    public void setBidID(int bidID) { this.bidID = bidID; }

    public int getProjectID() { return projectID; }
    public void setProjectID(int projectID) { this.projectID = projectID; }

    public String getFreelancerUserID() { return freelancerUserID; }
    public void setFreelancerUserID(String freelancerUserID) { this.freelancerUserID = freelancerUserID; }

    public BigDecimal getBidAmount() { return bidAmount; }
    public void setBidAmount(BigDecimal bidAmount) { this.bidAmount = bidAmount; }

    public int getDeliveryDays() { return deliveryDays; }
    public void setDeliveryDays(int deliveryDays) { this.deliveryDays = deliveryDays; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public Date getBidDate() { return bidDate; }
    public void setBidDate(Date bidDate) { this.bidDate = bidDate; }

    public String getBidStatus() { return bidStatus; }
    public void setBidStatus(String bidStatus) { this.bidStatus = bidStatus; }
}
