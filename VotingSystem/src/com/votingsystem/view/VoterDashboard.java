package com.votingsystem.view;

import com.votingsystem.model.Voter;

import javax.swing.*;
import java.awt.*;

public class VoterDashboard extends JFrame {
    private Voter voter;

    public VoterDashboard(Voter voter) {
        this.voter = voter;

        setTitle("Voter Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set a soft background color
        getContentPane().setBackground(new Color(245, 245, 245));

        // Main panel setup with padding and background color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title label with custom font and color
        JLabel titleLabel = new JLabel("Welcome to the Voter Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(54, 54, 54));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Button panel with grid layout for better alignment
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        // Create and style buttons
        JButton voteButton = createStyledButton("Cast Vote");
        voteButton.addActionListener(e -> new CastVoteFrame(voter).setVisible(true));

        JButton viewResultsButton = createStyledButton("View Results");
        viewResultsButton.addActionListener(e -> new ViewResultsFrame().setVisible(true));

        JButton logoutButton = createStyledButton("Logout");
        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        // Add buttons to button panel
        buttonPanel.add(voteButton);
        buttonPanel.add(viewResultsButton);
        buttonPanel.add(logoutButton);

        // Add button panel to main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);
    }

    // Method to create a styled button with consistent look
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(new Color(70, 130, 180)); // Steel blue color
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover effect for button
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237)); // Lighter shade on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180)); // Original color
            }
        });
        return button;
    }
}
