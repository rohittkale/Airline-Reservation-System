package gui;

import utils.CustomerDashboard;
import utils.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Login extends JFrame implements ActionListener {

    // GUI Components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton resetButton;
    private JCheckBox showPasswordCheckBox;
    private JComboBox<String> roleComboBox;

    // Constructor
    public Login() {
        initializeComponents();
        setupLayout();
        setFrameProperties();
    }

    /**
     * Initialize GUI components
     */
    private void initializeComponents() {
        // Text fields
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        // Buttons
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        resetButton = new JButton("Reset");

        // Checkbox for show password
        showPasswordCheckBox = new JCheckBox("Show Password");

        // Role selection
        String[] roles = {"Customer", "Admin"};
        roleComboBox = new JComboBox<>(roles);

        // Add action listeners
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        resetButton.addActionListener(this);

        // Show password functionality
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        });
    }

    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Airline Reservation System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);

        // Login Panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        // Show Password
        gbc.gridx = 1; gbc.gridy = 2;
        loginPanel.add(showPasswordCheckBox, gbc);

        // Role
        gbc.gridx = 0; gbc.gridy = 3;
        loginPanel.add(new JLabel("Login as:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(roleComboBox, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(registerButton);

        // Add panels to main frame
        add(titlePanel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Set frame properties
     */
    private void setFrameProperties() {
        setTitle("Airline Reservation System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /**
     * Handle button click events
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            handleLogin();
        } else if (e.getSource() == resetButton) {
            handleReset();
        } else if (e.getSource() == registerButton) {
            handleRegister();
        }
    }

    /**
     * Handle login functionality
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = roleComboBox.getSelectedItem().toString().toLowerCase();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both username and password.", 
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Authenticate user
        if (authenticateUser(username, password, role)) {
            JOptionPane.showMessageDialog(this, 
                "Login Successful! Welcome " + username, 
                "Success", JOptionPane.INFORMATION_MESSAGE);

            // Open appropriate dashboard based on role
            this.dispose();
            if (role.equals("admin")) {
                new AdminDashboard(username).setVisible(true);
            } else {
                new CustomerDashboard(username).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid username, password, or role. Please try again.", 
                "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Authenticate user against database
     */
    private boolean authenticateUser(String username, String password, String role) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ? AND role = ?";

        try {
            ResultSet rs = DBConnection.executeQuery(query, username, password, role);
            if (rs != null && rs.next()) {
                DBConnection.closeResultSet(rs);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Database error occurred. Please try again later.", 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    /**
     * Handle reset functionality
     */
    private void handleReset() {
        usernameField.setText("");
        passwordField.setText("");
        showPasswordCheckBox.setSelected(false);
        passwordField.setEchoChar('*');
        roleComboBox.setSelectedIndex(0);
        usernameField.requestFocus();
    }

    /**
     * Handle register functionality
     */
    private void handleRegister() {
        this.dispose();
        new Register().setVisible(true);
    }

    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Login().setVisible(true);
        });
    }
}