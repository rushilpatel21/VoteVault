package com.votingsystem.model;

import java.sql.Timestamp;

public class Vote {
    private int voteId;
    private int voterId;
    private int electionId;
    private int candidateId;
    private Timestamp timestamp;

    // Constructor
    public Vote(int voteId, int voterId, int electionId, int candidateId, Timestamp timestamp) {
        this.voteId = voteId;
        this.voterId = voterId;
        this.electionId = electionId;
        this.candidateId = candidateId;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public int getVoterId() {
        return voterId;
    }

    public void setVoterId(int voterId) {
        this.voterId = voterId;
    }

    public int getElectionId() {
        return electionId;
    }

    public void setElectionId(int electionId) {
        this.electionId = electionId;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
