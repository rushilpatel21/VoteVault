package com.votingsystem.main;

import javax.swing.*;
import com.votingsystem.view.LoginFrame;
import java.awt.*;
public class VotingSystemMain {
    public static void main(String[] args) {
        // Set Look and Feel to Nimbus or System default for better aesthetics
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Show splash screen while the app is initializing
        showSplashScreen();

        // Initialize the Login Frame after splash screen is closed
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    private static void showSplashScreen() {
        JWindow splashWindow = new JWindow();
        splashWindow.setLayout(new BorderLayout());

        // Create a label with an image for the splash screen
        JLabel splashLabel = new JLabel("Voting System", JLabel.CENTER);
        splashLabel.setFont(new Font("Arial", Font.BOLD, 24));
        splashLabel.setForeground(Color.WHITE);
        splashWindow.getContentPane().add(splashLabel, BorderLayout.CENTER);

        // Set the background color
        splashWindow.getContentPane().setBackground(new Color(0, 123, 255)); // Blue background

        // Set size and position for splash screen
        splashWindow.setSize(400, 200);
        splashWindow.setLocationRelativeTo(null); // Center on the screen
        splashWindow.setVisible(true);

        // Make splash screen visible for 3 seconds
        try {
            Thread.sleep(3000); // Display for 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        splashWindow.setVisible(false); // Hide splash screen
    }
}
