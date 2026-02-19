package com.kce.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS_TBL")
public class User {

    @Id
    @Column(name = "userID", length = 50)
    private String userID;

    @Column(name = "fullName", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "mobile", length = 15)
    private String mobile;

    @Column(name = "userRole", nullable = false, length = 20)
    private String userRole;

    @Column(name = "primarySkillOrCompany", length = 100)
    private String primarySkillOrCompany;

    @Column(name = "status", length = 20)
    private String status;

    // Required default constructor
    public User() {
    }

    // Getters and Setters

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getPrimarySkillOrCompany() {
        return primarySkillOrCompany;
    }

    public void setPrimarySkillOrCompany(String primarySkillOrCompany) {
        this.primarySkillOrCompany = primarySkillOrCompany;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
