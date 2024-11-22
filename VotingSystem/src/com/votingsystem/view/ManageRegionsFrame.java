package com.votingsystem.view;

import com.votingsystem.dao.RegionDAO;
import com.votingsystem.model.Region;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class ManageRegionsFrame extends JFrame {
    private JTextField regionNameField, searchField;
    private JTextArea descriptionArea;
    private JTable regionTable;
    private DefaultTableModel tableModel;
    private RegionDAO regionDAO;

    public ManageRegionsFrame(Connection connection) {
        setTitle("Manage Regions");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        regionDAO = new RegionDAO(connection);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.add(new JLabel("Region Name:"));
        regionNameField = new JTextField();
        formPanel.add(regionNameField);

        formPanel.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(descriptionArea));

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Region");
        addButton.addActionListener(e -> addRegion());

        JButton updateButton = new JButton("Update Region");
        updateButton.addActionListener(e -> updateRegion());

        JButton deleteButton = new JButton("Delete Region");
        deleteButton.addActionListener(e -> deleteRegion());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchRegions());
        searchPanel.add(searchButton);

        String[] columnNames = {"ID", "Region Name", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0);
        regionTable = new JTable(tableModel);
        loadRegions();

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(searchPanel, BorderLayout.SOUTH);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(regionTable), BorderLayout.CENTER);
    }

    private void loadRegions() {
        try {
            List<Region> regions = regionDAO.getAllRegions();
            tableModel.setRowCount(0);
            for (Region region : regions) {
                tableModel.addRow(new Object[]{region.getRegionId(), region.getRegionName(), region.getDescription()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load regions", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchRegions() {
        String query = searchField.getText();
        try {
            List<Region> regions = regionDAO.searchRegionsByName(query);
            tableModel.setRowCount(0);
            for (Region region : regions) {
                tableModel.addRow(new Object[]{region.getRegionId(), region.getRegionName(), region.getDescription()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to search regions", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRegion() {
        String regionName = regionNameField.getText();
        String description = descriptionArea.getText();

        if (regionName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Region name cannot be empty", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Region newRegion = new Region(0, regionName, description);

        try {
            if (regionDAO.addRegion(newRegion)) {
                JOptionPane.showMessageDialog(this, "Region added successfully");
                loadRegions();
                regionNameField.setText("");
                descriptionArea.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add region", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to add region", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRegion() {
        int selectedRow = regionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a region to update", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int regionId = (int) tableModel.getValueAt(selectedRow, 0);
        String regionName = regionNameField.getText();
        String description = descriptionArea.getText();

        Region updatedRegion = new Region(regionId, regionName, description);

        try {
            if (regionDAO.updateRegion(updatedRegion)) {
                JOptionPane.showMessageDialog(this, "Region updated successfully");
                loadRegions();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update region", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to update region", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRegion() {
        int selectedRow = regionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a region to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int regionId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this region?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (regionDAO.deleteRegion(regionId)) {
                    JOptionPane.showMessageDialog(this, "Region deleted successfully");
                    loadRegions();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete region", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to delete region", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
