# 💰 Bill Payment Reminder

A full-stack Spring Boot web application that helps users track recurring bills, receive payment reminders, and manage bill payments with a modern, responsive interface.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![MySQL](https://img.shields.io/badge/MySQL-9.4-blue)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5-purple)

## 🚀 Live Demo

**Repository:** [https://github.com/ombabu-18u/BPR](https://github.com/ombabu-18u/BPR)

## ✨ Features

### 🔐 Authentication & Security
- User registration and login with Spring Security
- Password encryption with BCrypt
- Session-based authentication
- Secure role-based access control

### 💳 Bill Management
- **Create Bills:** Add new bills with name, amount, due date, and category
- **View Bills:** See all bills in an organized table view
- **Mark as Paid:** Update bill payment status with one click
- **Delete Bills:** Remove bills when no longer needed
- **Bill Categories:** Organize bills by categories (Utilities, Rent, Insurance, etc.)

### 📊 Dashboard & Analytics
- Visual statistics cards showing pending bills count
- Total pending amount calculation
- Upcoming bills overview (next 7 days)
- Recent activity tracking

### 🔔 Smart Reminders
- **Automated Scheduling:** Daily checks for upcoming bills (8:00 AM)
- **Overdue Alerts:** Hourly monitoring for overdue payments
- **Smart Notifications:** Console-based reminder system (easily extendable to email/SMS)
- **Manual Testing:** API endpoints to trigger reminders on demand

### 👤 User Management
- User profile management
- Update personal information
- Secure password handling
- Registration date tracking

## 🛠️ Technology Stack

### Backend
- **Framework:** Spring Boot 3.5.6
- **Security:** Spring Security 6.5.5
- **Persistence:** Spring Data JPA with Hibernate
- **Validation:** Bean Validation API
- **Scheduling:** Spring Scheduler
- **Build Tool:** Maven

### Frontend
- **Templates:** Thymeleaf 3.1.3
- **Styling:** Bootstrap 5.1.3
- **Icons:** Bootstrap Icons
- **JavaScript:** Vanilla JS with Fetch API

### Database
- **Database:** MySQL 9.4
- **ORM:** Hibernate 6.4
- **Connection:** MySQL Connector/J

### Development
- **Language:** Java 21
- **IDE:** VS Code
- **Platform:** macOS (M2 Chip)

## 📋 Prerequisites

Before running this application, ensure you have:

- Java 21 or later
- Maven 3.9.x or later
- MySQL 9.4 or later
- Git for version control

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/ombabu-18u/BPR.git
cd BPR
```

### 2. Database Setup

```sql
CREATE DATABASE bill_reminder_db;
```

### 3. Configuration

Update `src/main/resources/application.properties` with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bill_reminder_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. Build and Run

```bash
# Clean and compile
./mvnw clean compile

# Run the application
./mvnw spring-boot:run
```

### 5. Access the Application

Open your browser and navigate to:

```
http://localhost:8080
```

## 🎯 Default Test Account

After first run, you can use the default test account:

- **Email:** test@example.com
- **Password:** password123

Or register a new account through the registration page.

## 📁 Project Structure

```
src/main/java/com/billreminder/billpaymentreminder/
├── entity/              # JPA Entities
│   ├── User.java
│   ├── Bill.java
│   └── BillCategory.java
├── repository/          # Data Access Layer
│   ├── UserRepository.java
│   ├── BillRepository.java
│   └── BillCategoryRepository.java
├── service/            # Business Logic
│   ├── UserService.java
│   ├── BillService.java
│   └── BillReminderService.java
├── controller/         # Web Layer
│   ├── DashboardController.java
│   ├── BillController.java
│   └── UserController.java
├── config/            # Configuration
│   ├── SecurityConfig.java
│   ├── DataLoader.java
│   └── CustomUserDetailsService.java
└── dto/               # Data Transfer Objects
    └── BillRequest.java
```

## 🔌 API Endpoints

### Authentication
- `POST /register` - User registration
- `GET /login` - Login page
- `POST /logout` - User logout

### Bills Management
- `GET /api/bills` - Get user's bills
- `POST /api/bills` - Create new bill
- `PUT /api/bills/{id}/pay` - Mark bill as paid
- `DELETE /api/bills/{id}` - Delete bill

### Categories
- `GET /api/categories` - Get all bill categories

### Reminders
- `POST /api/reminders/test` - Test reminder system
- `POST /api/reminders/check-now` - Manual bill check

## 🎨 UI Pages

- `/` - Dashboard with statistics
- `/login` - User login page
- `/register` - User registration
- `/bills` - Bills management
- `/bills/new` - Add new bill
- `/profile` - User profile

## 🔧 Configuration Options

### Database Settings
```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Server Settings
```properties
server.port=8080
```

### Thymeleaf Settings
```properties
spring.thymeleaf.cache=false
```

## 🐛 Troubleshooting

### Common Issues

**Database Connection Error**
- Verify MySQL is running
- Check database credentials in `application.properties`

**Port Already in Use**
- Change `server.port` in `application.properties`
- Or kill process using port 8080

**Compilation Errors**
- Run `./mvnw clean compile`
- Ensure Java 21 is installed

**Login Issues**
- Use default test account
- Or register new account

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 👨‍💻 Developer

**Ombabu** - [GitHub](https://github.com/ombabu-18u)

## 🎉 Acknowledgments

- Spring Boot team for the excellent framework
- Bootstrap team for the responsive UI components
- MySQL team for the reliable database system

---

⭐ Star this repository if you find it helpful!