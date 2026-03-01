---
name: write-e2e-tests
description: "Create end-to-end tests that verify complete user flows through the application from the browser/client perspective."
---

## Purpose
Create end-to-end tests that verify complete user flows through the application from the browser/client perspective.

## When to Use
- Critical user journeys need automated verification (login, checkout, signup)
- A feature spans multiple pages or components
- Regression testing for release candidates

## Input
- User flow description (step-by-step user actions)
- Expected UI states and outcomes
- Test data requirements
- E2E framework in use (Playwright, Cypress)

## Steps
1. **Map user journey** — List each step the user takes from start to finish
2. **Setup test data** — Seed database or use API to create required state
3. **Write page objects** (optional) — Create reusable selectors and actions for each page
4. **Implement test steps**:
   - Navigate to starting page
   - Interact with UI elements (click, type, select)
   - Wait for expected state changes (loading, navigation, data display)
   - Assert visible outcomes (text, elements, URLs)
5. **Add assertions at each step** — Don't just assert the final state
6. **Handle async operations** — Use proper waits (not arbitrary timeouts)
7. **Add screenshot on failure** — Configure automatic screenshots for debugging
8. **Test across viewports** — Run on desktop and mobile viewport sizes

## Output
- E2E test files
- Page object files (if using page object pattern)
- Test data setup scripts
- CI configuration for running E2E tests

## Quality Checklist
- [ ] Tests cover the complete user journey from start to finish
- [ ] No arbitrary `sleep()` or fixed timeouts — use proper waits
- [ ] Tests are independent and can run in any order
- [ ] Failed tests produce screenshots for debugging
- [ ] Tests work on both desktop and mobile viewports
- [ ] Test data is created and cleaned up programmatically
- [ ] Tests run successfully in CI headless mode
