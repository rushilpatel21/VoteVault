package com.votingsystem.dao;

import com.votingsystem.model.Voter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoterDAO {
    private Connection connection;

    public VoterDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addVoter(Voter voter) {
        String sql = "INSERT INTO voter (name, age, address, region_id, status, username, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, voter.getName());
            statement.setInt(2, voter.getAge());
            statement.setString(3, voter.getAddress());
            statement.setInt(4, voter.getRegionId());
            statement.setString(5, voter.getStatus());
            statement.setString(6, voter.getUsername());
            statement.setString(7, voter.getPassword());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to insert voter, no rows affected.");
            }

            // Set the generated ID back to the voter object if insertion was successful
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    voter.setVoterId(generatedKeys.getInt(1));
                }
            }
            return true; // Insert successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Insert failed
        }
    }

    public List<Voter> getAllVoters() throws SQLException {
        List<Voter> voters = new ArrayList<>();
        String sql = "SELECT * FROM voter";

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Voter voter = new Voter(
                    resultSet.getInt("voter_id"),
                    resultSet.getString("name"),
                    resultSet.getInt("age"),
                    resultSet.getString("address"),
                    resultSet.getInt("region_id"),
                    resultSet.getString("status"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
                );
                voters.add(voter);
            }
        }
        return voters;
    }

    public boolean updateVoter(Voter voter) throws SQLException {
        String sql = "UPDATE voter SET name = ?, age = ?, address = ?, region_id = ?, status = ?, username = ?, password = ? WHERE voter_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, voter.getName());
            statement.setInt(2, voter.getAge());
            statement.setString(3, voter.getAddress());
            statement.setInt(4, voter.getRegionId());
            statement.setString(5, voter.getStatus());
            statement.setString(6, voter.getUsername());
            statement.setString(7, voter.getPassword());
            statement.setInt(8, voter.getVoterId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Returns true if at least one row was updated
        }
    }


    public boolean deleteVoter(int voterId) throws SQLException {
        String sql = "DELETE FROM voter WHERE voter_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, voterId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Returns true if at least one row was deleted
        }
    }

}
