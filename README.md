# Money Transaction Management System

A Spring Boot REST API for managing scheduled financial transactions between accounts with automatic fee calculation.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Fee Calculation Logic](#fee-calculation-logic)
- [Testing](#testing)
- [Contributing](#contributing)

## 🎯 Overview

The Money Transaction Management System is a RESTful API that enables users to create, update, retrieve, and delete scheduled money transfers between accounts. The system automatically calculates transaction fees based on the transfer amount and the number of days until the scheduled execution date.

## ✨ Features

- **CRUD Operations**: Complete create, read, update, and delete operations for transactions
- **Automatic Fee Calculation**: Fees calculated based on amount and schedule date
- **RESTful API**: Well-designed REST endpoints following best practices
- **Data Validation**: Input validation using Jakarta Bean Validation
- **H2 Database**: In-memory database for development and testing
- **Comprehensive Documentation**: Javadoc comments on all classes and methods
- **Unit Tests**: Test coverage for all layers (controller, service, repository)
- **Bruno API Collection**: Ready-to-use API collection for testing

## 🛠️ Technology Stack

- **Java 17** - Programming language
- **Spring Boot 3.x** - Application framework
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database
- **Maven** - Build and dependency management
- **Lombok** - Boilerplate code reduction
- **Jakarta Validation** - Input validation
- **JUnit 5** - Testing framework
- **Bruno** - API testing

## 📁 Project Structure

```
money/
├── src/
│   ├── main/
│   │   ├── java/com/bank/money/
│   │   │   ├── MoneyApplication.java          # Main application entry point
│   │   │   ├── controller/
│   │   │   │   └── TransactionController.java # REST API endpoints
│   │   │   ├── domain/
│   │   │   │   └── Transaction.java           # JPA entity
│   │   │   ├── dto/
│   │   │   │   └── TransactionRequestDTO.java # Data transfer object
│   │   │   ├── exception/
│   │   │   │   └── BusinessException.java     # Custom exception
│   │   │   ├── repository/
│   │   │   │   └── TransactionRepository.java # Data access layer
│   │   │   └── service/
│   │   │       └── TransactionService.java    # Business logic
│   │   └── resources/
│   │       └── application.properties         # Application configuration
│   └── test/
│       └── java/com/bank/money/              # Unit tests
├── Bruno/Money/                               # Bruno API collection
├── API_DOCUMENTATION.md                       # Detailed API documentation
├── pom.xml                                    # Maven configuration
└── README.md                                  # This file
```

### 📂 Package Descriptions

#### 1️⃣ **controller**

**What:** Contains classes that handle incoming HTTP requests and return HTTP responses.

**Why:**
- Acts as the entry point of your application's API
- Responsible for mapping URLs to service calls
- Keeps web-related code separate from business logic

**Example:** `TransactionController` defines endpoints like `/api/transactions` and handles CRUD operations.

#### 2️⃣ **domain**

**What:** Contains your core business entities/models mapped to the database.

**Why:**
- Represents the data structure and business concepts (e.g., Transaction)
- Annotated with JPA annotations to define persistence
- Keeps database mapping separate from API input/output and business logic

**Example:** The `Transaction` class holds transaction details and maps to the `transactions` table.

#### 3️⃣ **dto (Data Transfer Object)**

**What:** Contains simple classes to carry data between client and server.

**Why:**
- Decouples internal domain model from external API models
- Allows you to control what data is exposed or accepted in API requests/responses
- Helps with input validation via annotations

**Example:** `TransactionRequestDTO` holds only the fields required to create or update a transaction.

#### 4️⃣ **exception**

**What:** Contains custom exceptions to handle specific business or system errors.

**Why:**
- Enables clear error reporting and centralized error handling (e.g., via @ControllerAdvice)
- Distinguishes between different error types (validation, business rule violation, etc.)

**Example:** `BusinessException` indicates a domain-specific error like "Transaction not found".

#### 5️⃣ **repository**

**What:** Contains interfaces for data access, typically extending Spring Data JPA repositories.

**Why:**
- Abstracts the database operations and queries
- Enables easy CRUD without writing boilerplate SQL/JPA code
- Promotes separation of concerns by isolating persistence logic

**Example:** `TransactionRepository` extends `JpaRepository<Transaction, Long>` to provide standard DB operations.

#### 6️⃣ **service**

**What:** Contains the business logic layer—the core rules and operations of your application.

**Why:**
- Encapsulates use cases and workflows (e.g., fee calculation, validation)
- Keeps business logic independent of web and persistence concerns
- Facilitates easier testing and maintenance

**Example:** `TransactionService` handles creating transactions, calculating fees, and validating business rules.

### 💡 Why This Structure?

- **Separation of Concerns**: Each layer has a clear responsibility, avoiding tangled code
- **Maintainability**: Easier to read, update, and debug
- **Scalability**: New features or changes can be made in one layer without affecting others
- **Testability**: Business logic in services can be unit tested independently from controllers or repositories
- **Reusability**: DTOs separate from domain allow different views or API versions without changing core models

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd money
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

Or run the JAR directly:
```bash
java -jar target/money-0.0.1-SNAPSHOT.jar
```

4. The application will start on `http://localhost:8080`

### H2 Database Console

Access the H2 console at: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave blank)

## 📖 API Documentation

For detailed API documentation including endpoints, request/response examples, and error codes, see [API_DOCUMENTATION.md](API_DOCUMENTATION.md).

### Quick API Reference

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/transactions` | Get all transactions |
| GET | `/api/transactions/{id}` | Get transaction by ID |
| POST | `/api/transactions` | Create new transaction |
| PUT | `/api/transactions/{id}` | Update transaction |
| DELETE | `/api/transactions/{id}` | Delete transaction |

## 💰 Fee Calculation Logic

Transaction fees are calculated based on two factors:
1. The transfer amount
2. The number of days between creation and scheduled execution

### Fee Rules Table

| Amount Range | Days Until Scheduled | Fee Rate |
|--------------|---------------------|----------|
| ≤ $1,000 | 0 (same day) | 3% + $3 |
| ≤ $2,000 | 1-10 days | 9% |
| > $2,000 | 11-20 days | 8.2% |
| > $2,000 | 21-30 days | 6.9% |
| > $2,000 | 31-40 days | 4.7% |
| > $2,000 | 40+ days | 1.7% |
| All others | - | $0 |

### Examples

**Example 1: Same-day small transfer**
- Amount: $500
- Days: 0
- Fee: $500 × 0.03 + $3 = **$18.00**

**Example 2: Medium amount, short term**
- Amount: $1,500
- Days: 5
- Fee: $1,500 × 0.09 = **$135.00**

**Example 3: Large amount, long term**
- Amount: $5,000
- Days: 45
- Fee: $5,000 × 0.017 = **$85.00**

## 🧪 Testing

### Run All Tests

```bash
mvn test
```

### Test Coverage

The project includes unit tests for all layers:
- **Controller Tests**: Test REST endpoints and HTTP responses
- **Service Tests**: Test business logic and fee calculations
- **Repository Tests**: Test data access operations
- **Integration Tests**: Test application context

### Using Bruno API Collection

1. Install [Bruno](https://www.usebruno.com/)
2. Open the `Bruno/Money/` directory in Bruno
3. Use the pre-configured requests to test the API

Available requests:
- Create Transaction
- Get All Transactions
- Get by Id
- Update a scheduled transaction
- Delete a scheduled transaction

## 📝 Javadoc

All classes and methods include comprehensive Javadoc comments. Generate the Javadoc HTML:

```bash
mvn javadoc:javadoc
```

View the generated documentation at: `target/site/apidocs/index.html`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

