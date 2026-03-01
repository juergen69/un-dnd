---
name: setup-error-handling
description: "Implement a consistent, centralized error handling strategy for the application."
---

## Purpose
Implement a consistent, centralized error handling strategy for the application.

## When to Use
- Starting a new backend project
- Standardizing error responses across an existing API
- Adding structured error logging

## Input
- Application framework (Express, Fastify, Spring Boot, Gin, etc.)
- Error categories needed (validation, auth, not found, conflict, internal)
- Logging requirements

## Steps
1. **Define error classes** — Create typed error classes for each category:
   - `ValidationError` (400) — Invalid input
   - `AuthenticationError` (401) — Not authenticated
   - `AuthorizationError` (403) — Not authorized
   - `NotFoundError` (404) — Resource not found
   - `ConflictError` (409) — Resource conflict
   - `InternalError` (500) — Unexpected server error
2. **Create error response format** — Standardize: `{ error: string, code: string, statusCode: number, details?: any }`
3. **Implement global error handler** — Catch all unhandled errors at the framework level
4. **Map errors to responses** — Convert error classes to appropriate HTTP responses
5. **Add error logging** — Log errors with stack traces, request context, and correlation IDs
6. **Handle async errors** — Ensure async/promise rejections are caught
7. **Add not-found handler** — Return 404 for undefined routes
8. **Sanitize error output** — Never expose stack traces or internal details in production

## Output
- Error class definitions
- Global error handling middleware
- Error response formatting utility
- Error logging integration

## Quality Checklist
- [ ] All error responses follow the same JSON structure
- [ ] Stack traces are logged but never sent to clients in production
- [ ] Async errors are properly caught (no unhandled promise rejections)
- [ ] 404 handler exists for undefined routes
- [ ] Validation errors include field-level details
- [ ] Error logs include correlation ID and request context
- [ ] Error classes are reusable across the codebase
