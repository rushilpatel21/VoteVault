//package com.votingsystem.view;
//
//import com.votingsystem.dao.CandidateDAO;
//import com.votingsystem.dao.ElectionDAO;
//import com.votingsystem.model.Candidate;
//import com.votingsystem.model.Election;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.data.category.DefaultCategoryDataset;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.List;
//
//public class ViewResultsFrame extends JFrame {
//    private ElectionDAO electionDAO;
//    private CandidateDAO candidateDAO;
//    private Connection connection;
//
//    // Constructor to set up the frame and establish database connection
//    public ViewResultsFrame() {
//        try {
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/VotingSystem", "root", "root");
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Failed to connect to the database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
//            System.exit(1); // Exit if the connection fails
//        }
//
//        setTitle("View Election Results");
//        setSize(800, 600);
//        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        electionDAO = new ElectionDAO(connection);
//        candidateDAO = new CandidateDAO(connection);
//
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//
//        try {
//            List<Election> elections = electionDAO.getAllElections();
//            for (Election election : elections) {
//                JPanel electionPanel = createElectionPanel(election);
//                mainPanel.add(electionPanel);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        JScrollPane scrollPane = new JScrollPane(mainPanel);
//        add(scrollPane);
//    }
//
////    // Method to create panel for each election, including vote table and chart
////    private JPanel createElectionPanel(Election election) {
////        JPanel panel = new JPanel();
////        panel.setLayout(new BorderLayout());
////        panel.setBorder(BorderFactory.createTitledBorder("Election: " + election.getElectionName()));
////
////        JLabel electionInfo = new JLabel("Date: " + election.getStartDate());
////        panel.add(electionInfo, BorderLayout.NORTH);
////
////        try {
////            DefaultTableModel tableModel = new DefaultTableModel();
////            tableModel.addColumn("Region");
////            tableModel.addColumn("Candidate");
////            tableModel.addColumn("Votes");
////
////            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
////            String leadingCandidate = "";
////            int maxVotes = 0;
////
////            // Retrieve regions and candidates for the election, populate table and chart
////            List<Integer> regionIds = electionDAO.getRegionIdsForElection(election.getElectionId());
////            for (int regionId : regionIds) {
////                List<Candidate> candidates = candidateDAO.getCandidatesForElectionAndRegion(election.getElectionId(), regionId);
////                
////                for (Candidate candidate : candidates) {
////                    int votes = candidate.getVotesCount();
////                    dataset.addValue(votes, candidate.getCandidateName(), "Region " + regionId);
////
////                    // Add data to table model
////                    tableModel.addRow(new Object[]{"Region " + regionId, candidate.getCandidateName(), votes});
////
////                    // Update leading candidate if current candidate has higher votes
////                    if (votes > maxVotes) {
////                        maxVotes = votes;
////                        leadingCandidate = candidate.getCandidateName();
////                    }
////                }
////            }
////
////            // Table to display vote counts for each candidate per region
////            JTable table = new JTable(tableModel);
////            JScrollPane tableScrollPane = new JScrollPane(table);
////            panel.add(tableScrollPane, BorderLayout.NORTH);
////
////            // Create bar chart for candidate votes by region
////            JFreeChart barChart = ChartFactory.createBarChart(
////                    "Votes by Candidate per Region",
////                    "Candidate",
////                    "Votes",
////                    dataset,
////                    PlotOrientation.VERTICAL,
////                    true, true, false);
////
////            ChartPanel chartPanel = new ChartPanel(barChart);
////            panel.add(chartPanel, BorderLayout.CENTER);
////
////            // Display leading candidate information
////            JLabel leadingLabel = new JLabel("Leading Candidate: " + leadingCandidate + " with " + maxVotes + " votes");
////            panel.add(leadingLabel, BorderLayout.SOUTH);
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        
////        return panel;
////    }
//    // Method to create panel for each election, including vote table and chart
//    private JPanel createElectionPanel(Election election) {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout());
//        panel.setBorder(BorderFactory.createTitledBorder("Election: " + election.getElectionName()));
//
//        JLabel electionInfo = new JLabel("Date: " + election.getStartDate());
//        panel.add(electionInfo, BorderLayout.NORTH);
//
//        try {
//            DefaultTableModel tableModel = new DefaultTableModel();
//            tableModel.addColumn("Region");
//            tableModel.addColumn("Candidate");
//            tableModel.addColumn("Votes");
//
//            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//
//            // Scroll panel for adding the results for each region
//            JPanel regionPanel = new JPanel();
//            regionPanel.setLayout(new BoxLayout(regionPanel, BoxLayout.Y_AXIS));
//
//            // Retrieve regions and candidates for the election, populate table and chart
//            List<Integer> regionIds = electionDAO.getRegionIdsForElection(election.getElectionId());
//            for (int regionId : regionIds) {
//                List<Candidate> candidates = candidateDAO.getCandidatesForElectionAndRegion(election.getElectionId(), regionId);
//
//                String leadingCandidate = "";
//                int maxVotes = 0;
//
//                // Track leading candidates per region
//                StringBuilder regionLeadingCandidates = new StringBuilder();
//
//                for (Candidate candidate : candidates) {
//                    int votes = candidate.getVotesCount();
//                    dataset.addValue(votes, candidate.getCandidateName(), "Region " + regionId);
//
//                    // Add data to table model
//                    tableModel.addRow(new Object[]{"Region " + regionId, candidate.getCandidateName(), votes});
//
//                    // Update leading candidate for the current region
//                    if (votes > maxVotes) {
//                        maxVotes = votes;
//                        leadingCandidate = candidate.getCandidateName();
//                    }
//                }
//
//                // Add the leading candidate information for this region
//                if (!leadingCandidate.isEmpty()) {
//                    regionLeadingCandidates.append("Leading Candidate in Region " + regionId + ": " + leadingCandidate + " (" + maxVotes + " votes)");
//                }
//
//                // Create a label for displaying the leading candidate for the current region
//                JLabel regionLeadingLabel = new JLabel(regionLeadingCandidates.toString());
//                regionPanel.add(regionLeadingLabel);  // Add it to the regionPanel
//            }
//
//            // Add the results for each region
//            JScrollPane tableScrollPane = new JScrollPane(regionPanel);
//            panel.add(tableScrollPane, BorderLayout.NORTH);
//
//            // Table to display vote counts for each candidate per region
//            JTable table = new JTable(tableModel);
//            JScrollPane tableScrollPane2 = new JScrollPane(table);
//            panel.add(tableScrollPane2, BorderLayout.CENTER);
//
//            // Create bar chart for candidate votes by region
//            JFreeChart barChart = ChartFactory.createBarChart(
//                    "Votes by Candidate per Region",
//                    "Candidate",
//                    "Votes",
//                    dataset,
//                    PlotOrientation.VERTICAL,
//                    true, true, false);
//
//            ChartPanel chartPanel = new ChartPanel(barChart);
//            panel.add(chartPanel, BorderLayout.SOUTH);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return panel;
//    }
//
//
//}
package com.votingsystem.view;

