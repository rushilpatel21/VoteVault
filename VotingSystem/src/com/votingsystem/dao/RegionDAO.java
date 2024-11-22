package com.votingsystem.dao;

import com.votingsystem.model.Region;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegionDAO {
    private Connection connection;

    public RegionDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addRegion(Region region) throws SQLException {
        String sql = "INSERT INTO regions (region_name, description) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, region.getRegionName());
            statement.setString(2, region.getDescription());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    region.setRegionId(generatedKeys.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Region> getAllRegions() throws SQLException {
        List<Region> regions = new ArrayList<>();
        String sql = "SELECT * FROM regions";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Region region = new Region(
                    resultSet.getInt("region_id"),
                    resultSet.getString("region_name"),
                    resultSet.getString("description")
                );
                regions.add(region);
            }
        }
        return regions;
    }

    public String getRegionNameById(int regionId) throws SQLException {
        String query = "SELECT region_name FROM regions WHERE region_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, regionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("region_name");
                }
            }
        }
        return null;
    }

    public int getRegionIdByName(String regionName) throws SQLException {
        String query = "SELECT region_id FROM regions WHERE region_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, regionName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("region_id");
                }
            }
        }
        return -1;
    }

    public boolean updateRegion(Region region) throws SQLException {
    String sql = "UPDATE regions SET region_name = ?, description = ? WHERE region_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, region.getRegionName());
        statement.setString(2, region.getDescription());
        statement.setInt(3, region.getRegionId());
        
        int rowsAffected = statement.executeUpdate();
        return rowsAffected > 0;  // Returns true if at least one row is updated
    } catch (SQLException e) {
        e.printStackTrace();
        return false;  // Returns false if update fails
    }
}


    public boolean deleteRegion(int regionId) throws SQLException {
        String sql = "DELETE FROM regions WHERE region_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, regionId);
            return statement.executeUpdate() > 0;
        }
    }
    
    public List<Region> searchRegionsByName(String regionName) throws SQLException {
    List<Region> regions = new ArrayList<>();
    String sql = "SELECT * FROM regions WHERE region_name LIKE ?";
    
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, "%" + regionName + "%");  // Using LIKE for partial match
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Region region = new Region(
                    resultSet.getInt("region_id"),
                    resultSet.getString("region_name"),
                    resultSet.getString("description")
                );
                regions.add(region);
            }
        }
    }
    return regions;
}

}
