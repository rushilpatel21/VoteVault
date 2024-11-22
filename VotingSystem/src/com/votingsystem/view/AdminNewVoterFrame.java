package com.votingsystem.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import com.votingsystem.dao.VoterDAO;
import com.votingsystem.model.Voter;

public class AdminNewVoterFrame extends JFrame {
    private JTextField nameField, ageField, addressField, usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> regionComboBox, statusComboBox;
    private JButton addButton, editButton, deleteButton;
    private JTable voterTable;
    private DefaultTableModel tableModel;
    private VoterDAO voterDAO;

    public AdminNewVoterFrame(VoterDAO voterDAO) {
        this.voterDAO = voterDAO;
        setTitle("Manage Voters");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize voter table and load existing voters
        initializeVoterTable();
        loadVoters();

        // Create panel for form input fields
        JPanel formPanel = createFormPanel();

        // Add components to frame
        add(new JScrollPane(voterTable), BorderLayout.CENTER);
        add(formPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Initialize JTable to display voters
    private void initializeVoterTable() {
        String[] columnNames = {"ID", "Name", "Age", "Address", "Region", "Status", "Username"};
        tableModel = new DefaultTableModel(columnNames, 0);
        voterTable = new JTable(tableModel);
        voterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Load voters when a row is selected
        voterTable.getSelectionModel().addListSelectionListener(e -> loadVoterDetails());
    }

    // Load voters from DAO to table
    private void loadVoters() {
        try{
        tableModel.setRowCount(0); // Clear existing rows
        List<Voter> voters = voterDAO.getAllVoters();
        for (Voter voter : voters) {
            tableModel.addRow(new Object[]{
                voter.getVoterId(), voter.getName(), voter.getAge(), voter.getAddress(),
                voter.getRegionId(), voter.getStatus(), voter.getUsername()
            });
        }}catch(Exception e){
            
        }
    }

    // Create panel for form input fields and buttons
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(5, 4, 10, 10));

        // Initialize form fields
        nameField = new JTextField();
        ageField = new JTextField();
        addressField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        regionComboBox = new JComboBox<>(new String[]{"North", "South", "East", "West", "Central"});
        statusComboBox = new JComboBox<>(new String[]{"Approved", "Pending", "Rejected"});
        
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        // Add components to form panel
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Region:"));
        formPanel.add(regionComboBox);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusComboBox);
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(addButton);
        formPanel.add(editButton);
        formPanel.add(deleteButton);

        // Add action listeners for buttons
        addButton.addActionListener(e -> addVoter());
        editButton.addActionListener(e -> editVoter());
        deleteButton.addActionListener(e -> deleteVoter());

        return formPanel;
    }

    // Load selected voter details into form fields
    private void loadVoterDetails() {
        int selectedRow = voterTable.getSelectedRow();
        if (selectedRow != -1) {
            nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            ageField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
            addressField.setText((String) tableModel.getValueAt(selectedRow, 3));
            regionComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 4));
            statusComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 5));
            usernameField.setText((String) tableModel.getValueAt(selectedRow, 6));
            passwordField.setText(""); // Password should not be pre-filled for security
        }
    }

    // Add a new voter
    private void addVoter() {
        if (validateFields()) {
            Voter newVoter = createVoterFromFields();
            if (voterDAO.addVoter(newVoter)) {
                JOptionPane.showMessageDialog(this, "Voter added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadVoters();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add voter.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Edit selected voter
    private void editVoter() {
        try{
        int selectedRow = voterTable.getSelectedRow();
        if (selectedRow != -1 && validateFields()) {
            Voter updatedVoter = createVoterFromFields();
            updatedVoter.setVoterId((Integer) tableModel.getValueAt(selectedRow, 0)); // Set the ID to update

            if (voterDAO.updateVoter(updatedVoter)) {
                JOptionPane.showMessageDialog(this, "Voter updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadVoters();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update voter.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }}catch(Exception e){
            
        }
    }

    // Delete selected voter
    private void deleteVoter() {
        try{
        int selectedRow = voterTable.getSelectedRow();
        if (selectedRow != -1) {
            int voterId = (Integer) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this voter?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION && voterDAO.deleteVoter(voterId)) {
                JOptionPane.showMessageDialog(this, "Voter deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadVoters();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete voter.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }}catch(Exception e){
            
        }
    }

    // Validate input fields
    private boolean validateFields() {
        if (nameField.getText().isEmpty() || ageField.getText().isEmpty() || addressField.getText().isEmpty()
                || usernameField.getText().isEmpty() || passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int age = Integer.parseInt(ageField.getText());
            if (age < 18) {
                JOptionPane.showMessageDialog(this, "Age must be 18 or older.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid age. Please enter a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Create a voter from form fields
    private Voter createVoterFromFields() {
        Voter voter = new Voter();
        voter.setName(nameField.getText());
        voter.setAge(Integer.parseInt(ageField.getText()));
        voter.setAddress(addressField.getText());
        voter.setRegionId(regionComboBox.getSelectedIndex() + 1);
        voter.setStatus((String) statusComboBox.getSelectedItem());
        voter.setUsername(usernameField.getText());
        voter.setPassword(new String(passwordField.getPassword()));
        return voter;
    }

    // Clear form fields
    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        addressField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        regionComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
    }
}
