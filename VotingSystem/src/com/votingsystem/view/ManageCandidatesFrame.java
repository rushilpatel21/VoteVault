package com.votingsystem.view;

import com.votingsystem.dao.CandidateDAO;
import com.votingsystem.dao.RegionDAO;
import com.votingsystem.dao.ElectionDAO;
import com.votingsystem.dao.PartyDAO;
import com.votingsystem.model.Candidate;
import com.votingsystem.model.Region;
import com.votingsystem.model.Election;
import com.votingsystem.model.Party;
import com.votingsystem.model.PartyMember;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ManageCandidatesFrame extends JFrame {
    private JTextField candidateNameField;
    private JComboBox<String> electionComboBox;
    private JComboBox<String> partyComboBox;
    private JComboBox<String> regionComboBox;
    private CandidateDAO candidateDAO;
    private RegionDAO regionDAO;
    private ElectionDAO electionDAO;
    private PartyDAO partyDAO;

    private JTable candidatesTable;
    private DefaultTableModel tableModel;

    public ManageCandidatesFrame(Connection connection) {
        setTitle("Manage Candidates");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize DAOs
        candidateDAO = new CandidateDAO(connection);
        regionDAO = new RegionDAO(connection);
        electionDAO = new ElectionDAO(connection);
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

        // Populate the combo boxes with data from the database
        populateComboBoxes();

        refreshTable();
    }

    // Table panel creation
    private void createTablePanel(JPanel mainPanel) {
        String[] columns = {"ID", "Candidate Name", "Election", "Party", "Region", "Votes Count"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells uneditable
            }
        };

        candidatesTable = new JTable(tableModel);
        candidatesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        candidatesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && candidatesTable.getSelectedRow() != -1) {
                populateFormFields(); // Populate form with selected candidate's details
            }
        });

        JScrollPane scrollPane = new JScrollPane(candidatesTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }
    private void populateFormFields() {
    int selectedRow = candidatesTable.getSelectedRow();
    if (selectedRow != -1) {
        // Get selected candidate ID from the table
        int candidateId = (int) tableModel.getValueAt(selectedRow, 0);
        
        try {
            // Fetch the candidate from the DAO
            Candidate selectedCandidate = candidateDAO.getCandidateById(candidateId);

            // Populate the form fields
            candidateNameField.setText(selectedCandidate.getCandidateName());

            // Set the combo boxes to the selected values
            String electionName = electionDAO.getElectionNameById(selectedCandidate.getElectionId());
            electionComboBox.setSelectedItem(electionName);

            String partyName = partyDAO.getPartyNameById(selectedCandidate.getPartyId());
            partyComboBox.setSelectedItem(partyName);

            String regionName = regionDAO.getRegionNameById(selectedCandidate.getRegionId());
            regionComboBox.setSelectedItem(regionName);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error populating form fields", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    // Form panel creation (for adding/updating candidate details)
    private void createFormPanel(JPanel mainPanel) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Candidate Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Candidate Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Candidate Name:"), gbc);
        gbc.gridx = 1;
        candidateNameField = new JTextField(20);
        formPanel.add(candidateNameField, gbc);

        // Election ComboBox
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Election:"), gbc);
        gbc.gridx = 1;
        electionComboBox = new JComboBox<>();
        formPanel.add(electionComboBox, gbc);

        // Party ComboBox
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Party:"), gbc);
        gbc.gridx = 1;
        partyComboBox = new JComboBox<>();
        formPanel.add(partyComboBox, gbc);

        // Region ComboBox
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Region:"), gbc);
        gbc.gridx = 1;
        regionComboBox = new JComboBox<>();
        formPanel.add(regionComboBox, gbc);

        mainPanel.add(formPanel, BorderLayout.EAST);
    }

    // Button panel creation for Add, Update, and Delete actions
    private void createButtonPanel(JPanel mainPanel) {
        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add Candidate");
        addButton.addActionListener(e -> addCandidate());
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update Candidate");
        updateButton.addActionListener(e -> updateCandidate());
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Candidate");
        deleteButton.addActionListener(e -> deleteCandidate());
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Populate the combo boxes with data from the database
    private void populateComboBoxes() {
        try {
            // Populate election combo box
            List<Election> elections = electionDAO.getAllElections();
            for (Election election : elections) {
                electionComboBox.addItem(election.getElectionName());
            }

            // Populate party combo box
            List<Party> parties = partyDAO.getAllParties();
            for (Party party : parties) {
                partyComboBox.addItem(party.getPartyName());
            }

            // Populate region combo box
            List<Region> regions = regionDAO.getAllRegions();
            for (Region region : regions) {
                regionComboBox.addItem(region.getRegionName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Refresh table with updated data
    private void refreshTable() {
        tableModel.setRowCount(0); // Clear existing rows
        try {
            List<Candidate> candidates = candidateDAO.getAllCandidates();
            for (Candidate candidate : candidates) {
                String electionName = electionDAO.getElectionNameById(candidate.getElectionId());
                String partyName = partyDAO.getPartyNameById(candidate.getPartyId());
                String regionName = regionDAO.getRegionNameById(candidate.getRegionId());

                tableModel.addRow(new Object[] {
                    candidate.getCandidateId(),
                    candidate.getCandidateName(),
                    electionName,
                    partyName,
                    regionName,
                    candidate.getVotesCount()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching candidates", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Add a new candidate to the database
    private void addCandidate() {
        try {
            String candidateName = candidateNameField.getText();
            if (candidateName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Candidate name cannot be empty", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int electionId = electionDAO.getElectionIdByName((String) electionComboBox.getSelectedItem());
            int partyId = partyDAO.getPartyIdByName((String) partyComboBox.getSelectedItem());
            int regionId = regionDAO.getRegionIdByName((String) regionComboBox.getSelectedItem());

            // Get the party name and member ID
            String partyName = (String) partyComboBox.getSelectedItem();
            int memberId = getSelectedMemberId(partyName);

            Candidate candidate = new Candidate(0, electionId, partyId, regionId, memberId, 0, candidateName, partyName);
            candidateDAO.addCandidate(candidate);
            refreshTable();
            candidateNameField.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding candidate", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update an existing candidate
    private void updateCandidate() {
        int selectedRow = candidatesTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                int candidateId = (int) tableModel.getValueAt(selectedRow, 0);
                Candidate candidate = candidateDAO.getCandidateById(candidateId);
                candidate.setCandidateName(candidateNameField.getText());

                int electionId = electionDAO.getElectionIdByName((String) electionComboBox.getSelectedItem());
                int partyId = partyDAO.getPartyIdByName((String) partyComboBox.getSelectedItem());
                int regionId = regionDAO.getRegionIdByName((String) regionComboBox.getSelectedItem());

                String partyName = (String) partyComboBox.getSelectedItem();
                int memberId = getSelectedMemberId(partyName);

                candidate.setElectionId(electionId);
                candidate.setPartyId(partyId);
                candidate.setRegionId(regionId);
                candidate.setPartyName(partyName);
                candidate.setMemberId(memberId);

                candidateDAO.updateCandidate(candidate);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Candidate updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating candidate", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No candidate selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete a selected candidate
    private void deleteCandidate() {
        int selectedRow = candidatesTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                int candidateId = (int) tableModel.getValueAt(selectedRow, 0);
                candidateDAO.deleteCandidate(candidateId);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Candidate deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting candidate", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No candidate selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to get the selected member ID from the party
    private int getSelectedMemberId(String partyName) {
        // This can be implemented as needed depending on the application logic
        return 0;  // Placeholder for now
    }
}
