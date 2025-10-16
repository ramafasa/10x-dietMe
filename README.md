# DietMe

![Version](https://img.shields.io/badge/version-0.0.1--SNAPSHOT-blue)
![Java](https://img.shields.io/badge/Java-21-orange)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2.0-purple)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green)

A comprehensive diet mentoring platform MVP that connects dietitians with clients through structured mentoring programs. DietMe enables continuous support during dietary habit transformation by providing tools for group management, educational content delivery, social interactions, and progress tracking through daily weight logging.

> **Note:** This is a certification project for [10xdev](https://10xdev.pl/).

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Local Development](#local-development)
  - [Environment Configuration](#environment-configuration)
- [Available Commands](#available-commands)
- [Project Scope](#project-scope)
  - [MVP Features](#mvp-features)
  - [Out of Scope](#out-of-scope)
- [Testing](#testing)
- [Code Quality](#code-quality)
- [Deployment](#deployment)
- [Project Status](#project-status)
- [Success Metrics](#success-metrics)
- [License](#license)

## Features

### For Dietitians
- **Group Management**: Create and manage up to 20 mentoring groups with configurable program duration (default 6 weeks)
- **Content Publishing**: Create posts with text, images (JPG, PNG, WebP up to 5MB), and video links (YouTube/Vimeo)
- **Scheduling**: Draft, schedule, and publish content with automatic publication
- **Moderation**: Delete comments, review abuse reports, and maintain community standards
- **Progress Monitoring**: View aggregate client progress with metrics like baseline weight, current weight, delta, and compliance rates

### For Clients
- **Content Consumption**: Access educational posts from dietitians in chronological feed
- **Social Interactions**: Like posts and comment to engage with the community
- **Weight Tracking**: Log daily weight entries (±0.1kg precision) with optional notes
- **Progress Visualization**: View weight charts with 7/30/90-day ranges and key metrics
- **Group Participation**: Join mentoring groups through invitation links or manual addition by dietitian

## Tech Stack

### Backend
- **Language**: Kotlin 2.2.0
- **Framework**: Spring Boot 3.5.7
- **JVM**: Java 21 (toolchain)
- **Build Tool**: Gradle 8.x with Kotlin DSL

### Database
- **RDBMS**: PostgreSQL 16
- **ORM**: Exposed (DAO pattern + DSL queries)
- **Migrations**: Liquibase
- **Connection Pooling**: HikariCP

### Infrastructure
- **File Storage**: AWS S3 (or S3-compatible like Cloudflare R2)
- **Image Processing**: Thumbnailator (generates 3 variants: 300px/800px/1920px)
- **Email Service**: AWS SES
- **Job Scheduling**: Quartz Scheduler (JDBC persistent store)
- **Containerization**: Docker + Docker Compose
- **Local Development**: LocalStack (S3 + SES mocking)

### Security & Compliance
- **Authentication**: Spring Security with password-based auth
- **Authorization**: Role-Based Access Control (RBAC)
- **Encryption**: TLS 1.3 in transit, AES-256 at rest
- **GDPR**: 24-month data retention, export/delete capabilities

### Testing
- **Framework**: Kotest (BDD style)
- **Mocking**: MockK
- **Integration Tests**: Testcontainers (PostgreSQL)
- **Spring Support**: kotest-extensions-spring

### Observability
- **Logging**: Logback with JSON format (logstash-logback-encoder)
- **Monitoring**: Spring Boot Actuator (health, metrics, info endpoints)
- **Log Rotation**: Daily with 30-day retention

### Code Quality
- **Linter**: ktlint 1.5.0 (strict validation, experimental rules enabled)
- **Style**: Tabs indentation, trailing commas required, max 120 chars/line
- **CI/CD**: GitHub Actions (lint + build + test)

## Architecture

DietMe follows a layered architecture with clear separation of concerns:

```
┌─────────────────────────────────────────┐
│         Frontend (Next.js 15)           │
│         (Separate Repository)           │
└─────────────────────────────────────────┘
                    ↓ REST API
┌─────────────────────────────────────────┐
│      Spring Boot Application            │
│  ┌────────────────────────────────────┐ │
│  │   Controllers (REST Endpoints)     │ │
│  └────────────────────────────────────┘ │
│  ┌────────────────────────────────────┐ │
│  │   Services (Business Logic)        │ │
│  └────────────────────────────────────┘ │
│  ┌────────────────────────────────────┐ │
│  │   Repositories (Exposed DAO/DSL)   │ │
│  └────────────────────────────────────┘ │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│         PostgreSQL Database             │
└─────────────────────────────────────────┘

External Services:
- AWS S3 (file storage)
- AWS SES (email delivery)
- Quartz Scheduler (background jobs)
```

### Key Design Decisions
- **Exposed ORM**: Type-safe SQL with DAO pattern for entities and DSL for complex queries
- **Liquibase**: Version-controlled database schema evolution
- **Quartz Scheduler**: Persistent job store for scheduled posts, token cleanup, and GDPR retention
- **Multi-stage Docker**: Optimized builder + runtime images
- **Separation of Concerns**: Backend API separate from frontend (Next.js in different repo)

## Getting Started

### Prerequisites

- **Java Development Kit (JDK)**: Version 21 or higher
- **Docker**: For running PostgreSQL, LocalStack, and containerized app
- **Docker Compose**: For orchestrating local development environment
- **Gradle**: Wrapper included (no manual installation needed)

### Local Development

#### 1. Clone the Repository
```bash
git clone <repository-url>
cd 10x-dietMe
```

#### 2. Configure Environment Variables
Copy the example environment file and adjust values:
```bash
cp .env.example .env
```

Key environment variables:
```bash
# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/dietme
DATABASE_USERNAME=dietme
DATABASE_PASSWORD=dietme

# AWS (LocalStack for local dev)
AWS_REGION=eu-west-1
AWS_ENDPOINT_OVERRIDE=http://localhost:4566
S3_BUCKET_NAME=dietme-uploads
SES_FROM_EMAIL=noreply@dietme.local

# Application
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:3000
SPRING_PROFILES_ACTIVE=dev
```

#### 3. Start with Docker Compose
```bash
# Start all services (postgres + localstack + app)
docker-compose up

# Or start only dependencies (if running app from IDE)
docker-compose up postgres localstack
```

Services will be available at:
- **Application**: http://localhost:8080
- **Actuator Health**: http://localhost:8080/actuator/health
- **PostgreSQL**: localhost:5432
- **LocalStack (S3/SES)**: localhost:4566

#### 4. Run Application without Docker
```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

#### 5. Verify Setup
```bash
# Check application health
curl http://localhost:8080/actuator/health

# Expected response:
# {"status":"UP"}
```

### Environment Configuration

For production deployment, configure these additional variables:
```bash
# AWS Production
AWS_ACCESS_KEY_ID=<your-access-key>
AWS_SECRET_ACCESS_KEY=<your-secret-key>
AWS_ENDPOINT_OVERRIDE=  # Leave empty for production AWS

# Security
JWT_SECRET=<your-jwt-secret>
SPRING_PROFILES_ACTIVE=prod

# Database (managed service)
DATABASE_URL=<rds-or-managed-postgres-url>
```

## Available Commands

### Build & Run
```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Clean build artifacts
./gradlew clean

# Build Docker image
docker build -t dietme:latest .

# Run with Docker Compose
docker-compose up
```

### Testing
```bash
# Run all tests (includes Testcontainers)
./gradlew test

# Run tests with verbose output
./gradlew test --info

# Run specific test class
./gradlew test --tests "pl.rmaciak.dietme.ApplicationTests"

# Run tests with coverage (if configured)
./gradlew test jacocoTestReport
```

### Code Quality
```bash
# Check code style (strict validation)
./gradlew ktlintCheck

# Auto-fix code style violations
./gradlew ktlintFormat

# Run all checks (lint + tests)
./gradlew check

# Before committing (recommended workflow)
./gradlew ktlintFormat check
```

### Database Migrations
```bash
# Apply migrations (automatic on startup)
./gradlew bootRun

# Create new migration (manual)
# Add changelog in: src/main/resources/db/changelog/
```

### Docker Operations
```bash
# Start all services
docker-compose up

# Start in detached mode
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs -f app

# Rebuild images
docker-compose build --no-cache
```

## Project Scope

### MVP Features ✅

**Authentication & Authorization**
- Email + password authentication
- Role-based access control (Dietitian vs. Client)
- Password reset via email link
- Session management with timeout

**Group Management**
- Create groups with configurable duration (default 6 weeks)
- Generate invitation links with expiration
- Manual client addition by dietitian
- Support for up to 20 groups and 200 clients total

**Content Publishing**
- Posts with text, images (upload), and videos (YouTube/Vimeo links)
- Draft, scheduled, and published states
- Pin one post per group
- Simple tagging system

**Social Interactions**
- Like posts (toggle on/off)
- Comment on posts (flat structure, no threading)
- Report abusive comments
- Dietitian moderation (delete comments)

**Weight Tracking**
- Daily weight entries in kg (0.1 precision)
- Optional notes (up to 500 characters)
- Late entry allowed until 02:00 next day (local timezone)
- Visualization with 7/30/90-day ranges
- Aggregate progress view for dietitians

**GDPR Compliance**
- 24-month data retention
- User data export (JSON/PDF)
- Right to be forgotten (account deletion)
- Mandatory consent checkboxes during registration

### Out of Scope 🚫

The following features are explicitly excluded from MVP and may be considered in future iterations:

**Notifications**
- Email notifications (welcome, reset password emails are included)
- Push notifications
- In-app notifications
- Personalized notification preferences

**Advanced Features**
- Multi-tenancy (multiple dietitian organizations)
- Dedicated diet plans and meal calendars
- Recipe database
- Automated weight anomaly alerts
- Advanced analytics and engagement metrics
- Gamification (badges, achievements, streaks)
- Private messaging between client and dietitian

**Integrations**
- IoT devices (smart scales, fitness trackers)
- Payment processing and subscriptions
- Third-party diet databases

**Social Features**
- Cross-group interactions
- Comment threading (replies to comments)
- Disable comments per post
- Social sharing outside groups

**Platform Extensions**
- Native mobile apps (iOS, Android)
- Video consultations
- Live chat
- Magic link authentication
- Two-factor authentication (2FA)

## Testing

DietMe uses **Kotest** for BDD-style testing with comprehensive coverage:

### Test Structure
```kotlin
class MyServiceTest : FunSpec({
    test("should create user successfully") {
        // Given
        val userRequest = UserRequest(email = "test@example.com")

        // When
        val result = userService.createUser(userRequest)

        // Then
        result.email shouldBe "test@example.com"
    }
})
```

### Test Categories
- **Unit Tests**: Services, validators, business logic
- **Integration Tests**: Repositories, database operations (Testcontainers)
- **E2E Tests**: Critical flows (authentication, post creation, weight logging)

### Running Tests
```bash
# All tests
./gradlew test

# With Testcontainers (PostgreSQL automatically started)
./gradlew test --info

# Specific test class
./gradlew test --tests "pl.rmaciak.dietme.auth.AuthServiceTest"
```

### Test Configuration
- **MockK** for mocking dependencies
- **Testcontainers** for PostgreSQL integration tests (auto-start/stop)
- **Kotest Spring Extension** for `@SpringBootTest` support

## Code Quality

### ktlint Configuration
The project enforces strict code style with **ktlint 1.5.0**:

**Rules:**
- Tabs indentation (4 spaces)
- Max line length: 120 characters
- Trailing commas required (calls + declarations)
- No wildcard imports
- No unused imports
- Experimental rules enabled
- **Violations fail the build** (`ignoreFailures = false`)

**Workflow:**
```bash
# Before committing (auto-fix + validate)
./gradlew ktlintFormat check

# CI/CD pipeline runs ktlintCheck on every push
```

### Git Hooks (Optional)
```bash
# Install pre-commit hook for automatic formatting
./scripts/install-git-hooks.sh
```

## Deployment

### Docker Deployment
```bash
# Build image
docker build -t dietme:latest .

# Run container
docker run -p 8080:8080 \
  -e DATABASE_URL=jdbc:postgresql://host:5432/dietme \
  -e AWS_REGION=eu-west-1 \
  dietme:latest
```

### Production Environments
Recommended platforms:
- **Railway** (managed Postgres + Docker deployment)
- **Render** (managed DB + container hosting)
- **AWS ECS** (with RDS PostgreSQL + S3)

**Production Checklist:**
- [ ] Configure AWS Secrets Manager for sensitive values
- [ ] Enable database encryption at rest (managed service)
- [ ] Set up CloudWatch or equivalent logging
- [ ] Configure CORS whitelist for frontend domains
- [ ] Enable Spring Boot Actuator with restricted access
- [ ] Set up database backups (automated snapshots)
- [ ] Configure Liquibase migrations for production
- [ ] Test S3 bucket permissions and signed URLs
- [ ] Verify SES email sending limits and domain verification

## Project Status

**Current Version:** `0.0.1-SNAPSHOT`
**Stage:** Early Development / MVP Implementation
**Target:** Certification project for 10xdev

### Development Roadmap

**Phase 1: Foundation (Current)**
- [x] Project setup with Spring Boot + Kotlin
- [x] Database configuration (PostgreSQL + Exposed + Liquibase)
- [x] Testing framework (Kotest + MockK + Testcontainers)
- [x] Code quality (ktlint strict validation)
- [x] Docker Compose for local development
- [ ] Authentication & authorization implementation
- [ ] Core domain models and repositories

**Phase 2: Core Features**
- [ ] User registration and login
- [ ] Group management (create, invite, manage members)
- [ ] Post creation and publishing (text, images, video links)
- [ ] Social interactions (likes, comments)
- [ ] Weight tracking and visualization

**Phase 3: Polish & Deploy**
- [ ] Email integration (AWS SES)
- [ ] Scheduled jobs (Quartz)
- [ ] GDPR compliance features
- [ ] E2E testing of critical flows
- [ ] Production deployment
- [ ] Frontend integration (Next.js separate repo)

### Known Limitations
- **Single dietitian**: MVP supports one dietitian managing multiple groups (no multi-tenancy)
- **Capacity**: Max 20 groups, 200 clients total
- **No notifications**: Welcome/reset emails only (no push/in-app notifications)
- **Basic moderation**: Manual comment deletion only (no automated abuse detection)
- **Image formats**: JPG, PNG, WebP only (max 5MB)

## Success Metrics

The MVP is considered successful if it meets the following criteria:

### Primary KPI
**≥50% of clients achieve ≥2kg weight reduction** at program end

**Calculation Method:**
- **Baseline weight** = average of first 3 days with entries
- **Final weight** = average of last 3 days with entries
- **Success** = baseline weight - final weight ≥ 2.0 kg
- **Success rate** = (clients with ≥2kg reduction / total clients) × 100%

**Eligibility:**
- Client must have ≥3 weight entries in first 7 days (baseline)
- Client must have ≥3 weight entries in last 7 days (final)
- Measurement occurs 7 days after program end date

### Secondary Metrics
- **Completion rate**: ≥70% clients complete the program (log weight in final week)
- **Technical uptime**: ≥99.5% availability (≤3.65 days downtime/year)
- **User satisfaction**: Positive qualitative feedback from dietitian and sample clients

### Performance Targets
- Page load time (p95): <3 seconds
- Chart rendering (p95): <2 seconds
- Image upload 5MB (p95): <10 seconds

## License

This project currently has no license specified. Please contact the maintainers for usage permissions.

---

**Developed as a certification project for [10xdev](https://10xdev.pl/).**

For detailed product requirements, see [PRD](.ai/prd.md).
For architectural decisions, see [Tech Stack](.ai/tech-stack.md).
For development guidelines, see [CLAUDE.md](CLAUDE.md).
