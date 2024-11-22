package com.votingsystem.view;

import com.votingsystem.dao.ElectionDAO;
import com.votingsystem.dao.CandidateDAO;
import com.votingsystem.dao.VoterDAO;
import com.votingsystem.dao.VoterElectionStatusDAO;
import com.votingsystem.model.Candidate;
import com.votingsystem.model.Election;
import com.votingsystem.model.Voter;
import com.votingsystem.model.VoterElectionStatus;
import com.votingsystem.util.DBUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class CastVoteFrame extends JFrame {
    private Voter voter;
    private VoterDAO voterDAO;
    private VoterElectionStatusDAO voterElectionStatusDAO;
    private ElectionDAO electionDAO;
    private CandidateDAO candidateDAO;
    private Connection connection;

    public CastVoteFrame(Voter voter) {
        this.voter = voter;
        connection = DBUtil.getConnection();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit if connection fails
        }

        // Initialize the DAOs
        this.voterDAO = new VoterDAO(connection);
        this.voterElectionStatusDAO = new VoterElectionStatusDAO(connection);
        this.electionDAO = new ElectionDAO(connection);
        this.candidateDAO = new CandidateDAO(connection); // Initialize candidateDAO

        setTitle("Cast Vote");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Display voter details
        JLabel nameLabel = new JLabel("Voter: " + voter.getName());
        JLabel ageLabel = new JLabel("Age: " + voter.getAge());
        JLabel addressLabel = new JLabel("Address: " + voter.getAddress());
        panel.add(nameLabel);
        panel.add(ageLabel);
        panel.add(addressLabel);

        try {
            // Check if voter is approved
            if (!voter.getStatus().equals("Approved")) {
                JLabel messageLabel = new JLabel("You are not eligible to vote. Your status is: " + voter.getStatus());
                panel.add(messageLabel);
            } else {
                // Fetch elections
                List<Election> elections = electionDAO.getAllElections(); // Assuming this method exists

                // Create a table to display ongoing elections
                String[] columnNames = {"Election Name", "Election Date", "Status", "Vote"};
                Object[][] data = new Object[elections.size()][4];
                int index = 0;

                for (Election election : elections) {
                    VoterElectionStatus status = getElectionStatus(voter.getVoterId(), election.getElectionId());
                    String voteStatus = (status != null && status.isHasVoted()) ? "Voted" : "Not Voted";

                    // Check if election is ongoing
                    String electionStatus = election.getStatus(); // Assuming 'status' field is in the Election class
                    String voteButtonText = "Cast Vote";

                    // If the election is not ongoing, change the status
                    if (!electionStatus.equals("Ongoing")) {
                        voteButtonText = "Voting Closed";
                    }

                    data[index][0] = election.getElectionName();
                    data[index][1] = election.getStartDate();
                    data[index][2] = electionStatus; // Status column
                    data[index][3] = (status == null || !status.isHasVoted()) ? voteButtonText : "Already Voted";

                    index++;
                }

                JTable electionTable = new JTable(data, columnNames);
                electionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                JScrollPane scrollPane = new JScrollPane(electionTable);
                panel.add(scrollPane);

                // Add a listener for vote button actions
                JButton voteButton = new JButton("Cast Vote");
                // Modify the voteButton action listener to display candidate name along with party name
voteButton.addActionListener(e -> {
    int selectedRow = electionTable.getSelectedRow();
    if (selectedRow != -1) {
        String electionName = (String) electionTable.getValueAt(selectedRow, 0);
        int electionId = elections.get(selectedRow).getElectionId();
        String status = (String) electionTable.getValueAt(selectedRow, 2);

        if (status.equals("Ongoing") && !hasVotedForElection(voter.getVoterId(), electionId)) {
            // Fetch candidates for the selected election
            try {
                List<Candidate> candidates = candidateDAO.getCandidatesForElectionAndRegion(electionId, voter.getRegionId());

                // Check if there are any candidates
                if (candidates.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No candidates available for this election.");
                } else {
                    // Create an array to hold candidate names with party information
                    String[] candidateNames = new String[candidates.size()];
                    for (int i = 0; i < candidates.size(); i++) {
                        Candidate candidate = candidates.get(i);
                        candidateNames[i] = candidate.getCandidateName() + " (" + candidate.getPartyName() + ")";
                    }

                    // Display the candidate selection dialog
                    String candidateNameWithParty = (String) JOptionPane.showInputDialog(
                            this,
                            "Select candidate for election: " + electionName,
                            "Cast Your Vote",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            candidateNames,
                            candidateNames[0]
                    );

                    if (candidateNameWithParty != null) {
                        // Extract the candidate name before passing it to `castVote` method
                        String selectedCandidateName = candidateNameWithParty.split(" \\(")[0];
                        castVote(voter.getVoterId(), electionId, selectedCandidateName);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (!status.equals("Ongoing")) {
            JOptionPane.showMessageDialog(this, "You cannot vote in this election as it is not ongoing.");
        } else {
            JOptionPane.showMessageDialog(this, "You have already cast your vote for this election.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Please select an election to vote for.");
    }
});

                panel.add(voteButton);
            }
        } catch (Exception e) {
            e.printStackTrace();  // Handle any other exceptions
        }

        add(panel);
    }

    private boolean hasVotedForElection(int voterId, int electionId) {
        // Check if the voter has already voted for this election
        List<VoterElectionStatus> statusList = voterElectionStatusDAO.getVoterElectionStatus(voterId);
        for (VoterElectionStatus status : statusList) {
            if (status.getElectionId() == electionId && status.isHasVoted()) {
                return true;
            }
        }
        return false;
    }

    private VoterElectionStatus getElectionStatus(int voterId, int electionId) {
        List<VoterElectionStatus> statusList = voterElectionStatusDAO.getVoterElectionStatus(voterId);
        for (VoterElectionStatus status : statusList) {
            if (status.getElectionId() == electionId) {
                return status;
            }
        }
        return null;
    }

    private void castVote(int voterId, int electionId, String candidateName) {
        // Logic to cast the vote for the selected candidate
        try {
            // Fetch candidate by name and election ID
            Candidate candidate = candidateDAO.getCandidateByName(candidateName, electionId);

            if (candidate != null) {
                // Increment the candidate's vote count
                candidateDAO.incrementVoteCount(candidate.getCandidateId());

                // Get the current time as the vote timestamp
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());

                // Update the VoterElectionStatus table directly
                boolean updated = voterElectionStatusDAO.updateVoterElectionStatus(true, currentTime, voterId, electionId);
                
                if (updated) {
                    JOptionPane.showMessageDialog(this, "Your vote for " + candidateName + " has been successfully cast!");
                    this.dispose();  // Close the frame after voting
                } else {
                    JOptionPane.showMessageDialog(this, "There was an error while casting your vote.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Candidate not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error casting vote: " + e.getMessage());
        }
    }
}
