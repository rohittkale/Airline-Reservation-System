# ✈️ Airline Reservation System – Complete Java Desktop Application

A comprehensive **Java Swing-based Airline Reservation System** with MySQL database backend that demonstrates enterprise-level application development with GUI design, database integration, and user management. Features role-based authentication, flight management, booking system, and administrative reporting.

***

## 🚀 Features

### 👨💼 Admin Features
- **Flight Management**: Add, update, delete, and view flights with real-time data
- **Booking Overview**: Monitor all customer bookings with detailed information
- **User Management**: View registered customers and their booking history
- **Advanced Reports**: Generate revenue, occupancy, popular routes, and customer analytics
- **Dashboard**: Comprehensive admin control panel with tabbed interface
- **Data Analytics**: Real-time statistics and business intelligence

### 👤 Customer Features
- **User Registration**: Easy account creation with comprehensive validation
- **Flight Search**: Advanced search by route, date, airline, and availability
- **Flight Booking**: Book tickets with seat selection and class options
- **Booking History**: View and manage personal bookings with status tracking
- **Profile Management**: Update personal information and preferences
- **Booking Operations**: Cancel confirmed bookings with automatic seat release
- **Travel Statistics**: Personal dashboard with travel insights

### 🔧 Technical Features
- **Secure Authentication**: Role-based access control (Admin/Customer)
- **Database Integration**: Full CRUD operations with MySQL using JDBC
- **Professional GUI**: Modern Swing-based interface with responsive layouts
- **Data Validation**: Comprehensive input validation and error handling
- **Error Management**: Robust exception handling and user feedback
- **Session Management**: Secure user state tracking and management

***

## 🛠️ Tech Stack

| Component | Technology | Implementation |
|-----------|------------|----------------|
| **Frontend** | Java Swing, AWT | Professional GUI with responsive design |
| **Backend** | Core Java 11+, JDBC | Object-oriented architecture with MVC pattern |
| **Database** | MySQL 8.0+ | Relational database with constraints and triggers |
| **Architecture** | Model-View-Controller | Clean separation of concerns |
| **Design Patterns** | Singleton, Observer | Industry-standard implementation |
| **Security** | PreparedStatement, Input Validation | SQL injection prevention |

***

## 📁 Project Structure

```
AirlineReservationSystem/
├── src/
│   ├── Main.java                    # Application entry point
│   ├── gui/                         # GUI components (Swing)
│   │   ├── Login.java              # User authentication interface
│   │   ├── Register.java           # User registration form
│   │   ├── AdminDashboard.java     # Admin management panel
│   │   ├── CustomerDashboard.java  # Customer interface
│   │   └── BookingForm.java        # Flight booking system
│   ├── model/                      # Data models (OOP)
│   │   ├── User.java              # User entity with validation
│   │   ├── Flight.java            # Flight entity with operations
│   │   └── Booking.java           # Booking entity with status management
│   └── utils/                      # Utility classes
│       └── DBConnection.java       # JDBC connection manager (Singleton)
├── db/
│   └── airline_db.sql             # Complete database setup with sample data
├── resources/
│   └── assets/                    # Images, icons, and resources
├── lib/                           # External JAR dependencies
│   └── mysql-connector-java.jar   # MySQL JDBC driver
└── README.md                      # Project documentation
```

***

## 🚀 Getting Started

### Prerequisites
- **Java Development Kit (JDK)** 11 or higher
- **MySQL Server** 8.0 or higher
- **IDE**: Eclipse, IntelliJ IDEA, or NetBeans
- **MySQL JDBC Driver** (mysql-connector-java.jar)

### Installation

1. **Clone or Download the Project**
   ```bash
   git clone https://github.com/yourusername/airline-reservation-system.git
   cd airline-reservation-system
   ```

2. **Database Setup**
   ```sql
   -- Start MySQL server
   mysql -u root -p
   
   -- Execute database script
   source /path/to/AirlineReservationSystem/DB/airline.sql
   ```

3. **Configure Database Connection**
   Edit `src/utils/DBConnection.java`:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/airline";
   private static final String USERNAME = "root";
   private static final String PASSWORD = "your_password";
   ```

4. **Add Dependencies**
   - Download MySQL JDBC Driver
   - Add `mysql-connector-java.jar` to project classpath

5. **Compile and Run**
   ```bash
   # Using IDE: Run Main.java
   # Or using command line:
   javac -cp ".:lib/mysql-connector-java.jar" src/**/*.java
   java -cp ".:lib/mysql-connector-java.jar" Main
   ```

***

## 📸 Screenshots

### Login Interface
Professional authentication system with role-based access control.

### Admin Dashboard
Comprehensive flight management with tabbed interface for easy navigation.

### Customer Dashboard
User-friendly interface with booking history and travel statistics.

### Booking System
Advanced flight search and reservation system with real-time availability.

***

## 🔐 Default Login Credentials for Admin

### Admin Access
- **Email**: `admin@airline.com`
- **Password**: `admin123`
- **Role**: Administrator
- 
*Or create a new account using the registration form*

***

## 🗃️ Database Schema

### Core Tables

#### Users Table
```sql
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('customer', 'admin') DEFAULT 'customer',
    phone VARCHAR(20),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Flights Table
