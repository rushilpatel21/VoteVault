package com.votingsystem.dao;

import com.votingsystem.model.Party;
import com.votingsystem.model.PartyMember;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartyDAO {
    private Connection connection;

    public PartyDAO(Connection connection) {
        this.connection = connection;
    }

    public void addParty(Party party) throws SQLException {
        String sql = "INSERT INTO party (party_name, founding_date, logo_path, description, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, party.getPartyName());
            statement.setDate(2, party.getFoundingDate());
            statement.setString(3, party.getLogoPath());
            statement.setString(4, party.getDescription());
            statement.setString(5, party.getStatus());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    party.setPartyId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to insert party, no ID obtained.");
                }
            }
        }
    }

    public List<Party> getAllParties() throws SQLException {
        List<Party> parties = new ArrayList<>();
        String sql = "SELECT * FROM party";

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Party party = new Party(
                    resultSet.getInt("party_id"),
                    resultSet.getString("party_name"),
                    resultSet.getDate("founding_date"),
                    resultSet.getString("logo_path"),
                    resultSet.getString("description"),
                    resultSet.getString("status")
                );
                parties.add(party);
            }
        }
        return parties;
    }
    public String getPartyNameById(int partyId) throws SQLException {
        String query = "SELECT party_name FROM party WHERE party_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, partyId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("party_name");
                }
            }
        }
        return null;  // Return null if not found
    }

    public int getPartyIdByName(String partyName) throws SQLException {
        String query = "SELECT party_id FROM party WHERE party_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, partyName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("party_id");
                }
            }
        }
        return -1;  // Return -1 if not found
    }

//    public List<PartyMember> getMembersByPartyAndRegion(int partyId, int regionId) throws SQLException {
//        List<PartyMember> members = new ArrayList<>();
//        String query = "SELECT * FROM party_members WHERE party_id = ? AND region_id = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setInt(1, partyId);
//            stmt.setInt(2, regionId);
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    PartyMember member = new PartyMember(
//                            rs.getInt("party_id"),
//                            rs.getString("member_name"),
//                            rs.getInt("party_id"),
//                            rs.getInt("region_id")
//                    );
//                    members.add(member);
//                }
//            }
//        }
//        return members;
//    }
    public void updateParty(Party party) throws SQLException {
        String sql = "UPDATE party SET party_name = ?, founding_date = ?, logo_path = ?, description = ?, status = ? WHERE party_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, party.getPartyName());
            statement.setDate(2, party.getFoundingDate());
            statement.setString(3, party.getLogoPath());
            statement.setString(4, party.getDescription());
            statement.setString(5, party.getStatus());
            statement.setInt(6, party.getPartyId());
            statement.executeUpdate();
        }
    }

    public void deleteParty(int partyId) throws SQLException {
        String sql = "DELETE FROM party WHERE party_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, partyId);
            statement.executeUpdate();
        }
    }
}
