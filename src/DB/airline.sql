-- Airline Reservation System Database Setup
-- Author: Rohit
-- Date: 2024

-- Create database
CREATE DATABASE IF NOT EXISTS airline;
USE airline;

-- Drop tables if they exist (for fresh setup)
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS flights;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS airports;

-- Create users table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('customer', 'admin') NOT NULL DEFAULT 'customer',
    phone VARCHAR(20),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create airports table
CREATE TABLE airports (
    airport_code VARCHAR(10) PRIMARY KEY,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50),
    country VARCHAR(50) NOT NULL DEFAULT 'India',
    airport_name VARCHAR(100) NOT NULL
);

-- Create flights table
CREATE TABLE flights (
    flight_id INT PRIMARY KEY AUTO_INCREMENT,
    flight_number VARCHAR(20) NOT NULL UNIQUE,
    airline VARCHAR(50) NOT NULL,
    source VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    total_seats INT NOT NULL DEFAULT 150,
    available_seats INT NOT NULL DEFAULT 150,
    status ENUM('ACTIVE', 'CANCELLED', 'DELAYED', 'COMPLETED') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Constraints
    CHECK (price > 0),
    CHECK (total_seats > 0),
    CHECK (available_seats >= 0),
    CHECK (available_seats <= total_seats),
    CHECK (departure_time < arrival_time)
);

-- Create bookings table
CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    flight_id INT NOT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('CONFIRMED', 'CANCELLED', 'PENDING') NOT NULL DEFAULT 'CONFIRMED',

    -- Foreign keys
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (flight_id) REFERENCES flights(flight_id) ON DELETE CASCADE,

    -- Constraints
    CHECK (total_amount > 0),

    -- Indexes
    INDEX idx_user_booking (user_id),
    INDEX idx_flight_booking (flight_id),
    INDEX idx_booking_date (booking_date)
);

-- Insert default admin user
INSERT INTO users (name, email, password, role, phone, address) VALUES
('Admin User', 'admin@airline.com', 'admin123', 'admin', '9999999999', 'Admin Office, Airline HQ'),
('John Doe', 'john@example.com', 'password123', 'customer', '9876543210', 'Mumbai, Maharashtra'),
('Jane Smith', 'jane@example.com', 'password123', 'customer', '9765432109', 'Delhi, India');

-- Insert sample airports
INSERT INTO airports (airport_code, city, state, country, airport_name) VALUES
('BOM', 'Mumbai', 'Maharashtra', 'India', 'Chhatrapati Shivaji Maharaj International Airport'),
('DEL', 'Delhi', 'Delhi', 'India', 'Indira Gandhi International Airport'),
('BLR', 'Bangalore', 'Karnataka', 'India', 'Kempegowda International Airport'),
('MAA', 'Chennai', 'Tamil Nadu', 'India', 'Chennai International Airport'),
('CCU', 'Kolkata', 'West Bengal', 'India', 'Netaji Subhash Chandra Bose International Airport'),
('HYD', 'Hyderabad', 'Telangana', 'India', 'Rajiv Gandhi International Airport'),
('AMD', 'Ahmedabad', 'Gujarat', 'India', 'Sardar Vallabhbhai Patel International Airport'),
('PNQ', 'Pune', 'Maharashtra', 'India', 'Pune Airport'),
('GOI', 'Goa', 'Goa', 'India', 'Goa Airport'),
('COK', 'Kochi', 'Kerala', 'India', 'Cochin International Airport');

-- Insert sample flights
INSERT INTO flights (flight_number, airline, source, destination, departure_time, arrival_time, price, total_seats, available_seats) VALUES
('AI101', 'Air India', 'Mumbai', 'Delhi', '2025-08-15 06:00:00', '2025-08-15 08:30:00', 5500.00, 180, 175),
('6E202', 'IndiGo', 'Delhi', 'Bangalore', '2025-08-15 09:00:00', '2025-08-15 11:45:00', 4200.00, 186, 180),
('SG303', 'SpiceJet', 'Mumbai', 'Chennai', '2025-08-15 12:00:00', '2025-08-15 14:20:00', 3800.00, 189, 185),
('UK404', 'Vistara', 'Bangalore', 'Hyderabad', '2025-08-15 15:30:00', '2025-08-15 16:45:00', 2500.00, 158, 150),
('AI505', 'Air India', 'Delhi', 'Kolkata', '2025-08-15 18:00:00', '2025-08-15 20:15:00', 4800.00, 180, 176),
('6E606', 'IndiGo', 'Chennai', 'Mumbai', '2025-08-15 21:00:00', '2025-08-15 23:10:00', 4100.00, 186, 182),
('SG707', 'SpiceJet', 'Hyderabad', 'Ahmedabad', '2025-08-16 07:30:00', '2025-08-16 09:00:00', 3200.00, 189, 189),
('UK808', 'Vistara', 'Kolkata', 'Pune', '2025-08-16 10:45:00', '2025-08-16 13:00:00', 4500.00, 158, 155),
('AI909', 'Air India', 'Pune', 'Goa', '2025-08-16 14:15:00', '2025-08-16 15:30:00', 2800.00, 180, 178),
('6E010', 'IndiGo', 'Goa', 'Kochi', '2025-08-16 16:45:00', '2025-08-16 18:30:00', 3500.00, 186, 183),
('SG111', 'SpiceJet', 'Kochi', 'Delhi', '2025-08-16 19:00:00', '2025-08-16 22:30:00', 6200.00, 189, 187),
('UK212', 'Vistara', 'Ahmedabad', 'Bangalore', '2025-08-17 06:30:00', '2025-08-17 08:15:00', 3700.00, 158, 156);

