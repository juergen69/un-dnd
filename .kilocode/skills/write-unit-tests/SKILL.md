---
name: write-unit-tests
description: "Create unit tests for isolated functions, methods, or components to verify correctness."
---

## Purpose
Create unit tests for isolated functions, methods, or components to verify correctness.

## When to Use
- New business logic has been implemented
- A bug fix needs regression test coverage
- Code coverage is below the target threshold (>80%)

## Input
- Source file(s) to test
- Function signatures and expected behavior
- Edge cases and boundary conditions to cover
- Testing framework in use (JUnit, Go testing, Vitest, Jest)

## Steps
1. **Identify test cases** — List happy path, edge cases, error cases, and boundary values
2. **Create test file** — Follow naming convention: `*.test.ts`, `*_test.go`, `*Test.java`
3. **Setup test fixtures** — Create mock data, stubs, and test helpers
4. **Write tests using AAA pattern**:
   - **Arrange** — Set up test data and dependencies
   - **Act** — Call the function under test
   - **Assert** — Verify the result matches expectations
5. **Add edge case tests** — Empty inputs, null/undefined, boundary values, large inputs
6. **Add error case tests** — Invalid inputs, thrown exceptions, error returns
7. **Run and verify** — All tests pass; check coverage report

## Output
- Test file(s) with unit tests
- Test fixtures/helpers (if created)
- Coverage report showing tested lines

## Quality Checklist
- [ ] Each test has a descriptive name that reads as a sentence
- [ ] Tests are independent — no shared mutable state
- [ ] Happy path, edge cases, and error cases are covered
- [ ] No test depends on external services or network
- [ ] Mocks are minimal — only mock external dependencies
- [ ] Tests are deterministic (no random, no time-dependent assertions)
- [ ] Coverage meets project threshold (>80% for business logic)
