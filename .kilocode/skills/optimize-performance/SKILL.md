---
name: optimize-performance
description: "Identify and resolve performance bottlenecks in backend services, APIs, and data access patterns."
---

## Purpose
Identify and resolve performance bottlenecks in backend services, APIs, and data access patterns.

## When to Use
- API response times exceed acceptable thresholds
- Database queries are slow or causing high load
- Memory usage is growing unexpectedly
- Throughput needs to scale for higher traffic
- A performance review is required before deployment

## Input
- Service or endpoint to optimize
- Current performance metrics (response time, throughput, error rate) if available
- Performance targets (e.g., p95 < 200ms, 1000 RPS)
- Infrastructure constraints (memory, CPU, database connections)

## Steps
1. **Profile the hot path** — Identify the slowest endpoint or operation using logs, APM traces, or manual timing
2. **Analyze database queries** — Look for N+1 queries, missing indexes, full table scans, unnecessary joins
3. **Check data serialization** — Look for over-fetching (selecting unused columns), large payloads, redundant transformations
4. **Evaluate caching opportunities** — Identify read-heavy, rarely-changing data suitable for caching (in-memory, Redis, HTTP cache headers)
5. **Review connection management** — Check database connection pooling, HTTP client reuse, keep-alive settings
6. **Check async patterns** — Identify blocking operations that could be async; look for sequential calls that could be parallelized
7. **Optimize algorithms** — Review loop complexity, data structure choices, and unnecessary allocations
8. **Add pagination** — Ensure list endpoints use cursor or offset pagination with reasonable defaults
9. **Implement results** — Apply optimizations incrementally; benchmark before and after each change
10. **Document findings** — Record what was slow, why, and what was changed with before/after metrics

## Output
- Optimized source files
- New or updated indexes, queries, or caching layers
- Before/after performance measurements
- Documentation of changes and rationale

## Quality Checklist
- [ ] Root cause of the bottleneck is identified and documented
- [ ] Optimization does not change functional behavior (tests pass)
- [ ] Before/after metrics are recorded
- [ ] No premature optimization — changes target measured bottlenecks
- [ ] Caching includes invalidation strategy where applicable
- [ ] Database changes include migration files if schema is affected
- [ ] No new N+1 queries introduced
- [ ] Pagination is implemented for all list endpoints
