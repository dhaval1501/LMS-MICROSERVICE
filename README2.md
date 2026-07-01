# 📚 Library Management System — Microservices

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.6-green?style=for-the-badge&logo=springboot)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2024.0.1-green?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![Kafka](https://img.shields.io/badge/Apache_Kafka-Event_Driven-231F20?style=for-the-badge&logo=apachekafka&logoColor=231F20)
![Zipkin](https://img.shields.io/badge/Zipkin-Tracing-FF6F00?style=for-the-badge&logo=zipkin&logoColor=F46800)
![Prometheus](https://img.shields.io/badge/Prometheus-Metrics-E6522C?style=for-the-badge&logo=prometheus&&logoColor=E6522C)
![Grafana](https://img.shields.io/badge/Grafana-Dashboards-F46800?style=for-the-badge&logo=grafana&logoColor=F46800)
![Docker](https://img.shields.io/badge/Docker-Basic-blue?style=for-the-badge&logo=docker)
![Swagger](https://img.shields.io/badge/Swagger-UI-green?style=for-the-badge&logo=swagger)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apachemaven&logoColor=C71A36)

[//]: # (https://img.shields.io/badge/Kubernetes-555555?style=for-the-badge&logo=kubernetes&logoColor=326CE5)
[//]: # (![Redis]&#40;https://img.shields.io/badge/Redis-7.2-DC382D?style=for-the-badge&logo=redis&logoColor=DC382D&#41;)

A production-ready **Library Management System** built with **Spring Boot Microservices Architecture**, featuring service discovery, centralized configuration, API gateway routing, event-driven communication with **Kafka**, distributed tracing with **Zipkin**, and observability with **Prometheus** and **Grafana**.

</div>

---

## 📌 Table of Contents

- [Architecture](#-architecture)
- [Services](#-services)
- [Tech Stack](#-tech-stack)
- [Features](#-features)
- [Observability & Monitoring](#-observability--monitoring)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [API Reference](#-api-reference)
- [Project Structure](#-project-structure)
- [Configuration](#-configuration)

---

## 🏗 Architecture

```
                        ┌─────────────────────┐
                        │   Config Server     │
                        │   (Git-backed)      │
                        │   Port: 8088        │
                        └──────────┬──────────┘
                                   │ centralized config
                                   │
              ┌────────────────────▼───────────────────┐
              │           Service Registry             │
              │           (Netflix Eureka)             │
              │           Port: 8761                   │
              └────────────────────┬───────────────────┘
                                   │ service discovery
                                   │
┌──────────┐          ┌────────────▼───────────┐
│  Client  │─────────▶│      API Gateway       │
│ (Postman/│          │   (Spring Cloud GW)    │
│ Frontend)│◀─────────│      Port: 8080        │
└──────────┘          └──┬──────────┬───────┬──┘
                         │          │       │
              lb://      │          │       │  lb://
           ┌─────────────┘    lb:// │       └─────────────┐
           │                        │                     │
┌──────────▼──────┐   ┌─────────────▼──────┐   ┌──────────▼──────┐
│ Student Service │   │   Book Service     │   │  Loan Service   │
│   Port: 8081    │   │   Port: 8082       │   │  Port: 8083     │
│                 │   │                    │   │                 │
│  PostgreSQL     │   │  PostgreSQL        │   │  PostgreSQL     │
│  (userdb)       │   │  (bookdb)          │   │  (loandb)       │
└────────┬────────┘   └──────────┬─────────┘   └────────┬────────┘
         │                       │                      │
         │            OpenFeign  │                      │
         └───────────────────────┴──────────────────────┘
         │                       │                      │
         │           produces/consumes events           │
         └───────────────────────┬──────────────────────┘
                                 │
                        ┌────────▼────────────┐
                        │   Apache Kafka      │
                        │   Port: 9092        │
                        └─────────────────────┘

     ┌───────────────────────  Observability Stack  ───────────────────────┐
     │                                                                     │
     │   traces from all services            metrics scraped from          │
     │            │                          each service's /actuator      │
     │   ┌────────▼────────┐            ┌────────────▼────────────┐        │
     │   │      Zipkin     │            │        Prometheus       │        │
     │   │   Port: 9411    │            │        Port: 9090       │        │
     │   └─────────────────┘            └────────────┬────────────┘        │
     │                                               │ data source         │
     │                                       ┌───────▼───────────┐         │
     │                                       │      Grafana      │         │
     │                                       │     Port: 3000    │         │
     │                                       └───────────────────┘         │
     └─────────────────────────────────────────────────────────────────────┘
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
| 📨 **kafka** | `9092` | Event broker for async, event-driven communication between services |
| 🔭 **zipkin** | `9411` | Distributed tracing UI — visualize request flow across services |
| 📈 **prometheus** | `9090` | Metrics collection — scrapes `/actuator/prometheus` from each service |
| 📊 **grafana** | `3000` | Dashboards for visualizing Prometheus metrics |

---

## 🛠 Tech Stack

| Category | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.4.6 |
| Service Discovery | Spring Cloud Netflix Eureka |
| API Gateway | Spring Cloud Gateway |
| Config Management | Spring Cloud Config Server |
| Inter-service Communication (Sync) | OpenFeign |
| Inter-service Communication (Async) | Apache Kafka |
| Distributed Tracing | Zipkin (via Micrometer Tracing) |
| Metrics | Micrometer + Prometheus |
| Dashboards | Grafana |
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
- ✅ **Synchronous Communication** — inter-service calls via OpenFeign with error handling
- ✅ **Event-Driven Communication** — async messaging between services using **Apache Kafka**
- ✅ **Distributed Tracing** — end-to-end request tracing across services with **Zipkin**
- ✅ **Metrics & Monitoring** — service health and performance metrics via **Prometheus**
- ✅ **Visual Dashboards** — real-time dashboards built with **Grafana**
- ✅ **Aggregated Swagger UI** — all service APIs visible at one place via gateway
- ✅ **REST Best Practices** — versioned endpoints (`/api/v1/**`)
- ✅ **Global Exception Handling** — consistent error responses across all services
- ✅ **Pagination & Sorting** — pageable responses for listing APIs
- ✅ **Loan Management** — issue, return, and overdue tracking
- ✅ **Inventory Tracking** — book availability management

---

## 📊 Observability & Monitoring

This project now ships with a full observability stack:

| Tool | Purpose | URL |
|---|---|---|
| **Zipkin** | Distributed tracing — trace a request as it flows through gateway → services → Kafka | `http://localhost:9411` |
| **Prometheus** | Scrapes and stores metrics exposed by each service's Actuator endpoint | `http://localhost:9090` |
| **Grafana** | Visualizes Prometheus metrics through pre-built/custom dashboards | `http://localhost:3000` |

Each service exposes the following Actuator endpoints for observability:

```
/actuator/health
/actuator/prometheus
```

> 💡 Tip: Add Prometheus as a data source in Grafana (`http://prometheus:9090` in Docker, or `http://localhost:9090` locally) to start building dashboards for request rates, latency, and error counts per service.

---

## 🚀 Getting Started

### Prerequisites

```bash
Java 17+
Maven 3.8+
PostgreSQL 14+
Docker (optional, recommended for Kafka/Zipkin/Prometheus/Grafana)
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
3️⃣  kafka              → localhost:9092
4️⃣  zipkin             → localhost:9411
5️⃣  prometheus         → localhost:9090
6️⃣  grafana            → localhost:3000
7️⃣  api-gateway        → localhost:8080
8️⃣  student-service    → localhost:8081
9️⃣  book-service       → localhost:8082
🔟  loan-service       → localhost:8083
```

### Run each service

```bash
# Clone the repo
git clone https://github.com/dhaval1501/LMS-MICROSERVICE.git
cd LMS-MICROSERVICE

# Start infrastructure (Kafka, Zipkin, Prometheus, Grafana) via Docker Compose
docker-compose up -d

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
| `POST` | `/api/v1/loans` | Create loan (issue book) — publishes a `LoanCreated` event to Kafka |
| `GET` | `/api/v1/loans/{id}` | Get loan by ID |
| `GET` | `/api/v1/loans` | Get all loans |
| `PUT` | `/api/v1/loans/{id}/return` | Return book — publishes a `LoanReturned` event to Kafka |

---

## 📁 Project Structure

```
LMS-MICROSERVICE/                    ← Parent Maven Project
├── pom.xml                          ← Parent POM
├── compose.yml                      ← Docker Compose (Kafka, Zipkin, Prometheus, Grafana, etc.)
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
        ├── producer/                ← Kafka producers
        └── ...
```

---

## ⚙️ Configuration

All service configurations are centralized in a separate Git repository:

🔗 **Config Repo:** [github.com/dhaval1501/Config-Server](https://github.com/dhaval1501/Config-Server)

```
Config-Server/
├── application.yaml         ← shared config (Eureka, JPA, Kafka, Zipkin, Actuator/Prometheus)
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

Shared config in `application.yaml` (Config Server) now includes Kafka, tracing, and metrics:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
  zipkin:
    tracing:
      endpoint: http://localhost:9411/api/v2/spans
  tracing:
    sampling:
      probability: 1.0

management:
  endpoints:
    web:
      exposure:
        include: health,prometheus,info
  metrics:
    tags:
      application: ${spring.application.name}
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

- [x] ~~**Apache Kafka** — Async event-driven communication between services~~
- [x] ~~**Zipkin** — Distributed tracing across services~~
- [x] ~~**Prometheus** — Metrics collection~~
- [x] ~~**Grafana** — Visualization dashboards~~
- [ ] **Spring Security + JWT** — Authentication and authorization with role-based access control
- [ ] **Redis Cache** — Caching frequently accessed data to improve response times
- [ ] **Liquibase** — Database version control and migration management
- [ ] **Resilience4j** — Circuit breaker, retry, and rate limiter for fault tolerance
- [ ] **Saga Pattern** — Distributed transaction management across microservices
- [ ] **CI/CD Pipeline** — Automated build, test, and deployment with GitHub Actions
- [ ] **Kubernetes** — Container orchestration for auto-scaling and self-healing

<div align="center">

Made with ❤️ by **Dhaval Dhimmar**

[![GitHub](https://img.shields.io/badge/GitHub-dhaval1501-black?style=flat&logo=github)](https://github.com/dhaval1501)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Dhaval_Dhimmar-blue?style=flat&logo=linkedin)](https://linkedin.com/in/dhaval-dhimmar-361632263)

</div>