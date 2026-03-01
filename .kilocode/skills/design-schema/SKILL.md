---
name: design-schema
description: "Design a database schema for a feature or domain, including tables, relationships, constraints, and indexes."
---

## Purpose
Design a database schema for a feature or domain, including tables, relationships, constraints, and indexes.

## When to Use
- Starting a new feature that requires data persistence
- Modeling a new domain or bounded context
- Refactoring an existing schema for performance or clarity

## Input
- Domain requirements (entities, relationships, business rules)
- Expected query patterns (what data is read most often)
- Scale expectations (row counts, query frequency)
- Database engine (PostgreSQL, MySQL, MongoDB, etc.)

## Steps
1. **Identify entities** — List all domain objects that need persistence
2. **Define attributes** — For each entity, list columns with types and constraints
3. **Map relationships** — Define foreign keys: one-to-one, one-to-many, many-to-many
4. **Add standard columns** — `id` (primary key), `created_at`, `updated_at`, `deleted_at` (soft delete)
5. **Apply normalization** — Normalize to 3NF; document any intentional denormalization
6. **Define constraints** — NOT NULL, UNIQUE, CHECK, foreign key constraints
7. **Design indexes** — Index foreign keys, frequently filtered columns, and unique constraints
8. **Create ERD** — Document the schema with a Mermaid entity-relationship diagram

## Output
- SQL DDL statements or ORM schema definition
- Entity-relationship diagram (Mermaid)
- Column documentation (purpose, constraints, examples)
- Index justification notes

## Quality Checklist
- [ ] All tables have a primary key
- [ ] All tables have `created_at` and `updated_at` timestamps
- [ ] Foreign keys have corresponding indexes
- [ ] Column types are appropriate (not oversized)
- [ ] NOT NULL is applied where data is required
- [ ] Naming is consistent: snake_case, plural table names
- [ ] Soft delete (`deleted_at`) is used instead of hard delete
- [ ] ERD diagram is included in documentation
