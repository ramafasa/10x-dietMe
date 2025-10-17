# Database Planning Summary - DietMe MVP

## Decisions

### Round 1: Core Schema Decisions (Questions 1-10)

1. **User Roles Storage**: Use ENUM or VARCHAR column in Users table with values 'DIETITIAN' and 'CLIENT'. Add CHECK constraint: `CHECK (role IN ('DIETITIAN', 'CLIENT'))`.

2. **Users-Groups Relationship**: Create junction table `group_member` with columns: id, user_id, group_id, joined_at, left_at (nullable). Add unique constraint on (user_id, group_id) and partial unique index: `CREATE UNIQUE INDEX idx_active_membership ON group_member(user_id) WHERE left_at IS NULL` to ensure client can only be in one active group.

3. **Image Storage in Posts**: Use denormalized columns in Post table: image_s3_key (base path), image_alt_text, image_uploaded_at. Generate variant names following pattern: `/uploads/{userId}/posts/{postId}/thumbnail.webp`. No separate PostImages table needed in MVP.

4. **Post Status Management**: Add status ENUM ('DRAFT', 'SCHEDULED', 'PUBLISHED') with columns: created_at (NOT NULL), scheduled_published_at (nullable), published_at (nullable). Add constraints to ensure data integrity based on status.

5. **Pinned Posts**: Add `is_pinned BOOLEAN DEFAULT FALSE` column in Post table with partial unique index: `CREATE UNIQUE INDEX idx_one_pinned_per_group ON post(group_id) WHERE is_pinned = TRUE`. Add pinned_at column for audit trail.

6. **Performance Indexes**: Create the following critical indexes:
   - Posts: `(group_id, status, published_at DESC)` - for chronological feed
   - WeightEntries: `(user_id, entry_date DESC)` - for client weight chart
   - WeightEntries: `(group_id, entry_date)` - for dietitian aggregate view
   - Likes: `(post_id, user_id)` - for checking if user liked post
   - Comments: `(post_id, created_at)` - for chronological comment list

7. **Weight Entry Storage**: In weight_entry table use: entry_date (DATE NOT NULL), weight_kg (NUMERIC(5,2) NOT NULL - range 0-999.99kg), note (TEXT nullable with 500 char constraint), created_at (TIMESTAMPTZ), updated_at (TIMESTAMPTZ nullable). Late entry logic (until 02:00 next day) handled in application layer (Kotlin). Add unique constraint `(user_id, entry_date)`. User timezone stored in Users.timezone (VARCHAR, e.g., 'Europe/Warsaw').

8. **Group Invitations**: Create Invitations table: id, group_id, invitation_token (UUID UNIQUE), created_by_dietitian_id, expires_at (TIMESTAMPTZ NOT NULL), max_uses (INTEGER DEFAULT 1), current_uses (INTEGER DEFAULT 0), created_at. Add index on (invitation_token) and constraint `CHECK (current_uses <= max_uses)`.

9. **Abuse Reporting**: Create comment_report table: id, comment_id, reported_by_user_id, reason (TEXT nullable), status ENUM ('PENDING', 'REVIEWED', 'DISMISSED'), created_at, reviewed_at (nullable), reviewed_by_dietitian_id (nullable). Add unique constraint `(comment_id, reported_by_user_id)` - one user can report comment only once. Index on (status, created_at).

10. **Delete Strategy (GDPR Compliance)**: Use mixed strategy:
    - Users, Groups, Posts: soft delete with deleted_at (TIMESTAMPTZ nullable)
    - Comments: hard delete by dietitian (CASCADE), but add is_deleted BOOLEAN + deleted_at + deleted_by_id before removal
    - WeightEntries: soft delete (member removed from group, but data preserved for reporting)
    - Scheduled job (Quartz) for hard delete after 24 months from deleted_at or group_end_date + 24m
    - Add index on deleted_at for filtering active records

### Round 2: Implementation Details (Questions 11-20)

11. **Audit Columns**: Add to all main tables (User, Group, Post, Comment, Like, WeightEntry): `created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()`, `updated_at TIMESTAMPTZ`. For tables requiring full audit (e.g., WeightEntry), add `edited_at TIMESTAMPTZ`. Use PostgreSQL trigger for automatic updated_at: `UPDATE ... SET updated_at = NOW()`.

