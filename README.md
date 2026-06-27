Personal Budget Tracker Web App
Project Overview: The Personal Budget Tracker is a full-stack web application developed to help individuals and small businesses efficiently manage their daily income and expenses. The application provides an easy-to-use interface for tracking financial transactions and visualizing financial data through charts.
Features: 
User Registration and Login Authentication
Secure Password Encryption
Forgot Password functionality using Email OTP verification
Add, Edit, and Delete Income/Expense Transactions
Categorize transactions (Salary, Food, Shopping, etc.)
Dashboard displaying:
Total Income
Total Expenses
Current Balance
Total Transactions
Interactive Pie Chart for Income vs Expense analysis
Search and Filter transactions by category
User Profile Management
Change Password functionality
Export transaction records to PDF
Export transaction records to Excel
Responsive UI for Desktop and Mobile devices
Email notifications during user login
Technologies Used:
Frontend
HTML5
CSS3
Bootstrap 5
JavaScript
Chart.js
Backend:
Java
Spring Boot
Spring MVC
Spring Data JPA
Spring Security
Database:
MySQL
Steps to Run the Project:
1. Clone the repository: git clone https://github.com/2200090041/azentrix-fullstack-task1.git
2. Open the project in Eclipse or Spring Tool Suite.
3. Create a MySQL database:
CREATE DATABASE budget_tracker;
4. Update the database configuration in application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/budget_tracker
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
5. Configure email credentials for OTP functionality:
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
6. Run the Spring Boot application.
Open your browser and navigate to:
http://localhost:8081
