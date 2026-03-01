---
name: write-integration-tests
description: "Create integration tests that verify multiple components work together correctly, especially API endpoints and database interactions."
---

## Purpose
Create integration tests that verify multiple components work together correctly, especially API endpoints and database interactions.

## When to Use
- New API endpoints have been implemented
- Database queries or transactions need verification
- Service-to-service interactions need testing
- Authentication/authorization flows need validation

## Input
- API endpoints or service interfaces to test
- Expected request/response contracts
- Database state requirements (seed data)
- Authentication requirements for protected endpoints

## Steps
1. **Setup test environment** — Configure test database, test server, environment variables
2. **Create seed data** — Prepare database state needed for tests
3. **Write API tests** — Test each endpoint with valid and invalid inputs:
   - Correct HTTP method and path
   - Valid request body → expected response and status code
   - Invalid request body → 400 with validation errors
   - Missing auth → 401
   - Insufficient permissions → 403
   - Non-existent resource → 404
4. **Test data flow** — Verify data is correctly persisted and retrieved
5. **Test transactions** — Verify rollback on failure
6. **Cleanup** — Reset database state after each test or test suite
7. **Run and verify** — All tests pass in isolation and in sequence

## Output
- Integration test files
- Test seed data / fixtures
- Test environment configuration
- Test database setup/teardown scripts

## Quality Checklist
- [ ] Tests use a dedicated test database (never production)
- [ ] Each test starts with a known database state
- [ ] Tests clean up after themselves
- [ ] All API status codes are verified (not just 200)
- [ ] Response body structure is validated against expected schema
- [ ] Auth flows are tested (valid token, expired token, no token)
- [ ] Tests can run in CI without external dependencies
