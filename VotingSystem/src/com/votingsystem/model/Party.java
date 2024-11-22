package com.votingsystem.model;

import java.sql.Date;

public class Party {
    private int partyId;
    private String partyName;
    private Date foundingDate;
    private String logoPath;
    private String description;
    private String status;

    // Constructor
    public Party(int partyId, String partyName, Date foundingDate, String logoPath, String description, String status) {
        this.partyId = partyId;
        this.partyName = partyName;
        this.foundingDate = foundingDate;
        this.logoPath = logoPath;
        this.description = description;
        this.status = status;
    }

    // Getters and Setters
    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public Date getFoundingDate() {
        return foundingDate;
    }

    public void setFoundingDate(Date foundingDate) {
        this.foundingDate = foundingDate;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
