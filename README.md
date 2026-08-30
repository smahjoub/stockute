# Stockute

A **reactive stock portfolio management** backend application built with Spring Boot WebFlux. Stockute lets users manage portfolios, track securities and assets, monitor dividends, and handle transactions — all through a secure, JWT-authenticated REST API.

## Tech Stack

| Technology | Purpose |
|---|---|
| **Java 21** | Runtime |
| **Spring Boot 3.4.5** | Application framework |
| **Spring WebFlux** | Reactive web layer |
| **Spring Security** | JWT-based authentication & authorization |
| **Spring Data R2DBC** | Reactive database access |
| **PostgreSQL 17** | Primary database |
| **Liquibase** | Database schema migrations |
| **Lombok** | Boilerplate reduction |
| **MapStruct** | DTO / domain mapping |
| **Vavr** | Functional programming utilities |
| **JJWT** | JSON Web Token generation & validation |

## Project Structure

The project follows a **Hexagonal Architecture (Ports & Adapters)** pattern organized as a multi-module Maven project:

```
stockute/
├── stockute-domain/         # Core domain models (Security, Portfolio, Asset, Transaction, Dividend, …)
├── stockute-application/    # Use cases & port interfaces (business logic, no framework coupling)
├── stockute-adapters/       # Inbound (REST controllers) & outbound (persistence, external APIs) adapters
├── stockute-bootstrap/      # Spring Boot entry-point & configuration (application.yml, profiles)
└── stockute-postman/        # Postman collections for API testing
```

### Domain Models

`Security` · `Portfolio` · `Asset` · `Transaction` · `Currency` · `User` · `Role` · `SecurityDividend` · `PortfolioDividendEntitlement` · `UserPreference`

### REST API Endpoints (under `/api`)

| Controller | Base Path | Description |
|---|---|---|
| `AuthenticationController` | `/v1/auth` | Login / register |
| `ProfileController` | — | User profile management |
| `PortfolioController` | `/v1/portfolios` | Portfolio CRUD |
| `AssetController` | — | Asset management |
| `SecurityController` | `/v1/securities` | Security search & lookup |
| `TransactionController` | — | Buy / sell transactions |
| `CurrencyController` | — | Currency listing |
| `DividendController` | `/v1/portfolios` | Portfolio dividend stats & entitlements |
| `DividendCalendarController` | `/v1/dividends` | Dividend calendar & history |
| `UserPreferenceController` | `/v1/users/{userId}/preferences` | User preferences |

## Prerequisites

- **Java 21** (check with `java -version`)
- **Maven 3.8+** (check with `mvn -version`)
- **Docker & Docker Compose** (for PostgreSQL, pgAdmin, Redis)

## Getting Started

### 1. Start Infrastructure

Launch PostgreSQL, pgAdmin, and Redis via Docker Compose:

```bash
docker-compose up -d
```

| Service | URL | Credentials |
|---|---|---|
| PostgreSQL | `localhost:5432` | `admin` / `postgres` — DB: `stockute_db` |
| pgAdmin | `http://localhost:5050` | `admin@admin.com` / `admin` |
| Redis | `localhost:6379` | — |

### 2. Compile the Project

```bash
mvn clean compile
```

### 3. Run Tests

```bash
mvn test
```

### 4. Build (compile + test + package)

```bash
mvn clean install
```

### 5. Run the Application

#### Default profile (uses `application.yml` — `dev` profile active by default)

```bash
mvn spring-boot:run
```

> **Note:** The base `application.yml` sets `spring.profiles.active: dev`, so the dev profile is loaded automatically.

#### Explicitly activate the `dev` profile

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Run without any profile (default configuration only)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=none
```

#### Run with the `test` profile (in-memory H2 database)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

#### Run from a packaged JAR

```bash
mvn clean package -DskipTests
java -jar stockute-bootstrap/target/stockute-bootstrap-1.0-SNAPSHOT.jar --spring.profiles.active=dev
```

### 6. Verify the Application

Once running, the API is available at **`http://localhost:8081/api`**.

You can use the Postman collection in the `stockute-postman/` directory to explore the endpoints.

## Configuration Profiles

| Profile | File | Database | Description |
|---|---|---|---|
| **dev** *(default)* | `application-dev.yml` | PostgreSQL (`localhost:5432`) | Local development with mock external API endpoints |
| **test** | `application-test.yaml` | H2 in-memory | Integration tests — Liquibase drops & recreates schema |

## CI / CD

A GitHub Actions workflow (`.github/workflows/maven.yml`) runs `mvn clean install` on every push and pull request to the `main` branch.

## License

This project is maintained by **smahjoub**.