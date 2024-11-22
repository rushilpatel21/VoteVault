package com.votingsystem.dao;

import com.votingsystem.model.PartyMember;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartyMemberDAO {
    private Connection connection;

    public PartyMemberDAO(Connection connection) {
        this.connection = connection;
    }

    public void addPartyMember(PartyMember partyMember) throws SQLException {
        String sql = "INSERT INTO partymember (party_id, region_id, name, position, photo_path, contact_info, joining_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, partyMember.getPartyId());
            statement.setInt(2, partyMember.getRegionId());
            statement.setString(3, partyMember.getName());
            statement.setString(4, partyMember.getPosition());
            statement.setString(5, partyMember.getPhotoPath());
            statement.setString(6, partyMember.getContactInfo());
            statement.setDate(7, partyMember.getJoiningDate());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    partyMember.setMemberId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to insert party member, no ID obtained.");
                }
            }
        }
    }

    public List<PartyMember> getAllPartyMembers() throws SQLException {
        List<PartyMember> partyMembers = new ArrayList<>();
        String sql = "SELECT * FROM partymember";

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                PartyMember partyMember = new PartyMember(
                    resultSet.getInt("member_id"),
                    resultSet.getInt("party_id"),
                    resultSet.getInt("region_id"),
                    resultSet.getString("name"),
                    resultSet.getString("position"),
                    resultSet.getString("photo_path"),
                    resultSet.getString("contact_info"),
                    resultSet.getDate("joining_date")
                );
                partyMembers.add(partyMember);
            }
        }
        return partyMembers;
    }

    public void updatePartyMember(PartyMember partyMember) throws SQLException {
        String sql = "UPDATE partymember SET party_id = ?, region_id = ?, name = ?, position = ?, photo_path = ?, contact_info = ?, joining_date = ? WHERE member_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, partyMember.getPartyId());
            statement.setInt(2, partyMember.getRegionId());
            statement.setString(3, partyMember.getName());
            statement.setString(4, partyMember.getPosition());
            statement.setString(5, partyMember.getPhotoPath());
            statement.setString(6, partyMember.getContactInfo());
            statement.setDate(7, partyMember.getJoiningDate());
            statement.setInt(8, partyMember.getMemberId());
            statement.executeUpdate();
        }
    }

    public void deletePartyMember(int memberId) throws SQLException {
        String sql = "DELETE FROM partymember WHERE member_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberId);
            statement.executeUpdate();
        }
    }
}
