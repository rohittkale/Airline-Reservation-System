package gui;

import utils.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AdminDashboard GUI class for Airline Reservation System
 * Handles admin operations like managing flights, viewing bookings, and generating reports
 */
public class AdminDashboard extends JFrame implements ActionListener {

    // GUI Components
    private JTabbedPane tabbedPane;
    private JTable flightTable, bookingTable;
    private DefaultTableModel flightTableModel, bookingTableModel;

    // Flight management components
    private JTextField flightNumberField, airlineField, sourceField, destinationField;
    private JTextField departureTimeField, arrivalTimeField, priceField, totalSeatsField;
    private JButton addFlightButton, updateFlightButton, deleteFlightButton, refreshButton;

    // Current admin user
    private String currentAdmin;

    // Constructor
    public AdminDashboard(String adminName) {
        this.currentAdmin = adminName;
        initializeComponents();
        setupLayout();
        setFrameProperties();
        loadData();
    }

    /**
     * Initialize GUI components
     */
    private void initializeComponents() {
        tabbedPane = new JTabbedPane();

        // Flight management components
        flightNumberField = new JTextField(15);
        airlineField = new JTextField(15);
        sourceField = new JTextField(15);
        destinationField = new JTextField(15);
        departureTimeField = new JTextField(15);
        arrivalTimeField = new JTextField(15);
        priceField = new JTextField(15);
        totalSeatsField = new JTextField(15);

        addFlightButton = new JButton("Add Flight");
        updateFlightButton = new JButton("Update Flight");
        deleteFlightButton = new JButton("Delete Flight");
        refreshButton = new JButton("Refresh");

        // Flight table
        String[] flightColumns = {"Flight ID", "Flight No", "Airline", "Source", "Destination",
                "Departure", "Arrival", "Price", "Total Seats", "Available", "Status"};
        flightTableModel = new DefaultTableModel(flightColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        flightTable = new JTable(flightTableModel);
        flightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Booking table
        String[] bookingColumns = {"Booking ID", "User Email", "Flight No", "Passenger",
                "Seat", "Amount", "Date", "Status"};
        bookingTableModel = new DefaultTableModel(bookingColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookingTable = new JTable(bookingTableModel);

        // Add action listeners
        addFlightButton.addActionListener(this);
        updateFlightButton.addActionListener(this);
        deleteFlightButton.addActionListener(this);
        refreshButton.addActionListener(this);

        // Table selection listener
        flightTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedFlightData();
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
        JLabel titleLabel = new JLabel("Admin Dashboard - Welcome " + currentAdmin);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);

        // Flight Management Tab
        JPanel flightManagementTab = createFlightManagementTab();
        tabbedPane.addTab("Flight Management", flightManagementTab);

        // Booking Management Tab
        JPanel bookingManagementTab = createBookingManagementTab();
        tabbedPane.addTab("View Bookings", bookingManagementTab);

        // Reports Tab
        JPanel reportsTab = createReportsTab();
        tabbedPane.addTab("Reports", reportsTab);

        add(titlePanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        // Menu Bar
        createMenuBar();
    }

    /**
     * Create flight management tab
     */
    private JPanel createFlightManagementTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Flight form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Flight Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // First row
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Flight Number:"), gbc);
        gbc.gridx = 1;
        formPanel.add(flightNumberField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Airline:"), gbc);
        gbc.gridx = 3;
        formPanel.add(airlineField, gbc);

        // Second row
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Source:"), gbc);
        gbc.gridx = 1;
        formPanel.add(sourceField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Destination:"), gbc);
        gbc.gridx = 3;
        formPanel.add(destinationField, gbc);

        // Third row
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Departure Time:"), gbc);
        gbc.gridx = 1;
        formPanel.add(departureTimeField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Arrival Time:"), gbc);
        gbc.gridx = 3;
        formPanel.add(arrivalTimeField, gbc);

        // Fourth row
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Total Seats:"), gbc);
        gbc.gridx = 3;
        formPanel.add(totalSeatsField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addFlightButton);
        buttonPanel.add(updateFlightButton);
        buttonPanel.add(deleteFlightButton);
        buttonPanel.add(refreshButton);

        // Flight table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Flights"));
        JScrollPane flightScrollPane = new JScrollPane(flightTable);
        flightScrollPane.setPreferredSize(new Dimension(800, 300));
        tablePanel.add(flightScrollPane, BorderLayout.CENTER);

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    /**
     * Create booking management tab
     */
    private JPanel createBookingManagementTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("All Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);

        JScrollPane bookingScrollPane = new JScrollPane(bookingTable);
        bookingScrollPane.setPreferredSize(new Dimension(800, 400));

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(bookingScrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * Create reports tab
     */
    private JPanel createReportsTab() {
        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Revenue report
        JButton revenueButton = new JButton("Generate Revenue Report");
        revenueButton.addActionListener(e -> generateRevenueReport());

        // Flight occupancy report
        JButton occupancyButton = new JButton("Flight Occupancy Report");
        occupancyButton.addActionListener(e -> generateOccupancyReport());

        // Popular routes report
        JButton routesButton = new JButton("Popular Routes Report");
        routesButton.addActionListener(e -> generateRoutesReport());

        // Customer report
        JButton customerButton = new JButton("Customer Report");
        customerButton.addActionListener(e -> generateCustomerReport());

        mainPanel.add(revenueButton);
        mainPanel.add(occupancyButton);
        mainPanel.add(routesButton);
        mainPanel.add(customerButton);

        return mainPanel;
    }

    /**
     * Create menu bar
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Set frame properties
     */
    private void setFrameProperties() {
        setTitle("Airline Reservation System - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Handle button click events
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addFlightButton) {
            handleAddFlight();
        } else if (e.getSource() == updateFlightButton) {
            handleUpdateFlight();
        } else if (e.getSource() == deleteFlightButton) {
            handleDeleteFlight();
        } else if (e.getSource() == refreshButton) {
            loadData();
        }
    }

    /**
     * Handle add flight functionality
     */
    private void handleAddFlight() {
        if (!validateFlightInput()) {
            return;
        }

        String query = "INSERT INTO flights (flight_number, airline, source, destination, departure_time, arrival_time, price, total_seats, available_seats, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'ACTIVE')";

        try {
            int totalSeats = Integer.parseInt(totalSeatsField.getText());
            int result = DBConnection.executeUpdate(query,
                    flightNumberField.getText(), airlineField.getText(),
                    sourceField.getText(), destinationField.getText(),
                    departureTimeField.getText(), arrivalTimeField.getText(),
                    Double.parseDouble(priceField.getText()), totalSeats, totalSeats);

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Flight added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFlightForm();
                loadFlights();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add flight.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and seats.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handle update flight functionality
     */
    private void handleUpdateFlight() {
        int selectedRow = flightTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateFlightInput()) {
            return;
        }

        int flightId = (int) flightTableModel.getValueAt(selectedRow, 0);
        String query = "UPDATE flights SET flight_number=?, airline=?, source=?, destination=?, departure_time=?, arrival_time=?, price=?, total_seats=? WHERE flight_id=?";

        try {
            int result = DBConnection.executeUpdate(query,
                    flightNumberField.getText(), airlineField.getText(),
                    sourceField.getText(), destinationField.getText(),
                    departureTimeField.getText(), arrivalTimeField.getText(),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(totalSeatsField.getText()), flightId);

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Flight updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFlightForm();
                loadFlights();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handle delete flight functionality
     */
    private void handleDeleteFlight() {
        int selectedRow = flightTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this flight?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int flightId = (int) flightTableModel.getValueAt(selectedRow, 0);
            String query = "DELETE FROM flights WHERE flight_id = ?";

            try {
                int result = DBConnection.executeUpdate(query, flightId);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Flight deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFlightForm();
                    loadFlights();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Validate flight input
     */
    private boolean validateFlightInput() {
        if (flightNumberField.getText().trim().isEmpty() ||
                airlineField.getText().trim().isEmpty() ||
                sourceField.getText().trim().isEmpty() ||
                destinationField.getText().trim().isEmpty() ||
                departureTimeField.getText().trim().isEmpty() ||
                arrivalTimeField.getText().trim().isEmpty() ||
                priceField.getText().trim().isEmpty() ||
                totalSeatsField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            Double.parseDouble(priceField.getText());
            Integer.parseInt(totalSeatsField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price and Total Seats must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Clear flight form
     */
    private void clearFlightForm() {
        flightNumberField.setText("");
        airlineField.setText("");
        sourceField.setText("");
        destinationField.setText("");
        departureTimeField.setText("");
        arrivalTimeField.setText("");
        priceField.setText("");
        totalSeatsField.setText("");
    }

    /**
     * Load selected flight data into form
     */
    private void loadSelectedFlightData() {
        int selectedRow = flightTable.getSelectedRow();
        if (selectedRow != -1) {
            flightNumberField.setText((String) flightTableModel.getValueAt(selectedRow, 1));
            airlineField.setText((String) flightTableModel.getValueAt(selectedRow, 2));
            sourceField.setText((String) flightTableModel.getValueAt(selectedRow, 3));
            destinationField.setText((String) flightTableModel.getValueAt(selectedRow, 4));
            departureTimeField.setText((String) flightTableModel.getValueAt(selectedRow, 5));
            arrivalTimeField.setText((String) flightTableModel.getValueAt(selectedRow, 6));
            priceField.setText(flightTableModel.getValueAt(selectedRow, 7).toString());
            totalSeatsField.setText(flightTableModel.getValueAt(selectedRow, 8).toString());
        }
    }

    /**
     * Load data for all tables
     */
    private void loadData() {
        loadFlights();
        loadBookings();
    }

    /**
     * Load flights data
     */
    private void loadFlights() {
        String query = "SELECT * FROM flights ORDER BY flight_id";

        try {
            ResultSet rs = DBConnection.executeQuery(query);
            flightTableModel.setRowCount(0);

            if (rs != null) {
                while (rs.next()) {
                    Object[] row = {
                            rs.getInt("flight_id"),
                            rs.getString("flight_number"),
                            rs.getString("airline"),
                            rs.getString("source"),
                            rs.getString("destination"),
                            rs.getString("departure_time"),
                            rs.getString("arrival_time"),
                            rs.getDouble("price"),
                            rs.getInt("total_seats"),
                            rs.getInt("available_seats"),
                            rs.getString("status")
                    };
                    flightTableModel.addRow(row);
                }
                DBConnection.closeResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error loading flights: " + e.getMessage());
        }
    }

    /**
     * Load bookings data
     */
    private void loadBookings() {
        String query = "SELECT b.booking_id, u.email, f.flight_number, b.passenger_name, b.seat_number, b.total_amount, b.booking_date, b.status " +
                "FROM bookings b JOIN users u ON b.user_id = u.user_id JOIN flights f ON b.flight_id = f.flight_id ORDER BY b.booking_date DESC";

        try {
            ResultSet rs = DBConnection.executeQuery(query);
            bookingTableModel.setRowCount(0);

            if (rs != null) {
                while (rs.next()) {
                    Object[] row = {
                            rs.getInt("booking_id"),
                            rs.getString("email"),
                            rs.getString("flight_number"),
                            rs.getString("passenger_name"),
                            rs.getString("seat_number"),
                            "₹" + rs.getDouble("total_amount"),
                            rs.getString("booking_date"),
                            rs.getString("status")
                    };
                    bookingTableModel.addRow(row);
                }
                DBConnection.closeResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
        }
    }

    // Real report generation methods with SQL queries and dialog display

    private void generateRevenueReport() {
        String query = "SELECT f.flight_number, SUM(b.total_amount) AS total_revenue " +
                "FROM bookings b JOIN flights f ON b.flight_id = f.flight_id " +
                "WHERE b.status = 'CONFIRMED' " +
                "GROUP BY f.flight_number " +
                "ORDER BY total_revenue DESC";
        try {
            ResultSet rs = DBConnection.executeQuery(query);
            StringBuilder report = new StringBuilder("=== Revenue Report ===\n\n");
            while (rs.next()) {
                report.append("Flight: ").append(rs.getString("flight_number"))
                        .append(" - Revenue: ₹").append(String.format("%.2f", rs.getDouble("total_revenue")))
                        .append("\n");
            }
            DBConnection.closeResultSet(rs);
            showReportDialog("Revenue Report", report.toString());
        } catch (SQLException e) {
            showErrorDialog("Error generating revenue report: " + e.getMessage());
        }
    }

    private void generateOccupancyReport() {
        String query = "SELECT flight_number, " +
                "ROUND((total_seats - available_seats) * 100 / total_seats, 2) AS occupancy_percent " +
                "FROM flights " +
                "WHERE total_seats > 0 " +
                "ORDER BY occupancy_percent DESC";
        try {
            ResultSet rs = DBConnection.executeQuery(query);
            StringBuilder report = new StringBuilder("=== Flight Occupancy Report ===\n\n");
            while (rs.next()) {
                report.append("Flight: ").append(rs.getString("flight_number"))
                        .append(" - Occupancy: ").append(rs.getDouble("occupancy_percent")).append("%\n");
            }
            DBConnection.closeResultSet(rs);
            showReportDialog("Occupancy Report", report.toString());
        } catch (SQLException e) {
            showErrorDialog("Error generating occupancy report: " + e.getMessage());
        }
    }

    private void generateRoutesReport() {
        String query = "SELECT source, destination, COUNT(*) AS bookings_count " +
                "FROM bookings b JOIN flights f ON b.flight_id = f.flight_id " +
                "WHERE b.status = 'CONFIRMED' " +
                "GROUP BY source, destination " +
                "ORDER BY bookings_count DESC " +
                "LIMIT 10";
        try {
            ResultSet rs = DBConnection.executeQuery(query);
            StringBuilder report = new StringBuilder("=== Popular Routes Report (Top 10) ===\n\n");
            while (rs.next()) {
                report.append(rs.getString("source")).append(" → ").append(rs.getString("destination"))
                        .append(" - Bookings: ").append(rs.getInt("bookings_count"))
                        .append("\n");
            }
            DBConnection.closeResultSet(rs);
            showReportDialog("Popular Routes Report", report.toString());
        } catch (SQLException e) {
            showErrorDialog("Error generating routes report: " + e.getMessage());
        }
    }

    private void generateCustomerReport() {
        String query = "SELECT u.email, COUNT(b.booking_id) AS total_bookings, " +
                "IFNULL(SUM(b.total_amount),0) AS total_spent " +
                "FROM users u LEFT JOIN bookings b ON u.user_id = b.user_id " +
                "GROUP BY u.email " +
                "ORDER BY total_spent DESC " +
                "LIMIT 10";
        try {
            ResultSet rs = DBConnection.executeQuery(query);
            StringBuilder report = new StringBuilder("=== Top Customers Report (by Spending) ===\n\n");
            while (rs.next()) {
                String email = rs.getString("email");
                int bookings = rs.getInt("total_bookings");
                double spent = rs.getDouble("total_spent");
                report.append(email)
                        .append(" - Bookings: ").append(bookings)
                        .append(", Total Spent: ₹").append(String.format("%.2f", spent))
                        .append("\n");
            }
            DBConnection.closeResultSet(rs);
            showReportDialog("Customer Report", report.toString());
        } catch (SQLException e) {
            showErrorDialog("Error generating customer report: " + e.getMessage());
        }
    }

    /**
     * Utility method to display report in a scrollable dialog
     */
    private void showReportDialog(String title, String reportText) {
        JTextArea textArea = new JTextArea(reportText);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Utility method to show error message dialogs
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void logout() {
        this.dispose();
        new Login().setVisible(true);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                "Airline Reservation System v1.0\nDeveloped using Java Swing & MySQL\nAdmin Dashboard",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
