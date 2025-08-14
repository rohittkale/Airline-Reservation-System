package utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import gui.BookingForm;
import gui.Login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * CustomerDashboard GUI class for Airline Reservation System
 * Handles customer operations like booking flights and viewing bookings
 */
public class CustomerDashboard extends JFrame implements ActionListener {

    // GUI Components
    private JTabbedPane tabbedPane;
    private JTable bookingHistoryTable;
    private DefaultTableModel bookingTableModel;
    private JButton bookFlightButton, cancelBookingButton, refreshButton, logoutButton;
    private JLabel welcomeLabel;

    // Current customer user
    private String currentCustomer;

    // Constructor
    public CustomerDashboard(String customerEmail) {
        this.currentCustomer = customerEmail;
        initializeComponents();
        setupLayout();
        setFrameProperties();
        loadBookingHistory();
    }

    /**
     * Initialize GUI components
     */
    private void initializeComponents() {
        tabbedPane = new JTabbedPane();

        // Welcome label
        welcomeLabel = new JLabel("Welcome, " + currentCustomer);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Buttons
        bookFlightButton = new JButton("Book New Flight");
        cancelBookingButton = new JButton("Cancel Booking");
        refreshButton = new JButton("Refresh");
        logoutButton = new JButton("Logout");

        // Booking history table
        String[] columns = {"Booking ID", "Flight No", "Airline", "Route", "Passenger", 
                           "Seat", "Amount", "Date", "Status"};
        bookingTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookingHistoryTable = new JTable(bookingTableModel);
        bookingHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add action listeners
        bookFlightButton.addActionListener(this);
        cancelBookingButton.addActionListener(this);
        refreshButton.addActionListener(this);
        logoutButton.addActionListener(this);
    }

    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Customer Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.add(welcomeLabel);
        userPanel.add(Box.createHorizontalStrut(20));
        userPanel.add(logoutButton);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        // Main content tabs
        createTabs();

        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        // Create menu bar
        createMenuBar();
    }

    /**
     * Create tabbed interface
     */
    private void createTabs() {
        // Booking History Tab
        JPanel bookingHistoryTab = createBookingHistoryTab();
        tabbedPane.addTab("My Bookings", bookingHistoryTab);

        // Quick Book Tab
        JPanel quickBookTab = createQuickBookTab();
        tabbedPane.addTab("Book Flight", quickBookTab);

        // Profile Tab
        JPanel profileTab = createProfileTab();
        tabbedPane.addTab("My Profile", profileTab);
    }

    /**
     * Create booking history tab
     */
    private JPanel createBookingHistoryTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Your Booking History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(refreshButton);
        buttonPanel.add(cancelBookingButton);

        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(bookingHistoryTable);
        tableScrollPane.setPreferredSize(new Dimension(800, 400));

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(tableScrollPane, BorderLayout.SOUTH);

        return mainPanel;
    }

    /**
     * Create quick book tab
     */
    private JPanel createQuickBookTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Welcome message
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeMsg = new JLabel("<html><h2>Book Your Next Flight</h2>" +
            "<p>Click the button below to search and book flights</p></html>");
        welcomePanel.add(welcomeMsg);

        // Book button panel
        JPanel bookButtonPanel = new JPanel();
        JButton largeBookButton = new JButton("Search & Book Flights");
        largeBookButton.setFont(new Font("Arial", Font.BOLD, 16));
        largeBookButton.setPreferredSize(new Dimension(250, 50));
        largeBookButton.addActionListener(e -> openBookingForm());
        bookButtonPanel.add(largeBookButton);

        // Recent flights info
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder("Quick Info"));
        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>" +
            "• Search flights by route and date<br>" +
            "• Compare prices across different airlines<br>" +
            "• Choose your preferred seat class<br>" +
            "• Instant booking confirmation<br>" +
            "• E-ticket generation</div></html>");
        infoPanel.add(infoLabel);

        mainPanel.add(welcomePanel, BorderLayout.NORTH);
        mainPanel.add(bookButtonPanel, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    /**
     * Create profile tab
     */
    private JPanel createProfileTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Profile info panel
        JPanel profileInfoPanel = new JPanel(new GridBagLayout());
        profileInfoPanel.setBorder(BorderFactory.createTitledBorder("Profile Information"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Load and display user profile
        displayUserProfile(profileInfoPanel, gbc);

        // Statistics panel
        JPanel statsPanel = createStatsPanel();

        mainPanel.add(profileInfoPanel, BorderLayout.NORTH);
        mainPanel.add(statsPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * Display user profile information
     */
    private void displayUserProfile(JPanel panel, GridBagConstraints gbc) {
        String query = "SELECT * FROM users WHERE email = ?";

        try {
            ResultSet rs = DBConnection.executeQuery(query, currentCustomer);
            if (rs != null && rs.next()) {
                gbc.gridx = 0; gbc.gridy = 0;
                panel.add(new JLabel("Name:"), gbc);
                gbc.gridx = 1;
                panel.add(new JLabel(rs.getString("name")), gbc);

                gbc.gridx = 0; gbc.gridy = 1;
                panel.add(new JLabel("Email:"), gbc);
                gbc.gridx = 1;
                panel.add(new JLabel(rs.getString("email")), gbc);

                gbc.gridx = 0; gbc.gridy = 2;
                panel.add(new JLabel("Phone:"), gbc);
                gbc.gridx = 1;
                panel.add(new JLabel(rs.getString("phone")), gbc);

                gbc.gridx = 0; gbc.gridy = 3;
                panel.add(new JLabel("Address:"), gbc);
                gbc.gridx = 1;
                JTextArea addressArea = new JTextArea(rs.getString("address"));
                addressArea.setEditable(false);
                addressArea.setRows(3);
                panel.add(new JScrollPane(addressArea), gbc);

                DBConnection.closeResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error loading user profile: " + e.getMessage());
        }
    }

    /**
     * Create statistics panel
     */
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Your Travel Statistics"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Get statistics from database
        int totalBookings = getTotalBookings();
        double totalSpent = getTotalSpent();
        int cancelledBookings = getCancelledBookings();
        String favoriteRoute = getFavoriteRoute();

        // Create stat cards
        JPanel totalBookingsPanel = createStatCard("Total Bookings", String.valueOf(totalBookings));
        JPanel totalSpentPanel = createStatCard("Total Spent", "₹" + String.format("%.2f", totalSpent));
        JPanel cancelledPanel = createStatCard("Cancelled", String.valueOf(cancelledBookings));
        JPanel favoriteRoutePanel = createStatCard("Favorite Route", favoriteRoute);

        statsPanel.add(totalBookingsPanel);
        statsPanel.add(totalSpentPanel);
        statsPanel.add(cancelledPanel);
        statsPanel.add(favoriteRoutePanel);

        return statsPanel;
    }

    /**
     * Create a statistics card
     */
    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createRaisedBevelBorder());
        card.setBackground(Color.LIGHT_GRAY);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Create menu bar
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Booking menu
        JMenu bookingMenu = new JMenu("Booking");
        JMenuItem newBookingItem = new JMenuItem("New Booking");
        newBookingItem.addActionListener(e -> openBookingForm());
        JMenuItem viewBookingsItem = new JMenuItem("View My Bookings");
        viewBookingsItem.addActionListener(e -> tabbedPane.setSelectedIndex(0));

        bookingMenu.add(newBookingItem);
        bookingMenu.add(viewBookingsItem);

        // Account menu
        JMenu accountMenu = new JMenu("Account");
        JMenuItem profileItem = new JMenuItem("My Profile");
        profileItem.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(this);

        accountMenu.add(profileItem);
        accountMenu.addSeparator();
        accountMenu.add(logoutItem);

        menuBar.add(bookingMenu);
        menuBar.add(accountMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Set frame properties
     */
    private void setFrameProperties() {
        setTitle("Airline Reservation System - Customer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    /**
     * Handle button click events
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bookFlightButton) {
            openBookingForm();
        } else if (e.getSource() == cancelBookingButton) {
            handleCancelBooking();
        } else if (e.getSource() == refreshButton) {
            loadBookingHistory();
        } else if (e.getSource() == logoutButton) {
            handleLogout();
        }
    }

    /**
     * Open booking form
     */
    private void openBookingForm() {
        new BookingForm(currentCustomer).setVisible(true);
    }

    /**
     * Handle booking cancellation
     */
    private void handleCancelBooking() {
        int selectedRow = bookingHistoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a booking to cancel.", 
                "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String status = (String) bookingTableModel.getValueAt(selectedRow, 8);
        if (!"CONFIRMED".equals(status)) {
            JOptionPane.showMessageDialog(this, 
                "Only confirmed bookings can be cancelled.", 
                "Cancellation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to cancel this booking?", 
            "Confirm Cancellation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int bookingId = (int) bookingTableModel.getValueAt(selectedRow, 0);

            if (cancelBooking(bookingId)) {
                JOptionPane.showMessageDialog(this, 
                    "Booking cancelled successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBookingHistory();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to cancel booking.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Cancel booking in database
     */
    private boolean cancelBooking(int bookingId) {
        try {
            // Update booking status
            String updateBookingQuery = "UPDATE bookings SET status = 'CANCELLED' WHERE booking_id = ?";
            int result = DBConnection.executeUpdate(updateBookingQuery, bookingId);

            if (result > 0) {
                // Increment available seats
                String updateFlightQuery = "UPDATE flights f JOIN bookings b ON f.flight_id = b.flight_id SET f.available_seats = f.available_seats + 1 WHERE b.booking_id = ?";
                DBConnection.executeUpdate(updateFlightQuery, bookingId);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error cancelling booking: " + e.getMessage());
        }
        return false;
    }

    /**
     * Handle logout
     */
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new Login().setVisible(true);
        }
    }

    /**
     * Load booking history
     */
    private void loadBookingHistory() {
        String query = "SELECT b.booking_id, f.flight_number, f.airline, " +
                      "CONCAT(f.source, ' → ', f.destination) as route, " +
                      "b.passenger_name, b.seat_number, b.total_amount, " +
                      "b.booking_date, b.status " +
                      "FROM bookings b " +
                      "JOIN flights f ON b.flight_id = f.flight_id " +
                      "JOIN users u ON b.user_id = u.user_id " +
                      "WHERE u.email = ? " +
                      "ORDER BY b.booking_date DESC";

        try {
            ResultSet rs = DBConnection.executeQuery(query, currentCustomer);
            bookingTableModel.setRowCount(0);

            if (rs != null) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("booking_id"),
                        rs.getString("flight_number"),
                        rs.getString("airline"),
                        rs.getString("route"),
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
            System.err.println("Error loading booking history: " + e.getMessage());
        }
    }

    // Statistics methods
    private int getTotalBookings() {
        String query = "SELECT COUNT(*) FROM bookings b JOIN users u ON b.user_id = u.user_id WHERE u.email = ?";
        try {
            ResultSet rs = DBConnection.executeQuery(query, currentCustomer);
            if (rs != null && rs.next()) {
                int count = rs.getInt(1);
                DBConnection.closeResultSet(rs);
                return count;
            }
        } catch (SQLException e) {
            System.err.println("Error getting total bookings: " + e.getMessage());
        }
        return 0;
    }

    private double getTotalSpent() {
        String query = "SELECT SUM(total_amount) FROM bookings b JOIN users u ON b.user_id = u.user_id WHERE u.email = ? AND b.status != 'CANCELLED'";
        try {
            ResultSet rs = DBConnection.executeQuery(query, currentCustomer);
            if (rs != null && rs.next()) {
                double total = rs.getDouble(1);
                DBConnection.closeResultSet(rs);
                return total;
            }
        } catch (SQLException e) {
            System.err.println("Error getting total spent: " + e.getMessage());
        }
        return 0.0;
    }

    private int getCancelledBookings() {
        String query = "SELECT COUNT(*) FROM bookings b JOIN users u ON b.user_id = u.user_id WHERE u.email = ? AND b.status = 'CANCELLED'";
        try {
            ResultSet rs = DBConnection.executeQuery(query, currentCustomer);
            if (rs != null && rs.next()) {
                int count = rs.getInt(1);
                DBConnection.closeResultSet(rs);
                return count;
            }
        } catch (SQLException e) {
            System.err.println("Error getting cancelled bookings: " + e.getMessage());
        }
        return 0;
    }

    private String getFavoriteRoute() {
        String query = "SELECT CONCAT(f.source, ' → ', f.destination) as route, COUNT(*) as count " +
                      "FROM bookings b " +
                      "JOIN flights f ON b.flight_id = f.flight_id " +
                      "JOIN users u ON b.user_id = u.user_id " +
                      "WHERE u.email = ? AND b.status != 'CANCELLED' " +
                      "GROUP BY f.source, f.destination " +
                      "ORDER BY count DESC LIMIT 1";
        try {
            ResultSet rs = DBConnection.executeQuery(query, currentCustomer);
            if (rs != null && rs.next()) {
                String route = rs.getString("route");
                DBConnection.closeResultSet(rs);
                return route;
            }
        } catch (SQLException e) {
            System.err.println("Error getting favorite route: " + e.getMessage());
        }
        return "None";
    }
}