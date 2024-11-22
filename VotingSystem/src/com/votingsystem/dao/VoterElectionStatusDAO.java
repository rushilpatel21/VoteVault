/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.votingsystem.dao;

/**
 *
 * @author rushi
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import com.votingsystem.model.VoterElectionStatus;


public class VoterElectionStatusDAO {
    private Connection connection;

    // Constructor to initialize the database connection
    public VoterElectionStatusDAO(Connection connection) {
        this.connection = connection;
    }

    // Method to insert a new record into the voter_election_status table
    public boolean insertVoterElectionStatus(VoterElectionStatus status) {
        String query = "INSERT INTO voter_election_status (voter_id, election_id, has_voted, vote_time) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, status.getVoterId());
            stmt.setInt(2, status.getElectionId());
            stmt.setBoolean(3, status.isHasVoted());
            stmt.setTimestamp(4, status.getVoteTime());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to check if a voter has voted in a particular election
    public VoterElectionStatus getVoterElectionStatus(int voterId, int electionId) {
        String query = "SELECT * FROM voter_election_status WHERE voter_id = ? AND election_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, voterId);
            stmt.setInt(2, electionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new VoterElectionStatus(
                        rs.getInt("voter_id"),
                        rs.getInt("election_id"),
                        rs.getBoolean("has_voted"),
                        rs.getTimestamp("vote_time")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // No record found
    }
    
    public List<VoterElectionStatus> getVoterElectionStatus(int voterId) {
        List<VoterElectionStatus> statuses = new ArrayList<>();
        String query = "SELECT * FROM voter_election_status WHERE voter_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, voterId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                statuses.add(new VoterElectionStatus(
                        rs.getInt("voter_id"),
                        rs.getInt("election_id"),
                        rs.getBoolean("has_voted"),
                        rs.getTimestamp("vote_time")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statuses;  // Return the list of statuses for the voter
    }

    // Method to update the voting status of a voter in a particular election
    public boolean updateVoterElectionStatus(boolean hasVoted, Timestamp voteTime, int voterId, int electionId) {
        String query = "INSERT INTO voter_election_status (voter_id, election_id, has_voted, vote_time) " +
                       "VALUES (?, ?, ?, ?) " +
                       "ON DUPLICATE KEY UPDATE has_voted = VALUES(has_voted), vote_time = VALUES(vote_time)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Set the boolean value for has_voted (TinyInt(1))
            stmt.setInt(1, voterId);

            // Set the election_id
            stmt.setInt(2, electionId);

            // Map boolean to TINYINT (1 for true, 0 for false)
            stmt.setInt(3, hasVoted ? 1 : 0);

            // Set the Timestamp value for vote_time
            stmt.setTimestamp(4, voteTime);

            // Execute the query
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;  // Return true if at least one row was inserted or updated
        } catch (SQLException e) {
            e.printStackTrace();  // Handle the exception appropriately
            return false;  // Return false in case of an error
        }
    }




    // Method to delete a voter election status record
    public boolean deleteVoterElectionStatus(int voterId, int electionId) {
        String query = "DELETE FROM voter_election_status WHERE voter_id = ? AND election_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, voterId);
            stmt.setInt(2, electionId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to get all voters' election statuses for a specific election
    public List<VoterElectionStatus> getVotersByElection(int electionId) {
        List<VoterElectionStatus> voterStatuses = new ArrayList<>();
        String query = "SELECT * FROM voter_election_status WHERE election_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, electionId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                VoterElectionStatus status = new VoterElectionStatus(
                        rs.getInt("voter_id"),
                        rs.getInt("election_id"),
                        rs.getBoolean("has_voted"),
                        rs.getTimestamp("vote_time")
                );
                voterStatuses.add(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voterStatuses;
    }
}
