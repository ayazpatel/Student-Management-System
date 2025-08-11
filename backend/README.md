# Student Management System

This project is a robust and secure Student Management System developed using Java and the Spring Boot framework. It provides a comprehensive set of features for managing student data, including registration, updates, and viewing individual or all student records. The system incorporates modern technologies such as Spring Security for authentication and authorization, JWT for secure API communication, and MinIO for object storage, making it a scalable and reliable solution for educational institutions.

## Technologies Used

* Java 17
* Spring Boot
* Spring Security
* Spring Data JPA
* PostgreSQL
* Maven
* JWT
* MinIO

## Setup and Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Ayaz-29/Student-Management-System.git
   ```
2. **Navigate to the project directory:**
   ```bash
   cd Student-Management-System/backend
   ```
3. **Install dependencies:**
   ```bash
   mvn install
   ```
4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Authentication

*   `POST /auth/register` - Register a new user.
*   `POST /auth/login` - Login an existing user.

### Student

*   `POST /student/register` - Register a new student.
*   `PUT /student/update/{id}` - Update an existing student.
*   `GET /student/view/{id}` - View a student by id.
*   `GET /student/view-all` - View all students.
*   `DELETE /student/delete/{id}` - Delete a student by id.
