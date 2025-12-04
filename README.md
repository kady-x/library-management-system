# Library Management System

[![Java 17](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/)
[![Spring Boot 4](https://img.shields.io/badge/Spring_Boot-4.0-brightgreen)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Build](https://img.shields.io/badge/Build-Maven-orange)](https://maven.apache.org/)

Educational CSE111 Data Structures project demonstrating a hybrid persistence approach (custom in-memory data structures + JPA persistence) implemented with Spring Boot, Thymeleaf and MySQL.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Custom Data Structures](#custom-data-structures)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Docker Deployment](#docker-deployment)
- [API Documentation](#api-documentation)
- [Frontend Pages](#frontend-pages)
- [Implementation Status](#implementation-status)
- [Future Enhancements](#future-enhancements)
- [Contributing](#contributing)
- [License](#license)
- [Authors](#authors)
- [Acknowledgments](#acknowledgments)

---

## Project Overview

The Library Management System is a learning project for the CSE111 Data Structures course. It combines Spring Boot web application patterns with custom implementations of common data structures (Binary Search Trees, Queues, Linked Lists) to manage books, members, and related library operations. The project demonstrates how custom in-memory structures can be used alongside a relational database (MySQL) to provide efficient queries and persistence.

Key learning objectives:

- Implement and use Binary Search Trees (BST) for efficient lookups.
- Integrate custom data structures with Spring Data JPA for persistence.
- Design a layered MVC application with services and repositories.

---

## Features

- **Book Management:** Add, update, delete, search books. Books are stored in an in-memory `BookBST` for fast lookups and persisted to MySQL via JPA.
- **Member Management:** Register, update, and list members using `MemberBST`.
- **Billing & Fines:** Basic billing endpoints and fine calculation (partially implemented).
- **Dashboard & UI:** Thymeleaf templates provide a web UI for day-to-day operations.
- **Planned:** Borrowing workflow, waiting list queue, advanced reporting, and improved billing history.

---

## Technology Stack

- Backend: Java 17, Spring Boot 4.0, Spring MVC, Spring Data JPA
- Frontend: Thymeleaf templates, HTML5, CSS3, JavaScript
- Database: MySQL 8.0 (development); HSQLDB may be used for tests
- Build: Maven (wrapper `mvnw` included)
- Containerization: Docker

---

## Architecture

The application follows an MVC pattern:

- Controller → Service → Repository → Data Structures / JPA

This project uses a hybrid persistence strategy:

- MySQL (via Spring Data JPA) is the source of truth for persisted data.
- Custom in-memory data structures (BST) are populated at startup from the DB and kept synchronized on writes.

This approach allows fast in-memory reads (O(log n) average for BST) while persisting data for durability.

---

## Custom Data Structures

- **BookBST** (`src/main/java/com/aiu/library/datastructures/BookBST.java`)
	- Binary Search Tree keyed by `bookID`.
	- Operations: insert, delete, searchByID, searchByTitle, in-order traversal, findMaxId.

- **MemberBST** (`src/main/java/com/aiu/library/datastructures/MemberBST.java`)
	- Binary Search Tree keyed by `memberId` for member operations.
 
 - **BorrowRecordList** (planned stub)
	 - Path: `src/main/java/com/aiu/library/datastructures/BorrowRecordList.java`
	 - Intended type: Doubly-linked list for borrow history records
	 - Core operations: `append(record)`, `remove(record)`, `iterateForward()`, `iterateBackward()`

 - **WaitingQueue** (planned stub)
	 - Path: `src/main/java/com/aiu/library/datastructures/WaitingQueue.java`
	 - Intended type: FIFO queue for reservations
	 - Core operations: `enqueue(memberOrRecord)`, `dequeue()`, `peek()`, `size()`

 - **BillingList** (planned stub)
	 - Path: `src/main/java/com/aiu/library/datastructures/BillingList.java`
	 - Intended type: Singly or doubly linked list to maintain payment history per member
	 - Core operations: `addPayment(payment)`, `getPayments(memberId)`, `remove(paymentId)`

---

## Project Structure

High-level layout:

```
.
├── Dockerfile
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.aiu.library
│   │   │       ├── controller
│   │   │       ├── service
│   │   │       ├── repository
│   │   │       ├── model
│   │   │       └── datastructures
│   │   └── resources
│   │       ├── static
│   │       └── templates
└── README.md
```

Explanation of key folders:

- `controller/` - Web controllers (page routing and REST endpoints).
- `service/` - Business logic that coordinates repository operations.
- `repository/` - Data access layer; includes both JPA repositories and wrapper repositories that maintain BST state.
- `model/` - JPA entities (`Book`, `Member`, etc.).
- `datastructures/` - Custom data structure implementations used by the repository layer.

---

## Database Schema

Main tables (descriptions):

- `books` — Book information
	- `bookID` (INT) — primary key
	- `title` (VARCHAR)
	- `author` (VARCHAR)
	- `genre` (VARCHAR)
	- `coverUrl` (VARCHAR)
	- `publicationYear` (INT)
	- `availabilityStatus` (BOOLEAN)

- `members` — Member details
	- `memberId` (INT) — primary key
	- `name` (VARCHAR)
	- `contactInfo` (VARCHAR)
	- `membershipDate` (DATE)

- `borrow_records` — Borrowing transactions (planned)
- `billings` — Billing and payment records (planned)
- `waiting_list` — Reservation queue (planned)

Note: If you switch ID management strategies, ensure the DB column definitions (AUTO_INCREMENT) align with application behavior.

---

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ (or Docker)
- An IDE (IntelliJ IDEA, Eclipse, or VS Code)

---

## Installation & Setup

1. Clone the repository:

```bash
git clone https://github.com/kady-x/library-management-system.git
cd library
```

2. Configure MySQL and create database:

```sql
CREATE DATABASE LMS;
```

Update `src/main/resources/application.properties` with your DB credentials:

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/LMS?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Build the project:

```bash
./mvnw clean install
```

4. Run the application:

```bash
./mvnw spring-boot:run
```
Or run `LibraryApplication.java` from your IDE.

5. Access the application in your browser:

```
http://localhost:8080
```

---

## Docker Deployment

Build the Docker image:

```bash
docker build -t library-app .
```

Run the container:

```bash
docker run -p 8080:8080 --name library-app library-app:latest
```

For a multi-container setup (application + MySQL) use `docker-compose` and point `spring.datasource.url` to the MySQL service name.

---

## API Documentation

### Books API

| Method | Endpoint | Description |
|---|---:|---|
| GET | `/api/books` | Get all books |
| GET | `/api/books/{id}` | Get book by ID |
| POST | `/api/books` | Add new book (JSON body) |
| PUT | `/api/books/{id}` | Update book (JSON body) |
| DELETE | `/api/books/{id}` | Delete book |

Example add book JSON:

```json
{
	"bookID": null,
	"title": "Example Book",
	"author": "Author Name",
	"genre": "Fiction",
	"coverUrl": "",
	"publicationYear": 2020,
	"availabilityStatus": true
}
```

### Members API (examples)

| Method | Endpoint | Description |
|---|---:|---|
| GET | `/members/all` | Get all members |
| GET | `/members/{id}` | Get member by ID |
| POST | `/members/add` | Register new member |
| PUT | `/members/edit/{id}` | Update member |

### Billing API

| Method | Endpoint | Description |
|---|---:|---|
| GET | `/api/billing/{memberID}?dueDate={yyyy-MM-dd}` | Calculate fine for a member given a due date (returns fine amount as double) |
| POST | `/api/billing/{memberID}?amount={amount}` | Add a payment for a member (amount as query param); returns updated payment status |
| GET | `/api/billing/status/{memberID}` | Get current payment/fine status for a member |

Notes:
- `GET /api/billing/{memberID}` expects a `dueDate` request parameter in `yyyy-MM-dd` format, e.g. `/api/billing/123?dueDate=2025-12-10`.
- `POST /api/billing/{memberID}` expects an `amount` request parameter, e.g. `/api/billing/123?amount=10.5`.

---

## Frontend Pages

- `/` or `/dashboard` — Dashboard overview
- `/books` or `/books/list` — Book listing
- `/books/add` — Add new book form
- `/books/edit/{id}` — Edit book form
- `/borrow` — Borrow/return books interface (partial)

---

## Implementation Status

| Area | Status |
|---|---:|
| Book management (CRUD) | ✅ Completed |
| Member management | ✅ Completed |
| Billing system | ⚠️ Partially Implemented |
| Borrow workflow | ❌ Not Implemented |
| Waiting list | ❌ Not Implemented |
| Reports | ❌ Not Implemented |

---

## Future Enhancements

- Implement `BorrowRecordList` as a doubly-linked list for history tracking
- Implement `WaitingQueue` for reservations
- Add Spring Security for authentication and roles
- Add unit and integration tests
- Improve Docker and CI/CD configuration

---

## Contributing

This repository is an educational project. Contributions and suggestions are welcome - fork the repository and open a pull request.

---

## License

This project is licensed under the Apache License 2.0 — see the `LICENSE` file for details.

---

## Authors

- [Mohamed ElKady](https://github.com/kady-x)
- [Adham Hatem](https://github.com/Adham-Hatem)
- [Basmala Samir](https://github.com/basmalasamir)
- [Shahd Alla](https://github.com/shahd-ali4)
- [Waffa Nabeh](https://github.com/wafaaalkaddaf-afk)

---

## Acknowledgments

- Spring Boot
- Thymeleaf
- MySQL
- Open-source libraries used in the project

