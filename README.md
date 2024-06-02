# Authorization and Authentication Microservice

This microservice, developed in Java using Spring Boot, provides robust authorization and authentication functionalities. It utilizes Maven for dependency management, MongoDB for data storage, Kafka for messaging, and Docker for containerization. The microservice includes features such as registration, email verification, login, password changing, and password recovery flows. The codebase follows clean architecture principles, separating concerns into controllers, services, repositories, and models. Exception handling ensures reliability and resilience.

## Features

- Registration: Allows users to create new accounts.
- Email Verification: Verifies user email addresses to ensure account validity.
- Login: Authenticates users into the system securely.
- Password Changing: Provides functionality for users to update their passwords.
- Password Forgotten: Allows users to recover forgotten passwords securely.

## Technologies Used

- Java
- Spring Boot
- Maven
- MongoDB
- Kafka
- Docker

## Usage

1. Register a new account.
2. Verify your email address.
3. Login with your credentials.
4. Change your password if needed.
5. If you forget your password, use the password recovery flow.

## Architecture

The microservice follows a clean architecture pattern, separating concerns into layers:

- Controller: Handles incoming HTTP requests and delegates to the appropriate service.
- Service: Implements business logic and orchestrates interactions between different components.
- Repository: Handles database interactions.
- Model: Defines data structures used throughout the application.