-- Insert sample bookings
INSERT INTO bookings (user_id, flight_id, passenger_name, seat_number, total_amount, status) VALUES
(2, 1, 'John Doe', 'E15', 5500.00, 'CONFIRMED'),
(3, 2, 'Jane Smith', 'B08', 6300.00, 'CONFIRMED'),
(2, 3, 'John Doe', 'E22', 3800.00, 'CONFIRMED');

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_flights_route ON flights(source, destination);
CREATE INDEX idx_flights_departure ON flights(departure_time);
CREATE INDEX idx_bookings_user_flight ON bookings(user_id, flight_id);

-- Create views for reporting
CREATE VIEW flight_booking_summary AS
SELECT 
    f.flight_number,
    f.airline,
    f.source,
    f.destination,
    f.total_seats,
    f.available_seats,
    (f.total_seats - f.available_seats) AS booked_seats,
    ROUND(((f.total_seats - f.available_seats) / f.total_seats) * 100, 2) AS occupancy_percentage
FROM flights f
WHERE f.status = 'ACTIVE';

CREATE VIEW user_booking_summary AS
SELECT 
    u.name,
    u.email,
    COUNT(b.booking_id) AS total_bookings,
    SUM(CASE WHEN b.status = 'CONFIRMED' THEN b.total_amount ELSE 0 END) AS total_spent,
    COUNT(CASE WHEN b.status = 'CANCELLED' THEN 1 END) AS cancelled_bookings
FROM users u
LEFT JOIN bookings b ON u.user_id = b.user_id
GROUP BY u.user_id, u.name, u.email;

-- Create stored procedures
DELIMITER //

CREATE PROCEDURE GetFlightsByRoute(
    IN p_source VARCHAR(50),
    IN p_destination VARCHAR(50)
)
BEGIN
    SELECT * FROM flights 
    WHERE source = p_source 
    AND destination = p_destination 
    AND status = 'ACTIVE' 
    AND available_seats > 0
    ORDER BY departure_time;
END //

CREATE PROCEDURE BookFlight(
    IN p_user_id INT,
    IN p_flight_id INT,
    IN p_passenger_name VARCHAR(100),
    IN p_seat_number VARCHAR(10),
    IN p_total_amount DECIMAL(10,2)
)
BEGIN
    DECLARE v_available_seats INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    -- Check available seats
    SELECT available_seats INTO v_available_seats
    FROM flights WHERE flight_id = p_flight_id FOR UPDATE;

    IF v_available_seats <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No seats available';
    END IF;

    -- Insert booking
    INSERT INTO bookings (user_id, flight_id, passenger_name, seat_number, total_amount)
    VALUES (p_user_id, p_flight_id, p_passenger_name, p_seat_number, p_total_amount);

    -- Update available seats
    UPDATE flights 
    SET available_seats = available_seats - 1 
    WHERE flight_id = p_flight_id;

    COMMIT;
END //

DELIMITER ;

-- Create triggers
DELIMITER //

CREATE TRIGGER before_booking_insert
BEFORE INSERT ON bookings
FOR EACH ROW
BEGIN
    DECLARE v_available_seats INT;

    SELECT available_seats INTO v_available_seats
    FROM flights WHERE flight_id = NEW.flight_id;

    IF v_available_seats <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No seats available for this flight';
    END IF;
END //

CREATE TRIGGER after_booking_cancel
AFTER UPDATE ON bookings
FOR EACH ROW
BEGIN
    IF OLD.status = 'CONFIRMED' AND NEW.status = 'CANCELLED' THEN
        UPDATE flights 
        SET available_seats = available_seats + 1 
        WHERE flight_id = NEW.flight_id;
    END IF;
END //

DELIMITER ;

-- Sample queries for testing

-- 1. Get all active flights
SELECT * FROM flights WHERE status = 'ACTIVE' ORDER BY departure_time;

-- 2. Get flights by route
CALL GetFlightsByRoute('Mumbai', 'Delhi');

-- 3. Get user bookings
SELECT u.name, f.flight_number, b.passenger_name, b.seat_number, b.total_amount, b.status
FROM bookings b
JOIN users u ON b.user_id = u.user_id
JOIN flights f ON b.flight_id = f.flight_id
ORDER BY b.booking_date DESC;

-- 4. Get flight occupancy report
SELECT * FROM flight_booking_summary;

-- 5. Get user summary report  
SELECT * FROM user_booking_summary;

-- Grant permissions (adjust as needed)
-- GRANT ALL PRIVILEGES ON airline_reservation_db.* TO 'root'@'localhost';
-- FLUSH PRIVILEGES;

-- Display success message
SELECT 'Database setup completed successfully!' AS message;