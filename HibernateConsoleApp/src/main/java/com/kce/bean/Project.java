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
@Table(name = "PROJECT_TBL")
@SequenceGenerator(
        name = "project_seq",
        sequenceName = "project_seq",
        allocationSize = 1
)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_seq")
    @Column(name = "projectID")
    private int projectID;

    @Column(name = "clientUserID", nullable = false, length = 50)
    private String clientUserID;

    @Column(name = "projectTitle", nullable = false, length = 200)
    private String projectTitle;

    @Column(name = "projectDescription", length = 2000)
    private String projectDescription;

    @Column(name = "budgetMin", nullable = false, precision = 10, scale = 2)
    private BigDecimal budgetMin;

    @Column(name = "budgetMax", nullable = false, precision = 10, scale = 2)
    private BigDecimal budgetMax;

    @Column(name = "postedDate")
    private Date postedDate;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "awardedBidID")
    private Integer awardedBidID;

    public Project() {}

    // Getters and Setters
    public int getProjectID() { return projectID; }
    public void setProjectID(int projectID) { this.projectID = projectID; }

    public String getClientUserID() { return clientUserID; }
    public void setClientUserID(String clientUserID) { this.clientUserID = clientUserID; }

    public String getProjectTitle() { return projectTitle; }
    public void setProjectTitle(String projectTitle) { this.projectTitle = projectTitle; }

    public String getProjectDescription() { return projectDescription; }
    public void setProjectDescription(String projectDescription) { this.projectDescription = projectDescription; }

    public BigDecimal getBudgetMin() { return budgetMin; }
    public void setBudgetMin(BigDecimal budgetMin) { this.budgetMin = budgetMin; }

    public BigDecimal getBudgetMax() { return budgetMax; }
    public void setBudgetMax(BigDecimal budgetMax) { this.budgetMax = budgetMax; }

    public Date getPostedDate() { return postedDate; }
    public void setPostedDate(Date postedDate) { this.postedDate = postedDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getAwardedBidID() { return awardedBidID; }
    public void setAwardedBidID(Integer awardedBidID) { this.awardedBidID = awardedBidID; }
}
