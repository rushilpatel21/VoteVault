package com.votingsystem.model;

public class Candidate {
    private int candidateId;
    private int electionId;
    private int partyId;
    private int regionId;
    private int memberId;
    private int votesCount;
    private String candidateName;  // New field for candidate name
    private String partyName;      // New field for party name

    // Constructor
 
    
    public Candidate(int candidateId, int electionId, int partyId, int regionId, int memberId, int votesCount, String candidateName, String partyName) {
        this.candidateId = candidateId;
        this.electionId = electionId;
        this.partyId = partyId;
        this.regionId = regionId;
        this.memberId = memberId;
        this.votesCount = votesCount;
        this.candidateName = candidateName;
        this.partyName = partyName;
    }

    // Getters and Setters
    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public int getElectionId() {
        return electionId;
    }

    public void setElectionId(int electionId) {
        this.electionId = electionId;
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

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(int votesCount) {
        this.votesCount = votesCount;
    }

    // New Getters and Setters for candidate_name and party_name
    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }
}
