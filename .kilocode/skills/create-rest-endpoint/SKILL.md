---
name: create-rest-endpoint
description: "Design and implement a RESTful API endpoint with proper validation, error handling, and documentation."
---

## Purpose
Design and implement a RESTful API endpoint with proper validation, error handling, and documentation.

## When to Use
- A new API endpoint is needed for a feature
- An existing endpoint needs to be refactored or extended
- A CRUD operation needs to be exposed

## Input
- Resource name and HTTP method (GET, POST, PUT, PATCH, DELETE)
- Request/response schema
- Authentication/authorization requirements
- Business logic requirements

## Steps
1. **Define route** — Follow REST conventions: `GET /api/v1/resources`, `POST /api/v1/resources`, `GET /api/v1/resources/:id`
2. **Create request DTO** — Define and validate the request body/params/query schema
3. **Add input validation** — Validate all input at the controller level; return 400 for invalid input
4. **Implement controller** — Parse request, call service layer, format response
5. **Implement service logic** — Business logic in a separate service layer (not in the controller)
6. **Add error handling** — Return structured errors: `{ error: string, code: string, details?: any }`
7. **Add authentication** — Apply auth middleware if endpoint requires it
8. **Add response formatting** — Consistent response envelope: `{ data: T }` for success
9. **Add logging** — Log request received, processing steps, and response status
10. **Add OpenAPI annotations** — Document the endpoint for API docs generation

## Output
- Route/controller file
- Request/response DTO definitions
- Service layer implementation
- Validation schema
- OpenAPI annotations or documentation

## Quality Checklist
- [ ] Correct HTTP method and status codes (201 for creation, 204 for deletion, etc.)
- [ ] All input is validated before processing
- [ ] Errors return structured JSON with appropriate status codes
- [ ] Authentication/authorization is enforced
- [ ] Request and response types are fully defined
- [ ] Endpoint is idempotent where appropriate (PUT, DELETE)
- [ ] Structured logging with correlation ID
