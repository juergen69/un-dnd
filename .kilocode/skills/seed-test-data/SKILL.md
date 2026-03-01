---
name: seed-test-data
description: "Create and manage seed data, fixtures, and factories for development and testing environments."
---

## Purpose
Create and manage seed data, fixtures, and factories for development and testing environments.

## When to Use
- Setting up a new development environment that needs realistic data
- Writing integration or e2e tests that require pre-populated data
- Creating demo environments for stakeholder review
- Resetting a development database to a known state

## Input
- Database schema (tables, relationships, constraints)
- Data requirements: which entities, how many records, relationships between them
- Environment target: development, testing, or demo
- Constraints: unique fields, foreign keys, enum values

## Steps
1. **Analyze schema** — Review the database schema to understand entities, relationships, and constraints
2. **Identify seed categories** — Classify data needs: reference data (static), sample data (realistic), test fixtures (specific scenarios)
3. **Design data factories** — Create factory functions that generate valid entity instances with sensible defaults and override capability
4. **Create reference data seeds** — Seed static/lookup data: roles, categories, statuses, feature flags
5. **Create sample data seeds** — Generate realistic-looking data using patterns (not random gibberish): real names, valid emails, plausible dates
6. **Create test fixtures** — Build specific scenarios for test cases: user with expired subscription, order with multiple items, etc.
7. **Handle relationships** — Ensure foreign keys are resolved in correct order; parent records before children
8. **Add idempotency** — Seeds should be safe to run multiple times (upsert or check-before-insert)
9. **Create seed runner** — Provide a script or command to execute seeds: `npm run seed`, `make seed`, etc.
10. **Document usage** — Describe how to run seeds, reset data, and extend with new fixtures

## Output
- Seed files or factory functions
- Seed runner script or command
- Documentation on how to use and extend seeds
- Test fixture files for specific test scenarios

## Quality Checklist
- [ ] Seeds respect all database constraints (foreign keys, unique, not null)
- [ ] Seed data looks realistic (not "test123" or "asdf")
- [ ] Seeds are idempotent — safe to run multiple times
- [ ] Relationship order is correct (parents before children)
- [ ] Sensitive fields use fake data (no real PII)
- [ ] Seed runner command is documented
- [ ] Factories support overrides for test-specific customization
- [ ] Cleanup/reset mechanism is provided
