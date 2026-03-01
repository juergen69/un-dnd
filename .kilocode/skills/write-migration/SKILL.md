---
name: write-migration
description: "Create a versioned database migration with both up (apply) and down (rollback) operations."
---

## Purpose
Create a versioned database migration with both up (apply) and down (rollback) operations.

## When to Use
- A new table or column needs to be added
- An existing schema needs to be modified
- An index or constraint needs to be added or removed
- Data needs to be transformed as part of a schema change

## Input
- Schema change description
- Current schema state
- Migration tool in use (Flyway, Liquibase, Prisma Migrate, golang-migrate, etc.)
- Whether data migration is needed alongside schema migration

## Steps
1. **Create migration file** — Follow the tool's naming convention (e.g., `V001__description.sql`, `001_description.up.sql`)
2. **Write UP migration** — SQL or ORM commands to apply the change
3. **Write DOWN migration** — SQL or ORM commands to reverse the change
4. **Handle data migration** — If existing data needs transformation, include data migration steps
5. **Test forward** — Verify the UP migration applies cleanly on a fresh database
6. **Test rollback** — Verify the DOWN migration cleanly reverses the change
7. **Test idempotency** — Ensure migration doesn't fail if partially applied (use IF EXISTS/IF NOT EXISTS)

## Output
- Migration file(s) (up and down)
- Data migration script (if needed)
- Updated schema documentation

## Quality Checklist
- [ ] UP migration applies cleanly on current schema
- [ ] DOWN migration fully reverses the UP migration
- [ ] Migration is idempotent (safe to retry)
- [ ] Large table alterations consider locking implications
- [ ] Data migrations handle NULL values and edge cases
- [ ] Migration file follows project naming convention
- [ ] Schema documentation is updated to reflect changes
