package com.onemoney.recgardecardservice.service.dto;


import java.io.Serializable;

public class AccountDTO implements Serializable {

    private Long id;                // Correspond à "Long id"
    private Long userId;            // Correspond à "Long userId"
    private String accountNumber;   // Correspond à "String accountNumber"
    private String accountType;     // "WALLET", "BANK", "MERCHANT"
    private String status;          // "ACTIVE", "INACTIVE"

    // Constructeurs
    public AccountDTO() {
    }

    public AccountDTO(Long id, Long userId, String accountNumber, String accountType, String status) {
        this.id = id;
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.status = status;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
            "id=" + id +
            ", userId=" + userId +
            ", accountNumber='" + accountNumber + '\'' +
            ", accountType='" + accountType + '\'' +
            ", status='" + status + '\'' +
            '}';
    }
}
