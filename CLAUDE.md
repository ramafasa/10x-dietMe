# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**DietMe** is a diet mentoring platform MVP that connects dietitians with their clients. The system enables dietitians to manage mentoring groups, publish content (posts with text/images/videos), and monitor client progress through daily weight logging. This is a certification project for 10xdev.

### Key Functional Requirements
- **Two user roles**: Dietitian (creates groups, publishes content, moderates, views progress) and Client (consumes content, likes/comments, logs weight)
- **Group management**: Dietitians create groups with configurable duration (default 6 weeks), simultaneous start, invitation links + manual addition
- **Content publishing**: Posts with text/image (upload) and video (link), drafts, scheduled publishing, editing with "edited" label, 1 pinned post per group, tags
- **Interactions**: Likes and comments within groups, abuse reporting, dietitian can delete comments
- **Weight tracking**: Daily entries in kg (0.1 precision), late entry allowed until 02:00 next day (local time), notes, visualization (7/30/90 days), aggregate view for dietitian
- **Success metric**: ≥50% clients with ≥2kg weight reduction at program end

### Architecture Decisions
- **Backend**: Spring Boot 3.5.7 + Kotlin 2.2.0
- **Database**: PostgreSQL 16 with Exposed ORM (DAO pattern) and Liquibase migrations
- **Connection pooling**: HikariCP (included with spring-boot-starter-jdbc)
- **File storage**: AWS S3 with 3 image variants (thumbnail 300px/medium 800px/full 1920px)
- **Image processing**: Thumbnailator library for resizing
- **Scheduling**: Quartz Scheduler (JDBC persistent store) for scheduled posts, token cleanup, invitation expiry, GDPR retention
- **Email**: AWS SES for transactional emails
- **Serialization**: Jackson (jackson-module-kotlin) for JSON processing
- **Logging**: Logback with JSON format (logstash-logback-encoder), daily rotation, 30 days retention
- **Testing**: Kotest (BDD style) + MockK + Testcontainers (PostgreSQL) for integration tests
- **Monitoring**: Spring Boot Actuator (health, metrics, info endpoints)
- **Docker**: Multi-stage build with LocalStack for local S3/SES development
- **CI/CD**: GitHub Actions (ktlint + build + test)
- **Frontend**: Next.js 15 with TypeScript and Tailwind CSS (located in `/frontend` directory)

## Essential Commands

### Build & Run
```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run with docker-compose (postgres + localstack + app)
docker-compose up

# Run only dependencies (postgres + localstack)
docker-compose up postgres localstack

# Build Docker image
docker build -t dietme:latest .

# Clean build artifacts
./gradlew clean
```

### Testing
```bash
# Run all tests
./gradlew test

# Run tests with verbose output
./gradlew test --info

# Run specific test class
./gradlew test --tests "pl.rmaciak.dietme.ApplicationTests"

# Run tests with Testcontainers (PostgreSQL)
./gradlew test  # Testcontainers configured by default
```

### Code Quality (ktlint)
```bash
# Check code style (strict validation)
./gradlew ktlintCheck

# Auto-fix code style violations
./gradlew ktlintFormat

# Check only main sources
./gradlew ktlintMainSourceSetCheck

# Check only test sources
./gradlew ktlintTestSourceSetCheck

# Run all checks (includes ktlint + tests)
./gradlew check
```

### Development Workflow
```bash
# Before committing: format code and run checks
./gradlew ktlintFormat check

# Install git pre-commit hook (optional)
./scripts/install-git-hooks.sh
```

### Frontend Commands

The frontend is located in the `/frontend` directory:

```bash
# Navigate to frontend
cd frontend

# Install dependencies
npm install

# Run development server (http://localhost:3000)
npm run dev

# Build for production
npm run build

# Start production server
npm start

# Run ESLint
npm run lint

# Type check
npm run type-check
```

