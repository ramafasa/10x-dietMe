# Tech Stack - DietMe MVP

## Frontend

**Technology:** Next.js 15 (App Router) + TypeScript + React 18
---

## Backend

**Technology:** Kotlin 2.2 + Spring Boot + Exposed + PostgreSQL

#### Baza danych
- **PostgreSQL 16** - relational database
- **Exposed ORM** - type-safe SQL DSL
  - DAO pattern dla entities
  - DSL queries dla custom operations
- **HikariCP** - connection pooling
- **Liquibase** - database migrations (versioning schema)

#### File storage (S3)
- **AWS S3** lub **Cloudflare R2** (S3-compatible)
- **Image processing:**
  - Thumbnailator library - resize obrazów
  - Generowanie 3 wariantów: thumbnail (300px), medium (800px), full (1920px)
  - WebP conversion dla mniejszych rozmiarów
- **Storage structure:**
  ```
  /uploads/
    /{userId}/
      /posts/
        /{postId}/
          /original.jpg
          /thumbnail.webp
          /medium.webp
          /full.webp
  ```
- Signed URLs z expiracją (1h) dla prywatnych plików
- Public read dla obrazów w opublikowanych postach

#### Email service
- **AWS SES** lub **SendGrid** lub **Resend**
- **Email templates:**
  - Powitanie po rejestracji
  - Reset hasła (link z tokenem)
  - Dokończenie rejestracji (dodanie przez dietetyka)
  - Potwierdzenie zmiany hasła
- HTML templates z inline CSS (email compatibility)
- Async sending (background jobs)

#### Scheduled jobs
- **Quartz Scheduler** - job scheduling
- **Jobs:**
  - Publikacja zaplanowanych postów (co 1 min check)
  - Cleanup nieważnych tokenów resetowania hasła (daily)
  - Cleanup expired invitations (daily)
  - RODO: usunięcie danych po 24 miesiącach (weekly)
- Persistent job store w PostgreSQL (Quartz tables)

#### Walidacja danych
- **kotlinx.serialization** - JSON serialization/deserialization
- **Custom validation layer:**
  - Email format, uniqueness
  - Password strength (min 8 znaków, 1 cyfra, 1 wielka litera)
  - Image file type/size (5MB max, JPG/PNG/WebP)
  - Weight value (positive float, max 2 decimal places)
  - Date ranges (start date not in past)
- Consistent error responses (JSON format)

#### Bezpieczeństwo

##### CORS
- Whitelist frontend origins (Vercel domains)
- Credentials allowed (cookies)

##### SQL Injection
- Exposed ORM używa prepared statements (automatic protection)

##### XSS Prevention
- Content Security Policy headers
- Output encoding (handled by frontend)

##### Data encryption at rest
- Postgres with encryption (managed service: AWS RDS, Supabase)
- S3 server-side encryption (AES-256)

##### Secrets management
- Environment variables
- `.env` file (not committed to git)
- Production: AWS Secrets Manager lub Railway/Render built-in secrets

#### Logging
- **Logback** - structured logging
- **Log levels:**
  - INFO: request/response (excluding sensitive data)
  - WARN: validation errors, business logic warnings
  - ERROR: exceptions, system errors
- Log rotation (daily, 30 days retention)
- JSON format dla łatwego parsowania (future monitoring integration)

#### Testing
- **Kotest** - test framework (BDD style)
- **Test Containers** - PostgreSQL dla integration tests
- **MockK** - mocking library
- **Test coverage:**
  - Unit tests: services, validation logic
  - Integration tests: repositories, database operations
  - E2E tests: critical flows (auth, post creation, weight entry)

#### Deployment
- **Docker** - containerization
- **Multi-stage build** (builder + runtime image)
- **Docker Compose** dla local development (app + postgres + s3-mock)
- **Production:**
  - Railway, Render lub AWS ECS
  - Postgres managed service (Railway/Render DB lub AWS RDS)
  - S3 dla file storage
  - Environment-based configuration

## CI/CD
**Technology:** GitHub Actions



