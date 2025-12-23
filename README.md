# Event-Driven E-Commerce Platform (Spring Boot + Kafka)

This repository contains an **event-driven e-commerce system** built with **Java, Spring Boot, Apache Kafka, and PostgreSQL**, following **microservices architecture** and **domain-driven design (DDD)** principles.

The system is designed to be **scalable, decoupled, and production-ready**, using Kafka as the backbone for asynchronous communication between services.

---

## Architecture Overview

The platform is composed of multiple **independent microservices**, each owning its own database and communicating mainly through **Kafka events**.

### Key architectural principles:
- Event-Driven Architecture (EDA)
- Database per service
- Loose coupling via Kafka topics
- Stateless services with JWT-based authentication
- Docker-based local infrastructure

---

## Microservices

| Service | Description | Port | Database |
|-------|------------|------|---------|
| **user-service** | User management, authentication, JWT | 8081 | users_db |
| **product-service** | Product and Categories management | 8082 | products_db |
| **order-service** | Orders management | 8083 | orders_db |

---

## Kafka Event Flow

Kafka is used as the **central event bus** for the system.

### Main topics:
- `user.created`
- `category.created`
- `category.updated`
- `category.deleted`
- `product.created`
- `product.updated`
- `product.deleted`
- `order.created`
- `order.paid`
- `order.cancelled`
- `order.shipped`

Each service **publishes domain events** and **reacts to events** from other services.

---

## Local Infrastructure (Docker)

All infrastructure dependencies are managed via **Docker Compose**:

- Apache Kafka
- Zookeeper
- Kafka UI
- PostgreSQL
- pgAdmin

Start everything with:

```bash
docker compose up -d
```

## Status

**Work in progress**:
The platform is being built step by step, service by service, following enterprise-grade best practices.

## Purpose
This project is part of a personal portfolio to strengthen programming fundamentals in Java and develop real, practical applications.
