package com.votingsystem.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.votingsystem.model.Voter;
import com.votingsystem.dao.VoterDAO;
import com.votingsystem.util.DBUtil;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeComboBox;
    private Connection connection;

    public LoginFrame() {
        connection = DBUtil.getConnection();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit if connection fails
        }
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("User Type:"));
        userTypeComboBox = new JComboBox<>(new String[]{"Admin", "Voter"});
        panel.add(userTypeComboBox);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        panel.add(loginButton);
        add(panel);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeComboBox.getSelectedItem();

        if ("Admin".equals(userType) && "admin".equals(username) && "admin".equals(password)) {
            new AdminDashboard().setVisible(true);
            dispose();
        } else if ("Voter".equals(userType)) {
            Voter voter = validateVoter(username, password);
            if (voter != null) {
                new VoterDashboard(voter).setVisible(true); // Pass voter to the dashboard
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Voter validateVoter(String username, String password) {
        String sql = "SELECT * FROM voter WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Create a Voter object and populate it with data from the resultSet
                    Voter voter = new Voter();
                    voter.setVoterId(resultSet.getInt("voter_id"));
                    voter.setName(resultSet.getString("name"));
                    voter.setAge(resultSet.getInt("age"));
                    voter.setAddress(resultSet.getString("address"));
                    voter.setRegionId(resultSet.getInt("region_id"));
                    voter.setStatus(resultSet.getString("status"));
                    voter.setUsername(resultSet.getString("username"));
                    voter.setPassword(resultSet.getString("password"));
                    return voter;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null; // Return null if authentication fails
    }
}
