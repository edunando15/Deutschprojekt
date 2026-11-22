# Deutschprojekt

This application is designed to help users learn German through interactive exercises and quizzes. It provides a simple UI to practice vocabulary. The application may be extended in the future to include more features, such as grammar exercises and progress tracking.

## Main Functionality

The main feature of the application consists of a quiz with multiple questions, where the user has to select the correct article, German name and German plural name of a given English word.

## Architecture

This project is a Java-based application, which uses Spring Boot and PostgreSQL. The application follow the MCV design pattern, where the model represents the data, the view represents the user interface, and the controller handles the logic and user input.

### Set Environment Variables and Run Tests (PowerShell)

Execute the following commands in PowerShell to configure your database connection and run the test suite:

```powershell
\$env:DB_URL = "jdbc:postgresql://localhost:5432/postgres?connectTimeout=10&sslmode=prefer"
\$env:DB_USERNAME = "postgres"
\$env:DB_PASSWORD = "<your password>"

.\mvnw.cmd test
```

