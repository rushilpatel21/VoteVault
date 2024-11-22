package com.votingsystem.dao;

import com.votingsystem.model.Candidate;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateDAO {
    private Connection connection;

    public CandidateDAO(Connection connection) {
        this.connection = connection;
    }

    // Method to add a candidate, now with candidate_name and party_name
    public void addCandidate(Candidate candidate) throws SQLException {
        String sql = "INSERT INTO candidates (election_id, party_id, region_id, member_id, votes_count, candidate_name, party_name) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, candidate.getElectionId());
            statement.setInt(2, candidate.getPartyId());
            statement.setInt(3, candidate.getRegionId());
            statement.setInt(4, candidate.getMemberId());
            statement.setInt(5, candidate.getVotesCount());
            statement.setString(6, candidate.getCandidateName());  // Set candidate_name
            statement.setString(7, candidate.getPartyName());      // Set party_name
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    candidate.setCandidateId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to insert candidate, no ID obtained.");
                }
            }
        }
    }
    public void incrementVoteCount(int candidateId) throws SQLException {
    String sql = "UPDATE candidates SET votes_count = votes_count + 1 WHERE candidate_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, candidateId);
        stmt.executeUpdate();
    }
}

    

    // Method to get all candidates, now with candidate_name and party_name
    public List<Candidate> getAllCandidates() throws SQLException {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "SELECT * FROM candidates";

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Candidate candidate = new Candidate(
                    resultSet.getInt("candidate_id"),
                    resultSet.getInt("election_id"),
                    resultSet.getInt("party_id"),
                    resultSet.getInt("region_id"),
                    resultSet.getInt("member_id"),
                    resultSet.getInt("votes_count"),
                    resultSet.getString("candidate_name"),  // Fetch candidate_name
                    resultSet.getString("party_name")       // Fetch party_name
                );
                candidates.add(candidate);
            }
        }
        return candidates;
    }

    // Method to update candidate, now includes candidate_name and party_name
    public void updateCandidate(Candidate candidate) throws SQLException {
        String sql = "UPDATE candidates SET election_id = ?, party_id = ?, region_id = ?, member_id = ?, votes_count = ?, candidate_name = ?, party_name = ? WHERE candidate_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, candidate.getElectionId());
            statement.setInt(2, candidate.getPartyId());
            statement.setInt(3, candidate.getRegionId());
            statement.setInt(4, candidate.getMemberId());
            statement.setInt(5, candidate.getVotesCount());
            statement.setString(6, candidate.getCandidateName());  // Set candidate_name
            statement.setString(7, candidate.getPartyName());      // Set party_name
            statement.setInt(8, candidate.getCandidateId());
            statement.executeUpdate();
        }
    }

    // Method to delete candidate by candidate_id
    public void deleteCandidate(int candidateId) throws SQLException {
        String sql = "DELETE FROM candidates WHERE candidate_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, candidateId);
            statement.executeUpdate();
        }
    }
    
    public Candidate getCandidateById(int candidateId) throws SQLException {
        String sql = "SELECT * FROM candidates WHERE candidate_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, candidateId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int electionId = rs.getInt("election_id");
                int partyId = rs.getInt("party_id");
                int regionId = rs.getInt("region_id");
                int memberId = rs.getInt("member_id");
                int votesCount = rs.getInt("votes_count");
                String candidateName = rs.getString("candidate_name");
                String partyName = rs.getString("party_name");
                return new Candidate(candidateId, electionId, partyId, regionId, memberId, votesCount, candidateName, partyName);
            }
        }
        return null;  // Return null if no candidate found
    }
    
    public Candidate getCandidateByName(String candidateName, int electionId) throws SQLException {
    String sql = "SELECT * FROM candidates WHERE candidate_name = ? AND election_id = ?";
    
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        // Set the parameters for the query
        ps.setString(1, candidateName);  // Use candidateName for the query
        ps.setInt(2, electionId);        // Use electionId for filtering candidates in the given election
        
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int candidateId = rs.getInt("candidate_id");  // Get the candidateId from the result set
            int partyId = rs.getInt("party_id");
            int regionId = rs.getInt("region_id");
            int memberId = rs.getInt("member_id");
            int votesCount = rs.getInt("votes_count");
            String partyName = rs.getString("party_name");
            
            // Return the candidate object
            return new Candidate(candidateId, electionId, partyId, regionId, memberId, votesCount, candidateName, partyName);
        }
    } catch (SQLException e) {
        // Log the exception (optional)
        throw new SQLException("Error retrieving candidate: " + e.getMessage());
    }
    
    return null;  // Return null if no candidate found
}

    
    public String getElectionNameById(int electionId) throws SQLException {
        String sql = "SELECT election_name FROM elections WHERE election_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, electionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("election_name");
            }
        }
        return null;  // Return null if no election name found
    }
    public List<Candidate> getCandidatesForElectionAndRegion(int electionId, int regionId) throws SQLException {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "SELECT * " +
                     "FROM candidates " +
                     "WHERE election_id = ? AND region_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, electionId);
            stmt.setInt(2, regionId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Candidate candidate = new Candidate(
                        rs.getInt("candidate_id"),
                        rs.getInt("election_id"),
                        rs.getInt("party_id"),
                        rs.getInt("region_id"),
                        rs.getInt("member_id"),
                        rs.getInt("votes_count"),
                        rs.getString("candidate_name"),
                        rs.getString("party_name")
                    );
                    candidates.add(candidate);
                }
            }
        }
        return candidates;
    }
    public List<Candidate> getCandidatesForElection(int electionId) throws SQLException {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "SELECT * FROM candidates WHERE election_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, electionId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Candidate candidate = new Candidate(
                        rs.getInt("candidate_id"),
                        rs.getInt("election_id"),
                        rs.getInt("party_id"),
                        rs.getInt("region_id"),
                        rs.getInt("member_id"),
                        rs.getInt("votes_count"),
                        rs.getString("candidate_name"),
                        rs.getString("party_name")
                    );
                    candidates.add(candidate);
                }
            }
        }
        return candidates;
    }

   
   

}
