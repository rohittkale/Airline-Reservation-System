package gui;


import utils.DBConnection;
import model.Flight;
import model.Booking;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * BookingForm GUI class for Airline Reservation System
 * Handles flight search and booking functionality
 */
public class BookingForm extends JFrame implements ActionListener {

    // GUI Components
    private JTextField sourceField;
    private JTextField destinationField;
    private JDateChooser dateChooser;
    private JButton searchButton;
    private JButton bookButton;
    private JButton resetButton;
    private JTable flightTable;
    private DefaultTableModel tableModel;
    private JTextArea bookingDetailsArea;
    private JTextField passengerNameField;
    private JComboBox<String> seatClassCombo;
    private JLabel totalAmountLabel;

    // Current user and selected flight
    private String currentUser;
    private Flight selectedFlight;

    // Constructor
    public BookingForm(String username) {
        this.currentUser = username;
        initializeComponents();
        setupLayout();
        setFrameProperties();
        loadFlights();
    }

    /**
     * Initialize GUI components
     */
    private void initializeComponents() {
        // Search components
        sourceField = new JTextField(15);
        destinationField = new JTextField(15);
        dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(150, 25));

        // Buttons
        searchButton = new JButton("Search Flights");
        bookButton = new JButton("Book Flight");
        resetButton = new JButton("Reset");

