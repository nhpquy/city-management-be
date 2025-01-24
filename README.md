# City Management System - Backend

## Overview

The backend of the City Management System is built using Spring Boot. This backend provides a RESTful API for managing city and electricity/water supply data.

## Features

- **RESTful API**: Provides endpoints for CRUD operations on cities, electricity, water supply, ...
- **Integration**: Connects to both MySQL database.
- **Exception Handling**: Custom error handling for not found resources.

## Technologies

- **Spring Boot**: Framework for building production-ready applications with Java.
- **MySQL**: Relational database for structured data storage.

## Setup Instructions

#### Important: Java 11 is required to run this project.

### Install Dependencies

Ensure you have [Maven](https://maven.apache.org/) and [Java JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) installed. Run the following command to install the required dependencies:

```bash
mvn install -DskipTests
```

### Configure the Application

Update `src/main/resources/application.properties` with your database configuration:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/database
spring.datasource.username=user
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

Ensure the databases are set up as expected and the URLs, usernames, and passwords match your local or remote database setup.

### Start Local Infrastructure
```bash
docker-compose up
```

### Start the Backend Server

Run the following command to start the Spring Boot application:

```bash
mvn spring-boot:run
```

The backend server will be available at [http://localhost:8080](http://localhost:8080).

## Swagger API Documentation

The backend API is documented using Swagger, which provides a user-friendly interface for exploring the available endpoints. 
To access the Swagger UI, navigate to [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) after starting the backend server.

[alt text](https://github.com/nhpquy/city-management-be/blob/main/doc/swagger-example.png?raw=true)


## Database Documentation
The backend service to organize data for a city, covering the water supply, electricity, and waste management.

[alt text](https://github.com/nhpquy/city-management-be/blob/main/doc/database-diagram.png?raw=true)

---
