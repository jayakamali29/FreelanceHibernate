📌 Freelance Project Management System

A Java Console-Based Application built using Hibernate ORM and Oracle Database to manage freelance projects, users, and bidding operations.

🚀 Features

👤 Register Users (Client / Freelancer)

📁 Post Projects

💰 Place Bids

🏆 Award Projects

✅ Mark Projects as Completed

📋 View Users

📊 View Projects

🔄 Hibernate ORM Integration

🛠️ Tech Stack

Java

Hibernate ORM

Oracle Database

Maven

JDBC

HikariCP Connection Pool

🗂️ Project Structure
com.kce.app        → Main class
com.kce.service    → Business logic
com.kce.DAO        → Database operations
com.kce.entity     → Hibernate entities

⚙️ Database Configuration

Update your hibernate.cfg.xml:

<property name="hibernate.connection.url">jdbc:oracle:thin:@localhost:1521:xe</property>
<property name="hibernate.connection.username">YOUR_USERNAME</property>
<property name="hibernate.connection.password">YOUR_PASSWORD</property>

▶️ How to Run

Clone the repository

Configure Oracle DB

Update Hibernate configuration

Run FreelanceMain.java

📌 Constraints

User roles must be:

CLIENT
FREELANCER

📷 Sample Menu
1. Register User
2. Post Project
3. Place Bid
4. Award Project
5. Mark Project Completed
6. View Users
7. View Projects
8. Exit

📄 License

This project is developed for academic and learning purposes.

OUTPUT:


<img width="582" height="462" alt="Screenshot 2026-02-19 211712" src="https://github.com/user-attachments/assets/8950a7d9-f2f1-45bd-9059-393534494e8d" />

