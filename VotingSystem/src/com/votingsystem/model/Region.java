package com.votingsystem.model;

public class Region {
    private int regionId;
    private String regionName;
    private String description;

    // Constructor
    public Region(int regionId, String regionName, String description) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.description = description;
    }

    // Getters and Setters
    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
