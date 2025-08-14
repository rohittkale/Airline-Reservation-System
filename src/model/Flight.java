package model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Flight model class for Airline Reservation System
 * Represents flight information
 */
public class Flight {
    private int flightId;
    private String flightNumber;
    private String airline;
    private String source;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double price;
    private int totalSeats;
    private int availableSeats;
    private String status; // "ACTIVE", "CANCELLED", "DELAYED"

    // Default constructor
    public Flight() {
    }

    // Constructor with parameters
    public Flight(int flightId, String flightNumber, String airline, String source, 
                  String destination, LocalDateTime departureTime, LocalDateTime arrivalTime, 
                  double price, int totalSeats, int availableSeats) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.status = "ACTIVE";
    }

    // Getters and Setters
    public int getFlightId() { return flightId; }
    public void setFlightId(int flightId) { this.flightId = flightId; }

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

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Utility methods
    public String getFormattedDepartureTime() {
        return departureTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public String getFormattedArrivalTime() {
        return arrivalTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public boolean hasAvailableSeats() {
        return availableSeats > 0;
    }

    public void bookSeat() {
        if (availableSeats > 0) {
            availableSeats--;
        }
    }

    public void cancelSeat() {
        if (availableSeats < totalSeats) {
            availableSeats++;
        }
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber='" + flightNumber + "'" +
                ", airline='" + airline + "'" +
                ", route='" + source + " -> " + destination + "'" +
                ", departure=" + getFormattedDepartureTime() +
                ", price=" + price +
                ", availableSeats=" + availableSeats +
                "}";
    }
}