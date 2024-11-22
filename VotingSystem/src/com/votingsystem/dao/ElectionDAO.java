package com.votingsystem.dao;

import com.votingsystem.model.Election;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ElectionDAO {
    private Connection connection;

    public ElectionDAO(Connection connection) {
        this.connection = connection;
    }

    // Method to add an election
    public boolean addElection(Election election) throws SQLException {
        String sql = "INSERT INTO elections (election_name, start_date, end_date, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, election.getElectionName());
            statement.setDate(2, election.getStartDate());
            statement.setDate(3, election.getEndDate());
            statement.setString(4, election.getStatus());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Adding election failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    election.setElectionId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to add election, no ID obtained.");
                }
            }
            return true;
        }
    }
    
    // Method to get the election ID by election name
    public int getElectionIdByName(String electionName) throws SQLException {
        String query = "SELECT election_id FROM elections WHERE election_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, electionName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("election_id");
                }
            }
        }
        return -1;  // Return -1 if not found
    }
    
    // Method to get the election name by election ID
    public String getElectionNameById(int electionId) throws SQLException {
        String query = "SELECT election_name FROM elections WHERE election_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, electionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("election_name");
                }
            }
        }
        return null;  // Return null if not found
    }
    
    // Method to get all elections
    public List<Election> getAllElections() throws SQLException {
        List<Election> elections = new ArrayList<>();
        String sql = "SELECT * FROM elections";

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Election election = new Election(
                    resultSet.getInt("election_id"),  // Use election_id as the primary key
                    resultSet.getString("election_name"),
                    resultSet.getDate("start_date"),
                    resultSet.getDate("end_date"),
                    resultSet.getString("status")
                );
                elections.add(election);
            }
        }
        return elections;
    }

    // Method to update election details
    public void updateElection(Election election) throws SQLException {
        String sql = "UPDATE elections SET election_name = ?, start_date = ?, end_date = ?, status = ? WHERE election_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, election.getElectionName());
            statement.setDate(2, election.getStartDate());
            statement.setDate(3, election.getEndDate());
            statement.setString(4, election.getStatus());
            statement.setInt(5, election.getElectionId());  // Use election_id as the primary key
            statement.executeUpdate();
        }
    }

    // Method to delete election by election_id
    public void deleteElection(int electionId) throws SQLException {
        String sql = "DELETE FROM elections WHERE election_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, electionId);  // Use election_id as the primary key
            statement.executeUpdate();
        }
    }
    // Add this method in the ElectionDAO class
    public List<Integer> getRegionIdsForElection(int electionId) throws SQLException {
        List<Integer> regionIds = new ArrayList<>();
        String sql = "SELECT DISTINCT region_id FROM candidates WHERE election_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, electionId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    regionIds.add(resultSet.getInt("region_id"));
                }
            }
        }
        return regionIds;
    }

    
}
