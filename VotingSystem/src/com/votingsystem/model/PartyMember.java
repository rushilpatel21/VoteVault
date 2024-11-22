package com.votingsystem.model;

import java.sql.Date;

public class PartyMember {
    private int memberId;
    private int partyId;
    private int regionId;
    private String name;
    private String position;
    private String photoPath;
    private String contactInfo;
    private Date joiningDate;

    // Constructor
    public PartyMember(int memberId, int partyId, int regionId, String name, String position, 
                       String photoPath, String contactInfo, Date joiningDate) {
        this.memberId = memberId;
        this.partyId = partyId;
        this.regionId = regionId;
        this.name = name;
        this.position = position;
        this.photoPath = photoPath;
        this.contactInfo = contactInfo;
        this.joiningDate = joiningDate;
    }

    // Getters and Setters
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int partyId) {
        this.partyId = partyId;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }
}