import com.votingsystem.dao.CandidateDAO;
import com.votingsystem.dao.ElectionDAO;
import com.votingsystem.model.Candidate;
import com.votingsystem.model.Election;
import com.votingsystem.util.DBUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ViewResultsFrame extends JFrame {
    private ElectionDAO electionDAO;
    private CandidateDAO candidateDAO;
    private Connection connection;

    // Constructor to set up the frame and establish database connection
    public ViewResultsFrame() {
        connection = DBUtil.getConnection();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit if connection fails
        }

        setTitle("View Election Results");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        electionDAO = new ElectionDAO(connection);
        candidateDAO = new CandidateDAO(connection);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        try {
            List<Election> elections = electionDAO.getAllElections();
            for (Election election : elections) {
                JPanel electionPanel = createElectionPanel(election);
                mainPanel.add(electionPanel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane);
    }

    // Method to create panel for each election, including vote table and chart
    private JPanel createElectionPanel(Election election) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Election: " + election.getElectionName()));

        JLabel electionInfo = new JLabel("Date: " + election.getStartDate());
        panel.add(electionInfo, BorderLayout.NORTH);

        try {
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Region");
            tableModel.addColumn("Candidate (Party)");
            tableModel.addColumn("Votes");

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            // Retrieve regions and candidates for the election, populate table and chart
            List<Integer> regionIds = electionDAO.getRegionIdsForElection(election.getElectionId());
            for (int regionId : regionIds) {
                List<Candidate> candidates = candidateDAO.getCandidatesForElectionAndRegion(election.getElectionId(), regionId);

                String leadingCandidate = "";
                int maxVotes = 0;

                for (Candidate candidate : candidates) {
                    int votes = candidate.getVotesCount();
                    String candidateWithParty = candidate.getCandidateName() + " (" + candidate.getPartyName() + ")";
                    dataset.addValue(votes, candidateWithParty, "Region " + regionId);

                    // Add data to table model
                    tableModel.addRow(new Object[]{"Region " + regionId, candidateWithParty, votes});

                    // Update leading candidate if current candidate has higher votes
                    if (votes > maxVotes) {
                        maxVotes = votes;
                        leadingCandidate = candidateWithParty;
                    }
                }

                // Create a label for displaying the leading candidate for the current region
                if (!leadingCandidate.isEmpty()) {
                    JLabel regionLeadingLabel = new JLabel("Leading Candidate in Region " + regionId + ": " + leadingCandidate + " with " + maxVotes + " votes");
                    panel.add(regionLeadingLabel, BorderLayout.SOUTH);
                }
            }

            // Table to display vote counts for each candidate per region
            JTable table = new JTable(tableModel);
            JScrollPane tableScrollPane = new JScrollPane(table);
            panel.add(tableScrollPane, BorderLayout.CENTER);

            // Create bar chart for candidate votes by region
            JFreeChart barChart = ChartFactory.createBarChart(
                    "Votes by Candidate per Region",
                    "Candidate",
                    "Votes",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, true, false);

            ChartPanel chartPanel = new ChartPanel(barChart);
            panel.add(chartPanel, BorderLayout.SOUTH);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return panel;
    }
}