12. **Naming Conventions**:
    - **snake_case** for all names (tables, columns, indexes, constraints)
    - **Singular for entity tables** (user, post, group) - Exposed DAO convention
    - **Plural for junction tables** (group_members, comment_reports)
    - Examples: `user.email_address`, `post.published_at`, `weight_entry.entry_date`

13. **Baseline/Final Weight Calculation**: Implement in application layer (Kotlin), not database. This is business logic (KPI calculation), not data integrity constraint. Calculate dynamically in service layer - do not store as denormalized column. Cache in Redis if performance becomes an issue (unlikely with 200 clients in MVP).

14. **Compliance % Calculation**: Implement in application layer (Kotlin), not as PostgreSQL VIEW. With max 200 clients and 20 groups, calculations will be fast without materialization.

15. **Foreign Key Cascading**:
    - Groups -> Posts: **ON DELETE RESTRICT** (groups should not be hard-deleted; use soft delete with archived_at if needed)
    - Users -> Posts: **ON DELETE SET NULL** (post can remain but author_id = NULL if user deleted)

16. **Validation Strategy**: Use defense in depth - both layers:
    - Application (Kotlin): `@field:Size(max = 500)` on DTO + custom validation
    - Database: `CHECK (LENGTH(note) <= 500)` in weight_entry table
    - Column type: `note TEXT` (not VARCHAR(500) - TEXT is more flexible in Postgres with no performance penalty)
    - Same for Comments: `CHECK (LENGTH(content) <= 1000)`

17. **Password Storage**: Use `password_hash VARCHAR(60) NOT NULL` (BCrypt generates fixed 60-char hash including salt and cost factor). Add additional columns:
    - `password_changed_at TIMESTAMPTZ` - for US-050 audit trail
    - `last_login_at TIMESTAMPTZ` - for future analytics (deferred from MVP, but cheap column)

18. **Password Reset Tokens**: Create separate `password_reset_token` table:
    - Columns: id (UUID PRIMARY KEY), user_id (FK with CASCADE), token (VARCHAR(64) UNIQUE - SHA-256 hash), expires_at (TIMESTAMPTZ NOT NULL), used_at (TIMESTAMPTZ nullable), created_at
    - Index: `CREATE INDEX idx_password_reset_token_token ON password_reset_token(token) WHERE used_at IS NULL`
    - Benefits: multiple active tokens possible, easy cleanup by Quartz job, no NULL columns in Users

19. **Post Tags**: Use `tags TEXT[]` column in Post table. Benefits:
    - Simpler implementation (no JOINs)
    - PostgreSQL GIN index support: `CREATE INDEX idx_post_tags ON post USING GIN(tags)`
    - Sufficient for simple tagging (US-020: no filtering for clients in MVP)
    - Add constraint: `CHECK (array_length(tags, 1) <= 10)` - limit 10 tags per post

20. **Data Separation per Group**: Implement in **application layer (Kotlin)**, not PostgreSQL RLS (Row Level Security). Reasons:
    - RLS adds complexity (requires SET ROLE, policies per table, harder debugging)
    - Performance overhead (RLS policies checked on every query)
    - Spring Security + Exposed ORM sufficient - filter queries by group_id or user_id in repository layer
    - Better testability without RLS
    - Consider RLS in future for multi-tenancy (outside MVP scope)

## Matched Recommendations

All 20 recommendations were accepted by the user. Key highlights:

### Data Modeling
- ENUM types for constrained values (user roles, post status, report status)
- Junction tables for many-to-many relationships with audit columns
- Denormalization where appropriate (image paths in Posts, tags as TEXT[])
- Partial unique indexes for business constraints (one active group per client, one pinned post per group)

### Performance
- Strategic indexing based on query patterns (feed, charts, aggregations)
- Avoid over-engineering (no materialized views, no separate tag tables in MVP)
- Application-layer caching strategy for complex calculations

### Security & Compliance
- Soft delete strategy with scheduled cleanup for GDPR (24-month retention)
- Defense-in-depth validation (application + database constraints)
- Separate token table for password resets with expiration
- Application-layer data separation (no RLS in MVP)

### Audit & Maintainability
- Standard audit columns (created_at, updated_at, edited_at where needed)
- Consistent naming conventions (snake_case, singular entity tables)
- Clear foreign key cascading rules (RESTRICT for groups, SET NULL for users)

## Database Planning Summary

### Main Schema Requirements

