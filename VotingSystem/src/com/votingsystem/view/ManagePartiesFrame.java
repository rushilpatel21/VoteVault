package com.votingsystem.view;

import com.votingsystem.dao.PartyDAO;
import com.votingsystem.model.Party;

import java.text.SimpleDateFormat;
import java.sql.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ManagePartiesFrame extends JFrame {
    private PartyDAO partyDAO;
    private JTable partyTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, foundingDateField, logoPathField;
    private JTextArea descriptionArea;
    private JComboBox<String> statusComboBox;

    public ManagePartiesFrame(Connection connection) {
        setTitle("Manage Parties");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        partyDAO = new PartyDAO(connection);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create and add table panel (center)
        createTablePanel(mainPanel);

        // Create and add form panel (east)
        createFormPanel(mainPanel);

        // Create and add button panel (south)
        createButtonPanel(mainPanel);

        add(mainPanel);
        refreshTable();
    }

    // Table panel creation
    private void createTablePanel(JPanel mainPanel) {
        String[] columns = {"ID", "Name", "Founding Date", "Logo Path", "Description", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells uneditable
            }
        };

        partyTable = new JTable(tableModel);
        partyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        partyTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && partyTable.getSelectedRow() != -1) {
                populateFormFields(); // Populate form with selected party's details
            }
        });

        JScrollPane scrollPane = new JScrollPane(partyTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    // Form panel creation (for adding/updating party details)
    private void createFormPanel(JPanel mainPanel) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Party Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Founding Date
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Founding Date:"), gbc);
        gbc.gridx = 1;
        foundingDateField = new JTextField(20);
        formPanel.add(foundingDateField, gbc);

        // Logo Path
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Logo Path:"), gbc);
        gbc.gridx = 1;
        logoPathField = new JTextField(20);
        formPanel.add(logoPathField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusComboBox = new JComboBox<>(new String[]{"Active", "Inactive"});
        formPanel.add(statusComboBox, gbc);

        mainPanel.add(formPanel, BorderLayout.EAST);
    }

    // Button panel creation for Add, Update, and Delete actions
    private void createButtonPanel(JPanel mainPanel) {
        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add Party");
        addButton.addActionListener(e -> addParty());
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update Party");
        updateButton.addActionListener(e -> updateParty());
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Party");
        deleteButton.addActionListener(e -> deleteParty());
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Populate form fields with selected party data
    private void populateFormFields() {
        int selectedRow = partyTable.getSelectedRow();
        nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        foundingDateField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        logoPathField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        descriptionArea.setText(tableModel.getValueAt(selectedRow, 4).toString());
        statusComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 5).toString());
    }

    // Refresh table with updated data
    private void refreshTable() {
        tableModel.setRowCount(0); // Clear existing rows
        try {
            List<Party> parties = partyDAO.getAllParties();
            for (Party party : parties) {
                tableModel.addRow(new Object[]{
                        party.getPartyId(),
                        party.getPartyName(),
                        party.getFoundingDate(),
                        party.getLogoPath(),
                        party.getDescription(),
                        party.getStatus()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching parties", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Add a new party to the database
    private void addParty() {
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // Assuming the date format is "yyyy-MM-dd"
        java.sql.Date foundingDate = new java.sql.Date(sdf.parse(foundingDateField.getText()).getTime());
        
        Party party = new Party(0, nameField.getText(), foundingDate, logoPathField.getText(), descriptionArea.getText(), (String) statusComboBox.getSelectedItem());
        partyDAO.addParty(party);
        refreshTable();
        clearFormFields();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error adding party", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    // Update an existing party
    private void updateParty() {
    int selectedRow = partyTable.getSelectedRow();
    if (selectedRow != -1) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date foundingDate = new java.sql.Date(sdf.parse(foundingDateField.getText()).getTime());
            
            int partyId = (int) tableModel.getValueAt(selectedRow, 0);
            Party party = new Party(partyId, nameField.getText(), foundingDate, logoPathField.getText(), descriptionArea.getText(), (String) statusComboBox.getSelectedItem());
            partyDAO.updateParty(party);
            refreshTable();
            clearFormFields();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating party", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Select a party to update", "Warning", JOptionPane.WARNING_MESSAGE);
    }
}

    // Delete the selected party
    private void deleteParty() {
        int selectedRow = partyTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                int partyId = (int) tableModel.getValueAt(selectedRow, 0);
                partyDAO.deleteParty(partyId);
                refreshTable();
                clearFormFields();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting party", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a party to delete", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Clear form fields after action
    private void clearFormFields() {
        nameField.setText("");
        foundingDateField.setText("");
        logoPathField.setText("");
        descriptionArea.setText("");
        statusComboBox.setSelectedIndex(0);
        partyTable.clearSelection();
    }
}
