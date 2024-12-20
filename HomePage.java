import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HomePage extends JFrame {

    private Client client;
    private JButton profileButton, joinGroupButton, logoutButton;
    private JTextField joinCodeField;
    private JList<String> groupList;
    private DefaultListModel<String> groupListModel;
    
    private ArrayList<String> groups; // To store the group names


    public HomePage(Client client) {
        this.client = client;

        setTitle("Home Page");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Terminates the program on window close
        setLocationRelativeTo(null);

        groups = new ArrayList<>();
        groups.add("Group A");
        groups.add("Group B");
        groups.add("Group C");

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Panel for profile and logout buttons
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        profileButton = new JButton("View Profile");
        logoutButton = new JButton("Logout");

        // Adding listeners to the profile and logout buttons
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to user profile page
                new UserProfilePage(client, true).setVisible(true);
                dispose();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle logout logic here
                JOptionPane.showMessageDialog(null, "Logged out successfully!");
                new WelcomePage(client).setVisible(true);
                dispose();
            }
        });

        topPanel.add(profileButton);
        topPanel.add(logoutButton);

        // Panel for group list and join code input
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new GridLayout(2, 1));

        // Model for the list of groups
        groupListModel = new DefaultListModel<>();
        updateGroupList();

        // JList to display groups
        groupList = new JList<>(groupListModel);
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedGroup = groupList.getSelectedValue();
                if (selectedGroup != null) {
                    // Navigate to selected group page
                    new GroupPage(client, selectedGroup).setVisible(true);
                    dispose();
                }
            }
        });

        // Text field and button for entering join code
        joinCodeField = new JTextField();
        joinGroupButton = new JButton("Submit Join Code");

        joinGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String joinCode = joinCodeField.getText().trim();
                if (!joinCode.isEmpty()) {
                    // Add the new group to the list and update the display
                    groups.add(joinCode);
                    updateGroupList();
                    joinCodeField.setText("");
                    JOptionPane.showMessageDialog(null, "Joined group: " + joinCode);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid join code.");
                }
            }
        });

        groupPanel.add(new JScrollPane(groupList));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(joinCodeField, BorderLayout.CENTER);
        bottomPanel.add(joinGroupButton, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(groupPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
    }

    // Method to update the JList with the groups
    private void updateGroupList() {
        groupListModel.clear();
        for (String group : groups) {
            groupListModel.addElement(group);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HomePage(null).setVisible(true);
            }
        });
    }
}