        // Flight table
        String[] columns = {"Flight No", "Airline", "Source", "Destination", 
                           "Departure", "Arrival", "Price", "Available Seats"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        flightTable = new JTable(tableModel);
        flightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Booking components
        passengerNameField = new JTextField(20);
        String[] seatClasses = {"Economy", "Business", "First Class"};
        seatClassCombo = new JComboBox<>(seatClasses);
        totalAmountLabel = new JLabel("₹0.00");
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Booking details area
        bookingDetailsArea = new JTextArea(8, 30);
        bookingDetailsArea.setEditable(false);
        bookingDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Add action listeners
        searchButton.addActionListener(this);
        bookButton.addActionListener(this);
        resetButton.addActionListener(this);

        // Table selection listener
        flightTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectFlight();
            }
        });

        // Seat class change listener
        seatClassCombo.addActionListener(e -> updateTotalAmount());
    }

    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Flight Booking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);

        // Search Panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Flights"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        searchPanel.add(new JLabel("From:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(sourceField, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        searchPanel.add(new JLabel("To:"), gbc);
        gbc.gridx = 3;
        searchPanel.add(destinationField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        searchPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(dateChooser, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        gbc.gridwidth = 2;
        JPanel searchButtonPanel = new JPanel();
        searchButtonPanel.add(searchButton);
        searchButtonPanel.add(resetButton);
        searchPanel.add(searchButtonPanel, gbc);

        // Flight Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Available Flights"));
        JScrollPane tableScrollPane = new JScrollPane(flightTable);
        tableScrollPane.setPreferredSize(new Dimension(800, 200));
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Booking Panel
        JPanel bookingPanel = new JPanel(new BorderLayout());
        bookingPanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));

        JPanel bookingFormPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        bookingFormPanel.add(new JLabel("Passenger Name:"), gbc);
        gbc.gridx = 1;
        bookingFormPanel.add(passengerNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        bookingFormPanel.add(new JLabel("Seat Class:"), gbc);
        gbc.gridx = 1;
        bookingFormPanel.add(seatClassCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        bookingFormPanel.add(new JLabel("Total Amount:"), gbc);
        gbc.gridx = 1;
        bookingFormPanel.add(totalAmountLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        bookingFormPanel.add(bookButton, gbc);

        JScrollPane detailsScrollPane = new JScrollPane(bookingDetailsArea);

        bookingPanel.add(bookingFormPanel, BorderLayout.WEST);
        bookingPanel.add(detailsScrollPane, BorderLayout.CENTER);

        // Main layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(bookingPanel, BorderLayout.SOUTH);
    }

    /**
     * Set frame properties
     */
    private void setFrameProperties() {
        setTitle("Airline Reservation System - Book Flight");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
    }

    /**
     * Handle button click events
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            handleFlightSearch();
        } else if (e.getSource() == bookButton) {
            handleFlightBooking();
        } else if (e.getSource() == resetButton) {
            handleReset();
        }
    }

    /**
     * Handle flight search
     */
    private void handleFlightSearch() {
        String source = sourceField.getText().trim();
        String destination = destinationField.getText().trim();

        if (source.isEmpty() || destination.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both source and destination.", 
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        searchFlights(source, destination);
    }

    /**
     * Search flights in database
     */
    private void searchFlights(String source, String destination) {
        String query = "SELECT * FROM flights WHERE source = ? AND destination = ? AND status = 'ACTIVE' AND available_seats > 0";

        try {
            ResultSet rs = DBConnection.executeQuery(query, source, destination);
            tableModel.setRowCount(0); // Clear existing data

            if (rs != null) {
                boolean hasResults = false;
                while (rs.next()) {
                    hasResults = true;
                    Object[] row = {
                        rs.getString("flight_number"),
                        rs.getString("airline"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getString("departure_time"),
                        rs.getString("arrival_time"),
                        "₹" + rs.getDouble("price"),
                        rs.getInt("available_seats")
                    };
                    tableModel.addRow(row);
                }

                if (!hasResults) {
                    JOptionPane.showMessageDialog(this, 
                        "No flights found for the selected route.", 
                        "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }

                DBConnection.closeResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error searching flights: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error occurred while searching flights.", 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Load all flights on startup
     */
    private void loadFlights() {
        String query = "SELECT * FROM flights WHERE status = 'ACTIVE' ORDER BY departure_time";

        try {
            ResultSet rs = DBConnection.executeQuery(query);
            tableModel.setRowCount(0);

            if (rs != null) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("flight_number"),
                        rs.getString("airline"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getString("departure_time"),
                        rs.getString("arrival_time"),
                        "₹" + rs.getDouble("price"),
                        rs.getInt("available_seats")
                    };
                    tableModel.addRow(row);
                }
                DBConnection.closeResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error loading flights: " + e.getMessage());
        }
    }

    /**
     * Handle flight selection
     */
    private void selectFlight() {
        int selectedRow = flightTable.getSelectedRow();
        if (selectedRow != -1) {
            String flightNumber = (String) tableModel.getValueAt(selectedRow, 0);
            loadSelectedFlight(flightNumber);
            updateTotalAmount();
        }
    }

    /**
     * Load selected flight details
     */
    private void loadSelectedFlight(String flightNumber) {
        String query = "SELECT * FROM flights WHERE flight_number = ?";

        try {
            ResultSet rs = DBConnection.executeQuery(query, flightNumber);
            if (rs != null && rs.next()) {
                selectedFlight = new Flight();
                selectedFlight.setFlightId(rs.getInt("flight_id"));
                selectedFlight.setFlightNumber(rs.getString("flight_number"));
                selectedFlight.setAirline(rs.getString("airline"));
                selectedFlight.setSource(rs.getString("source"));
                selectedFlight.setDestination(rs.getString("destination"));
                selectedFlight.setPrice(rs.getDouble("price"));
                selectedFlight.setAvailableSeats(rs.getInt("available_seats"));

                DBConnection.closeResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error loading selected flight: " + e.getMessage());
        }
    }

    /**
     * Update total amount based on seat class
     */
    private void updateTotalAmount() {
        if (selectedFlight != null) {
            double basePrice = selectedFlight.getPrice();
            String seatClass = (String) seatClassCombo.getSelectedItem();
            double multiplier = 1.0;

            switch (seatClass) {
                case "Business":
                    multiplier = 1.5;
                    break;
                case "First Class":
                    multiplier = 2.0;
                    break;
                default:
                    multiplier = 1.0;
            }

            double totalAmount = basePrice * multiplier;
            totalAmountLabel.setText("₹" + String.format("%.2f", totalAmount));
        }
    }

    /**
     * Handle flight booking
     */
    private void handleFlightBooking() {
        if (selectedFlight == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a flight first.", 
                "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String passengerName = passengerNameField.getText().trim();
        if (passengerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter passenger name.", 
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Generate seat number
        String seatNumber = generateSeatNumber();
        double totalAmount = Double.parseDouble(totalAmountLabel.getText().replace("₹", ""));

        // Create booking
        if (createBooking(passengerName, seatNumber, totalAmount)) {
            displayBookingConfirmation(passengerName, seatNumber, totalAmount);
            JOptionPane.showMessageDialog(this, 
                "Flight booked successfully! Check booking details below.", 
                "Booking Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Booking failed. Please try again.", 
                "Booking Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Create booking in database
     */
    private boolean createBooking(String passengerName, String seatNumber, double totalAmount) {
        try {
            // Get user ID
            String userQuery = "SELECT user_id FROM users WHERE email = ?";
            ResultSet userRs = DBConnection.executeQuery(userQuery, currentUser);
            int userId = 0;
            if (userRs != null && userRs.next()) {
                userId = userRs.getInt("user_id");
                DBConnection.closeResultSet(userRs);
            }

            // Insert booking
            String bookingQuery = "INSERT INTO bookings (user_id, flight_id, passenger_name, seat_number, total_amount, status) VALUES (?, ?, ?, ?, ?, 'CONFIRMED')";
            int result = DBConnection.executeUpdate(bookingQuery, 
                userId, selectedFlight.getFlightId(), passengerName, seatNumber, totalAmount);

            if (result > 0) {
                // Update available seats
                String updateQuery = "UPDATE flights SET available_seats = available_seats - 1 WHERE flight_id = ?";
                DBConnection.executeUpdate(updateQuery, selectedFlight.getFlightId());

                // Refresh flight table
                loadFlights();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating booking: " + e.getMessage());
        }
        return false;
    }

    /**
     * Generate seat number
     */
    private String generateSeatNumber() {
        String seatClass = (String) seatClassCombo.getSelectedItem();
        String prefix = seatClass.equals("Economy") ? "E" : 
                       seatClass.equals("Business") ? "B" : "F";
        int seatNum = (int) (Math.random() * 50) + 1;
        return prefix + String.format("%02d", seatNum);
    }

    /**
     * Display booking confirmation
     */
    private void displayBookingConfirmation(String passengerName, String seatNumber, double totalAmount) {
        StringBuilder details = new StringBuilder();
        details.append("\n========== BOOKING CONFIRMATION ==========\n");
        details.append("Flight Number: ").append(selectedFlight.getFlightNumber()).append("\n");
        details.append("Airline: ").append(selectedFlight.getAirline()).append("\n");
        details.append("Route: ").append(selectedFlight.getSource()).append(" → ").append(selectedFlight.getDestination()).append("\n");
        details.append("Passenger: ").append(passengerName).append("\n");
        details.append("Seat Number: ").append(seatNumber).append("\n");
        details.append("Seat Class: ").append(seatClassCombo.getSelectedItem()).append("\n");
        details.append("Total Amount: ₹").append(String.format("%.2f", totalAmount)).append("\n");
        details.append("Booking Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))).append("\n");
        details.append("Status: CONFIRMED\n");
        details.append("==========================================\n");

        bookingDetailsArea.setText(details.toString());
    }

    /**
     * Handle reset functionality
     */
    private void handleReset() {
        sourceField.setText("");
        destinationField.setText("");
        dateChooser.setDate(null);
        passengerNameField.setText("");
        seatClassCombo.setSelectedIndex(0);
        totalAmountLabel.setText("₹0.00");
        bookingDetailsArea.setText("");
        selectedFlight = null;
        flightTable.clearSelection();
        loadFlights();
    }
}

// Note: JDateChooser requires external library (JCalendar)
// For compilation without external libraries, you can replace it with JTextField
class JDateChooser extends JTextField {
    public JDateChooser() {
        super();
        setToolTipText("Enter date in DD-MM-YYYY format");
    }

    public java.util.Date getDate() {
        // Simple date parsing - in real implementation, use proper date parsing
        try {
            String text = getText();
            if (!text.isEmpty()) {
                return new java.util.Date(); // Return current date for demo
            }
        } catch (Exception e) {
            // Handle parsing error
        }
        return null;
    }

    public void setDate(java.util.Date date) {
        if (date == null) {
            setText("");
        } else {
            setText(new java.text.SimpleDateFormat("dd-MM-yyyy").format(date));
        }
    }
}