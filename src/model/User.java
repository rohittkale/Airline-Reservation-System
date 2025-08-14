package model;

/**
 * User model class for Airline Reservation System
 * Represents user/passenger information
 */
public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private String role; // "admin" or "customer"
    private String phone;
    private String address;

    // Default constructor
    public User() {
    }

    // Constructor with parameters
    public User(int userId, String name, String email, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Constructor for registration
    public User(String name, String email, String password, String phone, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = "customer";
        this.phone = phone;
        this.address = address;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + "'" +
                ", email='" + email + "'" +
                ", role='" + role + "'" +
                "}";
    }
}