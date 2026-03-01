---
name: optimize-query
description: "Analyze and optimize a slow or inefficient database query for better performance."
---

## Purpose
Analyze and optimize a slow or inefficient database query for better performance.

## When to Use
- A query is identified as slow (>100ms for simple queries, >500ms for complex)
- EXPLAIN shows full table scans on large tables
- Application performance profiling points to database bottlenecks
- N+1 query patterns are detected

## Input
- The slow query (SQL or ORM code)
- Current EXPLAIN/EXPLAIN ANALYZE output
- Table sizes and row counts
- Expected query frequency (queries per second)
- Current indexes on involved tables

## Steps
1. **Get baseline** — Run EXPLAIN ANALYZE on the current query; record execution time
2. **Identify bottlenecks** — Look for: sequential scans, nested loops on large tables, missing indexes, unnecessary joins
3. **Check indexes** — Verify indexes exist for WHERE, JOIN, and ORDER BY columns
4. **Optimize query structure**:
   - Replace `SELECT *` with specific columns
   - Add missing WHERE clauses to reduce result set
   - Use JOINs instead of subqueries (or vice versa, depending on the optimizer)
   - Add LIMIT for paginated queries
5. **Add indexes** — Create indexes for identified bottlenecks; prefer composite indexes for multi-column filters
6. **Consider denormalization** — If joins are the bottleneck, consider materialized views or denormalized columns
7. **Measure improvement** — Run EXPLAIN ANALYZE again; compare execution time
8. **Document** — Record the optimization rationale and before/after metrics

## Output
- Optimized query
- New index definitions (if needed)
- EXPLAIN ANALYZE before/after comparison
- Documentation of changes and rationale

## Quality Checklist
- [ ] EXPLAIN ANALYZE shows improvement over baseline
- [ ] No unnecessary full table scans remain
- [ ] New indexes don't negatively impact write performance
- [ ] Query returns correct results (verified against original)
- [ ] Optimization is documented with before/after metrics
- [ ] Edge cases tested (empty tables, NULL values, large result sets)