**Core Entities:**
1. **user** - Authentication, profile, role (DIETITIAN/CLIENT), timezone, soft delete support
2. **group** - Mentoring groups with start/end dates, created by dietitian, soft delete support
3. **group_member** - Junction table with temporal tracking (joined_at, left_at) and active membership constraint
4. **post** - Content (text/image/video), status lifecycle (DRAFT/SCHEDULED/PUBLISHED), group association, pinning, tags
5. **like** - Simple many-to-many between users and posts
6. **comment** - Text content with soft delete support, abuse reporting capability
7. **comment_report** - Abuse reporting with status tracking and unique reporter constraint
8. **weight_entry** - Daily weight logs with notes, unique per user per date
9. **invitation** - Token-based group invitations with expiration and usage limits
10. **password_reset_token** - Temporary tokens for password reset flow

### Key Relationships

```
user 1--* group (via created_by_dietitian_id)
user *--* group (via group_member junction table with temporal constraints)
group 1--* post
user 1--* post (author, SET NULL on delete)
user *--* post (via like junction table)
post 1--* comment
user 1--* comment (author, soft delete cascade)
comment 1--* comment_report
user 1--* comment_report (reporter)
user 1--* weight_entry
group 1--* invitation
user 1--* password_reset_token (CASCADE on delete)
```

### Important Security & Scalability Considerations

**Security:**
- BCrypt password hashing (60-char VARCHAR)
- Token-based password reset with expiration (24h per PRD US-003)
- Data separation enforced in application layer (Spring Security + Exposed ORM)
- Soft delete for audit trail and GDPR compliance
- CHECK constraints for data validation at database level
- Defense-in-depth validation (application + database)

**Scalability (MVP Constraints):**
- Max 20 groups, 200 clients total (per PRD section 1)
- No need for partitioning or sharding in MVP
- Strategic indexing sufficient for performance targets (<3s page load, <2s chart render)
- Application-layer caching for complex calculations (baseline/final weight, compliance %)
- No materialized views needed (query performance adequate for MVP scale)

**GDPR & Data Retention:**
- 24-month retention period from group end date or account deletion
- Scheduled Quartz job for automatic hard delete after retention period
- Soft delete preserves referential integrity during retention period
- Export/delete on request capabilities (per PRD section 3.8)

**Performance Targets (PRD section 3.9):**
- Page load: <3s (p95)
- Chart rendering: <2s (p95)
- Image upload (5MB): <10s (p95)
- Availability: 99.5% (≤3.65 days downtime/year)

### Schema Implementation Notes

**Exposed ORM Integration:**
- Use singular table names in Exposed objects: `object Users : Table("user")`
- snake_case for all database identifiers
- DAO pattern for entities
- DSL queries for complex operations (e.g., compliance calculation, baseline weight)

**Liquibase Migration Strategy:**
- Initial schema in `db/changelog/v1.0.0-initial-schema.sql`
- Separate changesets for: tables, indexes, constraints, triggers
- Use `<rollback>` tags for reversible migrations
- Version format: `v{major}.{minor}.{patch}-{description}`

**Trigger Requirements:**
- `updated_at` auto-update trigger for all tables with this column
- Example: `CREATE TRIGGER set_updated_at BEFORE UPDATE ON user FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();`

**Index Naming Convention:**
- Primary key: `pk_{table}`
- Foreign key: `fk_{table}_{referenced_table}`
- Unique constraint: `uk_{table}_{column(s)}`
- Check constraint: `ck_{table}_{column}_{description}`
- Index: `idx_{table}_{column(s)}_{description}`
- Partial index: `idx_{table}_{column(s)}_where_{condition}`

### Data Types Summary

**Common Types:**
- Primary keys: `BIGSERIAL` (auto-increment 64-bit integer)
- Foreign keys: `BIGINT`
- UUIDs: `UUID` (for tokens)
- Timestamps: `TIMESTAMPTZ` (timezone-aware)
- Dates: `DATE` (for entry_date in weight_entry)
- Text: `TEXT` (for unlimited text), `VARCHAR(n)` for constrained strings
- Booleans: `BOOLEAN`
- Decimals: `NUMERIC(precision, scale)` (e.g., weight_kg as NUMERIC(5,2))
- Arrays: `TEXT[]` (for tags)
- Enums: `VARCHAR` with CHECK constraints or native PostgreSQL ENUM types

