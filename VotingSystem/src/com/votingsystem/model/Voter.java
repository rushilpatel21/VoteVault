package com.votingsystem.model;

public class Voter {
    private int voterId;
    private String name;
    private int age;
    private String address;
    private int regionId;
    private String status;
    private String username;
    private String password;

    // Constructor
    public Voter(int voterId, String name, int age, String address, int regionId, String status, String username, String password) {
        this.voterId = voterId;
        this.name = name;
        this.age = age;
        this.address = address;
        this.regionId = regionId;
        this.status = status;
        this.username = username;
        this.password = password;
    }
    public Voter() {
    }
    // Getters and Setters
    public int getVoterId() {
        return voterId;
    }

    public void setVoterId(int voterId) {
        this.voterId = voterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
