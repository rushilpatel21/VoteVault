package com.votingsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
//     VotingSystem
    private static final String URL = "jdbc:mysql://localhost:3306/new_voting_system";
    private static final String USER = "root"; // Change if your root user has a different password
    private static final String PASSWORD = "root"; // Set your root password
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return connection;
    }
}
