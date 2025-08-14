package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Booking model class for Airline Reservation System
 * Represents flight booking information
 */
public class Booking {
    private int bookingId;
    private int userId;
    private int flightId;
    private String passengerName;
    private String seatNumber;
    private LocalDateTime bookingDate;
    private double totalAmount;
    private String status; // "CONFIRMED", "CANCELLED", "PENDING"

    // Flight details for display (not stored in DB)
    private String flightNumber;
    private String airline;
    private String source;
    private String destination;
    private LocalDateTime departureTime;

    // Default constructor
    public Booking() {
        this.bookingDate = LocalDateTime.now();
        this.status = "CONFIRMED";
    }

    // Constructor with parameters
    public Booking(int userId, int flightId, String passengerName, String seatNumber, 
                   double totalAmount) {
        this();
        this.userId = userId;
        this.flightId = flightId;
        this.passengerName = passengerName;
        this.seatNumber = seatNumber;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getFlightId() { return flightId; }
    public void setFlightId(int flightId) { this.flightId = flightId; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Flight details getters and setters
    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getAirline() { return airline; }
    public void setAirline(String airline) { this.airline = airline; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    // Utility methods
    public String getFormattedBookingDate() {
        return bookingDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public String getFormattedDepartureTime() {
        if (departureTime != null) {
            return departureTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        }
        return "";
    }

    public String getBookingReference() {
        return "AR" + String.format("%06d", bookingId);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", reference='" + getBookingReference() + "'" +
                ", passengerName='" + passengerName + "'" +
                ", flightNumber='" + flightNumber + "'" +
                ", seatNumber='" + seatNumber + "'" +
                ", status='" + status + "'" +
                "}";
    }
}
