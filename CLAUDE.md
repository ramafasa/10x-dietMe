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
- **Database**: PostgreSQL with Liquibase migrations
- **File storage**: S3-compatible storage (AWS S3 or Cloudflare R2) with 3 image variants (thumbnail/medium/full)
- **Scheduling**: Quartz Scheduler for scheduled posts, token cleanup, invitation expiry, GDPR retention (24 months)
- **Email**: AWS SES/SendGrid/Resend for registration, password reset, invitations
- **Testing**: JUnit 5 + Testcontainers for PostgreSQL integration tests
- **Frontend**: Next.js 15 (separate repository, not in this codebase)

## Essential Commands

### Build & Run
```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run with test profile
./gradlew bootTestRun

# Build Docker image
./gradlew bootBuildImage

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

## Project Structure

```
.
├── src/
│   ├── main/kotlin/pl/rmaciak/dietme/
│   │   └── Application.kt              # Spring Boot entry point
│   └── test/kotlin/pl/rmaciak/dietme/
│       ├── ApplicationTests.kt          # Integration tests
│       ├── TestApplication.kt           # Test runner with Testcontainers
│       └── TestcontainersConfiguration.kt
├── .ai/                                 # Project documentation
│   ├── prd.md                          # Full Product Requirements Document
│   ├── planning-summary.md             # Planning decisions summary
│   ├── project-description.md          # MVP scope definition
│   ├── tech-stack.md                   # Detailed tech stack decisions
│   └── ktlint-guide.md                 # ktlint usage guide
├── .editorconfig                        # Code style config (ktlint rules)
├── build.gradle.kts                     # Gradle build configuration
└── settings.gradle.kts
```

## Development Notes

### Database Migrations
- **Liquibase** is configured for schema versioning
- Migrations should be placed in `src/main/resources/db/changelog/`
- Always create reversible migrations when possible

### Testing with Testcontainers
- PostgreSQL Testcontainers are configured in `TestcontainersConfiguration.kt`
- Containers are automatically started/stopped for integration tests
- Use `@SpringBootTest` with Testcontainers for integration tests

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

## Common Pitfalls

1. **ktlint violations block builds**: Always run `./gradlew ktlintFormat` before committing
2. **Tabs vs. spaces**: This project uses tabs for Kotlin files (`.editorconfig` enforces this)
3. **Java version**: JVM toolchain is Java 21 (not 25, as Kotlin 2.2.0 doesn't support it yet)
4. **Testcontainers requires Docker**: Docker must be running for tests to pass
5. **Trailing commas**: Required by ktlint config; auto-fix with `ktlintFormat`
