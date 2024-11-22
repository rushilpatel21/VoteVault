package com.votingsystem.model;

import java.sql.Timestamp;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class VoterElectionStatus {
    private int voterId;
    private int electionId;
    private boolean hasVoted;
    private Timestamp voteTime;

    // Constructor
    public VoterElectionStatus(int voterId, int electionId, boolean hasVoted, Timestamp voteTime) {
        this.voterId = voterId;
        this.electionId = electionId;
        this.hasVoted = hasVoted;
        this.voteTime = voteTime;
    }

    // Getters and Setters
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

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public Timestamp getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(Timestamp voteTime) {
        this.voteTime = voteTime;
    }

    @Override
    public String toString() {
        return "VoterElectionStatus{" +
                "voterId=" + voterId +
                ", electionId=" + electionId +
                ", hasVoted=" + hasVoted +
                ", voteTime=" + voteTime +
                '}';
    }
}