```sql
CREATE TABLE flights (
    flight_id INT PRIMARY KEY AUTO_INCREMENT,
    flight_number VARCHAR(20) NOT NULL UNIQUE,
    airline VARCHAR(50) NOT NULL,
    source VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    total_seats INT DEFAULT 150,
    available_seats INT DEFAULT 150,
    status ENUM('ACTIVE', 'CANCELLED', 'DELAYED') DEFAULT 'ACTIVE'
);
```

#### Bookings Table
```sql
CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    flight_id INT NOT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('CONFIRMED', 'CANCELLED', 'PENDING') DEFAULT 'CONFIRMED',
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (flight_id) REFERENCES flights(flight_id)
);
```

***

## 🎯 Usage Guide

### For Customers
1. **Register/Login**: Create account or login with existing credentials
2. **Search Flights**: Enter source, destination, and travel preferences
3. **Select Flight**: Choose from available flights with real-time pricing
4. **Book Ticket**: Enter passenger details and confirm reservation
5. **Manage Bookings**: View history, download tickets, and cancel if needed

### For Administrators
1. **Login**: Use admin credentials to access dashboard
2. **Manage Flights**: Add, edit, delete flight schedules and pricing
3. **View Bookings**: Monitor all customer reservations and statuses
4. **Generate Reports**: Access revenue, occupancy, and business analytics
5. **User Management**: View registered users and their activity

***

## 📊 Advanced Features

### Database Features
- **Stored Procedures**: Complex booking operations with transaction management
- **Database Views**: Pre-defined queries for reporting and analytics
- **Triggers**: Automatic seat management and data consistency
- **Constraints**: Business rule enforcement and data integrity
- **Indexing**: Optimized query performance for large datasets

### Application Features
- **Input Validation**: Client-side and server-side validation
- **Exception Handling**: Comprehensive error management with user feedback
- **Session Management**: Secure user state tracking
- **Professional UI**: Consistent design with modern Swing components
- **Responsive Design**: Adaptive layouts for different screen sizes

***

## 🌟 Key Learning Outcomes

This project demonstrates proficiency in:
- **Java Swing GUI Development** with professional design patterns
- **Database Design & MySQL Integration** with complex relationships
- **JDBC Connection Management** with prepared statements
- **Object-Oriented Programming** principles and design patterns
- **MVC Architecture** implementation with clean code structure
- **Event-Driven Programming** with user interaction handling
- **Exception Handling** and comprehensive error management
- **SQL Query Development** with joins, aggregations, and reporting

***

## 🚀 Future Enhancements

### Technical Improvements
- [ ] RESTful API development for web/mobile integration
- [ ] Spring Boot framework migration for enterprise features
- [ ] Advanced security with JWT authentication
- [ ] Multi-language support and internationalization
- [ ] Advanced reporting with charts and visualizations

### Feature Additions
- [ ] Email notifications for booking confirmations
- [ ] Payment gateway integration
- [ ] Seat selection visualization
- [ ] Real-time flight status updates
- [ ] Mobile application development
- [ ] PDF ticket generation and printing

### Business Features
- [ ] Loyalty program and rewards system
- [ ] Dynamic pricing and discount management
- [ ] Group booking functionality
- [ ] Waitlist management system
- [ ] Multi-city and round-trip bookings

***

## 🐛 Troubleshooting

### Common Issues

**Database Connection Failed**
```
Solution: 
- Verify MySQL server is running
- Check connection credentials in DBConnection.java
- Ensure database exists and is accessible
- Confirm JDBC driver is in classpath
```

**Class Not Found Exception**
```
Solution:
- Add MySQL JDBC driver to project classpath
- Verify jar file version compatibility
- Check import statements in Java files
```

**GUI Not Displaying**
```
Solution:
- Ensure proper Swing EDT usage
- Check main method implementation
- Verify component initialization order
```

***

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

***

## 📄 License

This project is created for educational purposes. Feel free to use, modify, and distribute for learning and development.

***

## 🙋♂️ Author

**Name**: Rohit Pran Kale  
**Email**: [kalerohit1912@gmail.com](mailto:kalerohit1912@gmail.com)  
**LinkedIn**: [linkedin.com/in/rohitkale](https://linkedin.com/in/rohitkale)  
**GitHub**: [github.com/rohitkale](https://github.com/rohitkale)

***

⭐ **Star this repository if you found it helpful for learning Java desktop application development!**

***

**Happy Coding! ✈️**

*Built with ❤️ using Java, Swing, and MySQL*
