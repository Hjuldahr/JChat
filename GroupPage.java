import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GroupPage extends JFrame {

    private Client client;
    private String groupName;

    private JList<String> channelList;
    private JList<String> memberList;
    private JTextArea chatWindow;
    private JTextField chatInputField;
    private JButton sendButton, leaveButton, homeButton, settingsButton;

    public GroupPage(Client client, String groupName) {
        this.client = client;
        this.groupName = groupName;

        setTitle("Group Page - " + groupName);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Terminates the program on window close
        setLocationRelativeTo(null);

        // Panel for the main layout (split into three sections: left, middle, right)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Panel for the left section (Channel List)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        // Sample list of channels for the group
        String[] channels = {"General", "Announcements", "Random"};
        channelList = new JList<>(channels);
        channelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        channelList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedChannel = channelList.getSelectedValue();
                if (selectedChannel != null) {
                    // Display messages for the selected channel (placeholder logic)
                    chatWindow.setText("Messages from channel: " + selectedChannel);
                }
            }
        });

        JScrollPane channelScrollPane = new JScrollPane(channelList);
        leftPanel.add(channelScrollPane, BorderLayout.CENTER);

        // Panel for the middle section (Channel Chat)
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());

        chatWindow = new JTextArea();
        chatWindow.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatWindow);
        middlePanel.add(chatScrollPane, BorderLayout.CENTER);

        // Panel for the right section (Group Members List)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Sample list of group members
        String[] members = {"User1", "User2", "User3", "User4"};
        memberList = new JList<>(members);
        JScrollPane memberScrollPane = new JScrollPane(memberList);
        rightPanel.add(memberScrollPane, BorderLayout.CENTER);

        // Panel for the bottom section (Chat Input)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        chatInputField = new JTextField();
        sendButton = new JButton("Send");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to send message to the selected channel
                String message = chatInputField.getText();
                if (!message.isEmpty()) {
                    // Append the message to the chat window (for demo purposes)
                    chatWindow.append("\nYou: " + message);
                    chatInputField.setText(""); // Clear the input field
                }
            }
        });

        bottomPanel.add(chatInputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        // Panel for buttons (Leave, Home, Settings)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        leaveButton = new JButton("Leave Group");
        homeButton = new JButton("Home");
        settingsButton = new JButton("Edit Settings");

        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to leave the group
                JOptionPane.showMessageDialog(null, "You have left the group: " + groupName);
                new HomePage(client).setVisible(true); // Redirect to HomePage
                dispose();
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to the home page
                new HomePage(client).setVisible(true);
                dispose();
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to the group settings page (placeholder for now)
                new GroupSettingsPage(groupName).setVisible(true);
                dispose();
            }
        });

        buttonPanel.add(leaveButton);
        buttonPanel.add(homeButton);
        buttonPanel.add(settingsButton);

        // Add everything to the main panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(middlePanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GroupPage(null, "Sample Group").setVisible(true);
            }
        });
    }
}
