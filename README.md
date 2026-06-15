# 📚 Library Management System — Microservices

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.6-green?style=for-the-badge&logo=springboot)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2024.0.1-green?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Basic-blue?style=for-the-badge&logo=docker)
![Swagger](https://img.shields.io/badge/Swagger-UI-green?style=for-the-badge&logo=swagger)

A production-ready **Library Management System** built with **Spring Boot Microservices Architecture**, featuring service discovery, centralized configuration, API gateway routing, and inter-service communication.

</div>

---

## 📌 Table of Contents

- [Architecture](#-architecture)
- [Services](#-services)
- [Tech Stack](#-tech-stack)
- [Features](#-features)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [API Reference](#-api-reference)
- [Project Structure](#-project-structure)
- [Configuration](#-configuration)

---

## 🏗 Architecture

```
                        ┌─────────────────────┐
                        │   Config Server      │
                        │   (Git-backed)       │
                        │   Port: 8088         │
                        └──────────┬──────────┘
                                   │ centralized config
                                   │
              ┌────────────────────▼───────────────────┐
              │           Service Registry              │
              │           (Netflix Eureka)              │
              │           Port: 8761                    │
              └────────────────────┬───────────────────┘
                                   │ service discovery
                                   │
┌──────────┐          ┌────────────▼───────────┐
│  Client  │─────────▶│      API Gateway        │
│ (Postman/│          │   (Spring Cloud GW)     │
│ Frontend)│◀─────────│      Port: 8080         │
└──────────┘          └──┬──────────┬───────┬──┘
                         │          │       │
              lb://       │          │       │  lb://
           ┌─────────────┘    lb:// │       └──────────────┐
           │                        │                       │
┌──────────▼──────┐   ┌─────────────▼──────┐   ┌──────────▼──────┐
│ Student Service │   │   Book Service      │   │  Loan Service   │
│   Port: 8081    │   │   Port: 8082        │   │  Port: 8083     │
│                 │   │                     │   │                 │
│  PostgreSQL     │   │  PostgreSQL         │   │  PostgreSQL     │
│  (userdb)       │   │  (bookdb)           │   │  (loandb)       │
└─────────────────┘   └─────────────────────┘   └─────────────────┘
         ▲                      ▲                        │
         │                      │      OpenFeign         │
         └──────────────────────┴────────────────────────┘
```

---

## 🔧 Services

| Service | Port | Description |
|---|---|---|
| 🔍 **service-registry** | `8761` | Netflix Eureka — service discovery & registration |
| ⚙️ **config-server** | `8088` | Centralized config management via Git repo |
| 🚪 **api-gateway** | `8080` | Single entry point — routing, load balancing, Swagger aggregation |
| 👨‍🎓 **student-service** | `8081` | Manages student registration, profiles, and paginated listing |
| 📖 **book-service** | `8082` | Manages books, inventory tracking, and availability |
| 📋 **loan-service** | `8083` | Manages book issuance, returns, and overdue tracking |

---

## 🛠 Tech Stack

| Category | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.4.6 |
| Service Discovery | Spring Cloud Netflix Eureka |
| API Gateway | Spring Cloud Gateway |
| Config Management | Spring Cloud Config Server |
| Inter-service Communication | OpenFeign |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| API Documentation | SpringDoc OpenAPI (Swagger UI) |
| Build Tool | Maven (Parent-Child Structure) |
| Containerization | Docker (Basic) |

---

## ✨ Features

- ✅ **Microservices Architecture** — independent, loosely coupled services
- ✅ **Service Discovery** — automatic service registration and discovery via Eureka
- ✅ **API Gateway** — single entry point with load balancing (`lb://`)
- ✅ **Centralized Config** — all service configs managed in Git repo via Config Server
- ✅ **Inter-service Communication** — synchronous calls via OpenFeign with error handling
- ✅ **Aggregated Swagger UI** — all service APIs visible at one place via gateway
- ✅ **REST Best Practices** — versioned endpoints (`/api/v1/**`)
- ✅ **Global Exception Handling** — consistent error responses across all services
- ✅ **Pagination & Sorting** — pageable responses for listing APIs
- ✅ **Loan Management** — issue, return, and overdue tracking
- ✅ **Inventory Tracking** — book availability management

---

## 🚀 Getting Started

### Prerequisites

```bash
Java 17+
Maven 3.8+
PostgreSQL 14+
Docker (optional)
```

### Database Setup

```sql
CREATE DATABASE userdb;
CREATE DATABASE bookdb;
CREATE DATABASE loandb;
```

### Startup Order

> ⚠️ Services must start in this exact order!

```
1️⃣  service-registry   → localhost:8761
2️⃣  config-server      → localhost:8088
3️⃣  api-gateway        → localhost:8080
4️⃣  student-service    → localhost:8081
5️⃣  book-service       → localhost:8082
6️⃣  loan-service       → localhost:8083
```

### Run each service

```bash
# Clone the repo
git clone https://github.com/dhaval1501/LMS-MICROSERVICE.git
cd LMS-MICROSERVICE

# Build all services
mvn clean install

# Run each service (in order)
cd service-registry && mvn spring-boot:run
cd config-server    && mvn spring-boot:run
cd api-gateway      && mvn spring-boot:run
cd student-service  && mvn spring-boot:run
cd book-service     && mvn spring-boot:run
cd loan-service     && mvn spring-boot:run
```

---

## 📖 API Documentation

Once all services are running, access the aggregated Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

Select any service from the dropdown to view its APIs:

| Dropdown Option | Service |
|---|---|
| `student-service` | Student APIs |
| `book-service` | Book APIs |
| `loan-service` | Loan APIs |

---

## 📡 API Reference

### 👨‍🎓 Student Service `/api/v1/students`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/students` | Add new student |
| `GET` | `/api/v1/students` | Get all students (pageable) |
| `GET` | `/api/v1/students/{id}` | Get student by ID |
| `PUT` | `/api/v1/students/{id}` | Update student |
| `DELETE` | `/api/v1/students/{id}` | Delete student |

### 📖 Book Service `/api/v1/books`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/books` | Add new book |
| `GET` | `/api/v1/books/{id}` | Get book by ID |
| `POST` | `/api/v1/books/search` | Search books |
| `POST` | `/api/v1/books/{id}/issue` | Issue book copy |
| `POST` | `/api/v1/books/{id}/return` | Return book copy |
| `DELETE` | `/api/v1/books/{id}` | Delete book |

### 📋 Loan Service `/api/v1/loans`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/loans` | Create loan (issue book) |
| `GET` | `/api/v1/loans/{id}` | Get loan by ID |
| `GET` | `/api/v1/loans` | Get all loans |
| `PUT` | `/api/v1/loans/{id}/return` | Return book |

---

## 📁 Project Structure

```
LMS-MICROSERVICE/                    ← Parent Maven Project
├── pom.xml                          ← Parent POM
├── compose.yml                      ← Docker Compose
├── api-gateway/                     ← Spring Cloud Gateway
│   └── src/main/resources/
│       └── application.yaml         ← imports from config-server
├── config-server/                   ← Spring Cloud Config
│   └── src/main/resources/
│       └── application.yaml         ← points to Git repo
├── service-registry/                ← Netflix Eureka Server
├── student-service/                 ← Student Microservice
│   └── src/main/java/com/lms/student_service/
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── entity/
│       ├── dto/
│       ├── mapper/
│       └── config/
├── book-service/                    ← Book Microservice
└── loan-service/                    ← Loan Microservice
    └── src/main/java/com/lms/loan_service/
        ├── client/                  ← Feign clients
        │   ├── StudentClient.java
        │   └── BookClient.java
        └── ...
```

---

## ⚙️ Configuration

All service configurations are centralized in a separate Git repository:

🔗 **Config Repo:** [github.com/dhaval1501/Config-Server](https://github.com/dhaval1501/Config-Server)

```
Config-Server/
├── application.yaml         ← shared config (Eureka, JPA)
├── student-service.yaml     ← student service config
├── book-service.yaml        ← book service config
├── loan-service.yaml        ← loan service config
└── api-gateway.yaml         ← gateway routes & swagger config
```

Each service's local `application.yaml` is minimal:

```yaml
spring:
  application:
    name: student-service
  config:
    import: configserver:http://localhost:8088
```

---

## 🔗 Related Repositories

| Repo | Description |
|---|---|
| [LMS-MICROSERVICE](https://github.com/dhaval1501/LMS-MICROSERVICE) | Main project |
| [Config-Server](https://github.com/dhaval1501/Config-Server) | Centralized config repo |

---
## 🗺️ Roadmap

Features and improvements planned for future development:

- [ ] **Spring Security + JWT** — Authentication and authorization with role-based access control
- [ ] **Redis Cache** — Caching frequently accessed data to improve response times
- [ ] **Liquibase** — Database version control and migration management
- [ ] **Resilience4j** — Circuit breaker, retry, and rate limiter for fault tolerance
- [ ] **Saga Pattern** — Distributed transaction management across microservices
- [ ] **Apache Kafka** — Async event-driven communication between services
- [ ] **CI/CD Pipeline** — Automated build, test, and deployment with GitHub Actions
- [ ] **Kubernetes** — Container orchestration for auto-scaling and self-healing

<div align="center">

Made with ❤️ by **Dhaval Dhimmar**

[![GitHub](https://img.shields.io/badge/GitHub-dhaval1501-black?style=flat&logo=github)](https://github.com/dhaval1501)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Dhaval_Dhimmar-blue?style=flat&logo=linkedin)](https://linkedin.com/in/dhaval-dhimmar-361632263)

</div>