**Frontend Environment Variables:**
- `NEXT_PUBLIC_API_URL`: Backend API URL (default: http://localhost:8080)
- See `frontend/.env.example` for all available variables

## Code Style & Standards

### ktlint Configuration
- **Strict validation** with ktlint 1.5.0 and experimental rules enabled
- **Indentation**: Tabs (4 spaces), max line length 120 characters
- **Trailing commas**: Required on call sites and declaration sites
- **No wildcard imports**, no unused imports, no consecutive blank lines
- **All violations fail the build** (`ignoreFailures = false`)
- Configuration in `.editorconfig` (lines 13-46) and `build.gradle.kts` (lines 49-62)

### Kotlin Conventions
- Use tabs for indentation (not spaces)
- Always use trailing commas in multi-line function parameters and data classes
- No `Unit` return type (implicit)
- No semicolons
- String templates over concatenation
- Proper modifier order per Kotlin standards

## Development Notes

### Database Migrations
- **Liquibase** is configured for schema versioning
- Migrations should be placed in `src/main/resources/db/changelog/`
- Always create reversible migrations when possible

### Testing with Kotest and Testcontainers
- **Test framework**: Kotest (BDD style with FunSpec, StringSpec, etc.)
- **Mocking library**: MockK (Kotlin-native, replaces Mockito)
- **Spring integration**: kotest-extensions-spring for `@SpringBootTest` support
- PostgreSQL Testcontainers are configured in `TestcontainersConfiguration.kt`
- Containers are automatically started/stopped for integration tests
- Test files use Kotest specs (e.g., `class MyTest : FunSpec({ test("...") { } })`)

### Security Requirements
- Password-based authentication (no magic links in MVP)
- Password policy: min 8 chars, 1 digit, 1 uppercase
- RBAC: dietitian vs. client roles
- Data encryption at rest (managed PostgreSQL service)
- CORS whitelist for frontend origins (Vercel)
- Environment-based secrets (AWS Secrets Manager in production)

### File Upload Constraints
- Max image size: 5 MB
- Allowed formats: JPG, PNG, WebP
- Generate 3 variants: thumbnail (300px), medium (800px), full (1920px)
- Alt-text required for accessibility
- Signed URLs with 1-hour expiration for private files

### Data Retention (GDPR)
- Personal data retention: 24 months
- Scheduled cleanup job runs weekly (Quartz)
- Users can export/delete their data on request

### Limits & Constraints
- Max 20 groups total
- Max 200 clients total
- Target availability: 99.5% (≤3.65 days downtime/year)

## Key Documentation

Critical project documentation is in `.ai/` directory:
- **PRD** (`.ai/prd.md`): Full product requirements with user stories, acceptance criteria, and technical specifications
- **Planning Summary** (`.ai/planning-summary.md`): All architectural decisions, trade-offs, and unresolved issues
- **Tech Stack** (`.ai/tech-stack.md`): Detailed technology choices including database schema approach, file storage, email service, security measures

### Unresolved Issues (from planning)
1. Double opt-in for invitations: MVP or later?
2. Weight anomaly alerts and "quiet hours" for notifications
3. RPO/RTO targets, backup strategy, monitoring tools
4. Internationalization (PL-only in MVP?) and accessibility baseline
5. API rate limits, field size limits, log retention

## Configuration

### Environment Variables
See `.env.example` for all available configuration options. Key variables:

**Database:**
- `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`

**AWS:**
- `AWS_REGION`, `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`
- `AWS_ENDPOINT_OVERRIDE` (for LocalStack: http://localhost:4566)
- `S3_BUCKET_NAME`, `SES_FROM_EMAIL`

**Application:**
- `SERVER_PORT` (default: 8080)
- `CORS_ALLOWED_ORIGINS` (default: http://localhost:3000)
- `SPRING_PROFILES_ACTIVE` (dev/test/prod)

### Local Development with Docker
```bash
# Start all services (postgres + localstack + app)
docker-compose up

# Access services:
# - App: http://localhost:8080
# - Actuator health: http://localhost:8080/actuator/health
# - PostgreSQL: localhost:5432
# - LocalStack S3/SES: localhost:4566
```

## Common Pitfalls

1. **ktlint violations block builds**: Always run `./gradlew ktlintFormat` before committing
2. **Tabs vs. spaces**: This project uses tabs for Kotlin files (`.editorconfig` enforces this)
3. **Java version**: JVM toolchain is Java 21 (not 25, as Kotlin 2.2.0 doesn't support it yet)
4. **Testcontainers requires Docker**: Docker must be running for tests to pass
5. **Trailing commas**: Required by ktlint config; auto-fix with `ktlintFormat`
6. **Kotest test structure**: Use Kotest specs (FunSpec, StringSpec, etc.) instead of JUnit `@Test` annotations
7. **MockK syntax**: Use MockK DSL (`mockk<Type>()`, `every { }`) instead of Mockito
- always make sure that there is no linter issues
- always make sure that application build passes and all tests are green