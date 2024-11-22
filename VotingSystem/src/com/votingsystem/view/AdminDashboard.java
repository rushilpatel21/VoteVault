package com.votingsystem.view;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.votingsystem.dao.VoterDAO;
import com.votingsystem.util.DBUtil;

public class AdminDashboard extends JFrame {
    private Connection connection;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(500, 600); // Adjusted size to fit all elements comfortably
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize the database connection
        connection = DBUtil.getConnection();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit if connection fails
        }

        // Initialize the VoterDAO instance
        VoterDAO voterDAO = new VoterDAO(connection);

        // Panel to hold all buttons
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245)); // Light gray background for better contrast

        // Create and style buttons
        JButton managePartiesButton = createStyledButton("Manage Parties");
        managePartiesButton.addActionListener(e -> new ManagePartiesFrame(connection).setVisible(true));

        JButton manageRegionsButton = createStyledButton("Manage Regions");
        manageRegionsButton.addActionListener(e -> new ManageRegionsFrame(connection).setVisible(true));

        JButton manageCandidatesButton = createStyledButton("Manage Candidates");
        manageCandidatesButton.addActionListener(e -> new ManageCandidatesFrame(connection).setVisible(true));

        JButton manageElectionsButton = createStyledButton("Manage Elections");
        manageElectionsButton.addActionListener(e -> new ManageElectionsFrame(connection).setVisible(true));

        JButton addNewVoterButton = createStyledButton("Add New Voter");
        addNewVoterButton.addActionListener(e -> new AdminNewVoterFrame(voterDAO).setVisible(true));

        JButton logoutButton = createStyledButton("Logout");
        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true); // Opens the Login frame
            dispose(); // Close the AdminDashboard
        });

        // Adding buttons to the panel with some space between them
        panel.add(managePartiesButton);
        panel.add(Box.createVerticalStrut(10)); // Adds space between buttons
        panel.add(manageRegionsButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(manageCandidatesButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(manageElectionsButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(addNewVoterButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(logoutButton);

        // Create and set up the menu bar
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        logoutMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q")); // Adds accelerator for Ctrl+Q
        logoutMenuItem.setMnemonic('L'); // Sets mnemonic key for 'L' to activate this item
        logoutMenuItem.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        fileMenu.add(logoutMenuItem);

        JMenu manageMenu = new JMenu("Manage");
        JMenuItem managePartiesMenuItem = new JMenuItem("Manage Parties");
        managePartiesMenuItem.setMnemonic('P'); // Sets mnemonic key for 'P' to activate this item
        managePartiesMenuItem.addActionListener(e -> new ManagePartiesFrame(connection).setVisible(true));
        manageMenu.add(managePartiesMenuItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.setMnemonic('A'); // Sets mnemonic key for 'A' to activate this item
        aboutMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Admin Dashboard for Voting System\nDeveloped by Rushil and Saloni"));
        helpMenu.add(aboutMenuItem);

        // Adding menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(manageMenu);
        menuBar.add(helpMenu);

        // Set the menu bar to the frame
        setJMenuBar(menuBar);

        // Add the main panel with buttons to the frame
        add(panel);
    }

    // Helper method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20))); // Rounded corners effect
        button.setMaximumSize(new Dimension(400, 50));  // Make the buttons wider
        return button;
    }
}
