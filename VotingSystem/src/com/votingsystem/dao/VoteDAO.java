package com.votingsystem.dao;

import com.votingsystem.model.Vote;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoteDAO {
    private Connection connection;

    public VoteDAO(Connection connection) {
        this.connection = connection;
    }

    public void addVote(Vote vote) throws SQLException {
        String sql = "INSERT INTO vote (voter_id, election_id, candidate_id, timestamp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, vote.getVoterId());
            statement.setInt(2, vote.getElectionId());
            statement.setInt(3, vote.getCandidateId());
            statement.setTimestamp(4, vote.getTimestamp());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vote.setVoteId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to insert vote, no ID obtained.");
                }
            }
        }
    }

    public List<Vote> getAllVotes() throws SQLException {
        List<Vote> votes = new ArrayList<>();
        String sql = "SELECT * FROM vote";

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Vote vote = new Vote(
                    resultSet.getInt("vote_id"),
                    resultSet.getInt("voter_id"),
                    resultSet.getInt("election_id"),
                    resultSet.getInt("candidate_id"),
                    resultSet.getTimestamp("timestamp")
                );
                votes.add(vote);
            }
        }
        return votes;
    }

    public void deleteVote(int voteId) throws SQLException {
        String sql = "DELETE FROM vote WHERE vote_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, voteId);
            statement.executeUpdate();
        }
    }
}
