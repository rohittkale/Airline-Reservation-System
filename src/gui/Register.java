package gui;



import utils.DBConnection;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

/**
 * Register GUI class for Airline Reservation System
 * Handles new user registration
 */
public class Register extends JFrame implements ActionListener {

    // GUI Components
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField phoneField;
    private JTextArea addressArea;
    private JButton registerButton;
    private JButton backButton;
    private JButton resetButton;

    // Constructor
    public Register() {
        initializeComponents();
        setupLayout();
        setFrameProperties();
    }

    /**
     * Initialize GUI components
     */
    private void initializeComponents() {
        // Text fields
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        phoneField = new JTextField(20);
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);

        // Buttons
        registerButton = new JButton("Register");
        backButton = new JButton("Back to Login");
        resetButton = new JButton("Reset");

        // Add action listeners
        registerButton.addActionListener(this);
        backButton.addActionListener(this);
        resetButton.addActionListener(this);
    }

    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("User Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);

        // Registration Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Full Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        formPanel.add(addressScrollPane, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(registerButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);

        // Add panels to main frame
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Set frame properties
     */
    private void setFrameProperties() {
        setTitle("Airline Reservation System - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /**
     * Handle button click events
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            handleRegistration();
        } else if (e.getSource() == resetButton) {
            handleReset();
        } else if (e.getSource() == backButton) {
            handleBackToLogin();
        }
    }

    /**
     * Handle user registration
     */
    private void handleRegistration() {
        // Get input values
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();

        // Validate input
        if (!validateInput(name, email, password, confirmPassword, phone, address)) {
            return;
        }

        // Check if email already exists
        if (emailExists(email)) {
            JOptionPane.showMessageDialog(this, 
                "Email already registered. Please use a different email.", 
                "Registration Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create user and register
        User newUser = new User(name, email, password, phone, address);
        if (registerUser(newUser)) {
            JOptionPane.showMessageDialog(this, 
                "Registration successful! You can now login with your credentials.", 
                "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            handleBackToLogin();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Registration failed. Please try again.", 
                "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Validate user input
     */
    private boolean validateInput(String name, String email, String password, 
                                  String confirmPassword, String phone, String address) {

        // Check for empty fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || 
            confirmPassword.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all fields.", 
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validate name (at least 2 characters)
        if (name.length() < 2) {
            JOptionPane.showMessageDialog(this, 
                "Name must be at least 2 characters long.", 
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid email address.", 
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validate password (at least 6 characters)
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "Password must be at least 6 characters long.", 
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Check password confirmation
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Passwords do not match.", 
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validate phone (basic check for numbers and length)
        if (!isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid phone number (10-15 digits).", 
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Validate email format using regex
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                           "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    /**
     * Validate phone number
     */
    private boolean isValidPhone(String phone) {
        String phoneRegex = "^[0-9]{10,15}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        return pattern.matcher(phone.replaceAll("[\\s\\-\\(\\)]", "")).matches();
    }

    /**
     * Check if email already exists in database
     */
    private boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try {
            java.sql.ResultSet rs = DBConnection.executeQuery(query, email);
            if (rs != null && rs.next()) {
                int count = rs.getInt(1);
                DBConnection.closeResultSet(rs);
                return count > 0;
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        }
        return false;
    }

    /**
     * Register new user in database
     */
    private boolean registerUser(User user) {
        String query = "INSERT INTO users (name, email, password, role, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            int result = DBConnection.executeUpdate(query, 
                user.getName(), user.getEmail(), user.getPassword(), 
                user.getRole(), user.getPhone(), user.getAddress());
            return result > 0;
        } catch (Exception e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reset form fields
     */
    private void handleReset() {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        phoneField.setText("");
        addressArea.setText("");
        nameField.requestFocus();
    }

    /**
     * Go back to login screen
     */
    private void handleBackToLogin() {
        this.dispose();
        new Login().setVisible(true);
    }
}
