package com.votingsystem.view;

import com.votingsystem.dao.ElectionDAO;
import com.votingsystem.model.Election;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class ManageElectionsFrame extends JFrame {
    private JTextField electionNameField, startDateField, endDateField, statusField;
    private JTable electionTable;
    private DefaultTableModel tableModel;
    private ElectionDAO electionDAO;

    public ManageElectionsFrame(Connection connection) {
        setTitle("Manage Elections");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        electionDAO = new ElectionDAO(connection);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.add(new JLabel("Election Name:"));
        electionNameField = new JTextField();
        formPanel.add(electionNameField);

        formPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        startDateField = new JTextField();
        formPanel.add(startDateField);

        formPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        endDateField = new JTextField();
        formPanel.add(endDateField);

        formPanel.add(new JLabel("Status:"));
        statusField = new JTextField();
        formPanel.add(statusField);

        JButton addButton = new JButton("Add Election");
        addButton.addActionListener(e -> addElection());
        JButton updateButton = new JButton("Update Election");
        updateButton.addActionListener(e -> updateElection());
        JButton deleteButton = new JButton("Delete Election");
        deleteButton.addActionListener(e -> deleteElection());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Start Date", "End Date", "Status"}, 0);
        electionTable = new JTable(tableModel);
        loadElections();

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(electionTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadElections() {
        tableModel.setRowCount(0);
        try {
            List<Election> elections = electionDAO.getAllElections();
            for (Election election : elections) {
                tableModel.addRow(new Object[]{
                        election.getElectionId(), election.getElectionName(),
                        election.getStartDate(), election.getEndDate(), election.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load elections: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addElection() {
        try {
            Election newElection = new Election(
                    0, electionNameField.getText(), 
                    Date.valueOf(startDateField.getText()), 
                    Date.valueOf(endDateField.getText()), 
                    statusField.getText()
            );
            electionDAO.addElection(newElection);
            loadElections();
            clearFields();
            JOptionPane.showMessageDialog(this, "Election added successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to add election: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateElection() {
        int selectedRow = electionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an election to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Election election = new Election(
                    (int) tableModel.getValueAt(selectedRow, 0),
                    electionNameField.getText(),
                    Date.valueOf(startDateField.getText()),
                    Date.valueOf(endDateField.getText()),
                    statusField.getText()
            );
            electionDAO.updateElection(election);
            loadElections();
            clearFields();
            JOptionPane.showMessageDialog(this, "Election updated successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to update election: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteElection() {
        int selectedRow = electionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an election to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int electionId = (int) tableModel.getValueAt(selectedRow, 0);
            electionDAO.deleteElection(electionId);
            loadElections();
            JOptionPane.showMessageDialog(this, "Election deleted successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to delete election: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        electionNameField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        statusField.setText("");
    }
}
