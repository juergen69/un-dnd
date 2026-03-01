---
name: generate-api-docs
description: "Create or update API documentation from code annotations or manual specification."
---

## Purpose
Create or update API documentation from code annotations or manual specification.

## When to Use
- New API endpoints have been implemented
- Existing endpoints have been modified
- API documentation is missing or outdated
- Onboarding new developers or API consumers

## Input
- API source code with route definitions
- Request/response schemas
- Authentication requirements
- Existing OpenAPI spec (if updating)

## Steps
1. **Inventory endpoints** — List all API routes with methods, paths, and descriptions
2. **Document each endpoint**:
   - HTTP method and path
   - Description and purpose
   - Request parameters (path, query, header)
   - Request body schema with examples
   - Response schemas for each status code (200, 400, 401, 404, 500)
   - Authentication requirements
3. **Create OpenAPI spec** — Write or update `openapi.yaml` / `openapi.json`
4. **Add examples** — Include realistic request/response examples for each endpoint
5. **Document error responses** — Standard error format with all possible error codes
6. **Add authentication section** — Document how to authenticate (Bearer token, API key, etc.)
7. **Validate spec** — Run OpenAPI linter to verify spec correctness
8. **Setup viewer** — Configure Swagger UI, Redoc, or similar for browsing

## Output
- OpenAPI specification file (`openapi.yaml`)
- API documentation page or configuration
- Example request/response collections (Postman, Bruno, etc.)

## Quality Checklist
- [ ] Every endpoint is documented
- [ ] Request and response schemas are complete with types
- [ ] Examples are realistic and valid
- [ ] Error responses are documented for each endpoint
- [ ] Authentication is clearly documented
- [ ] OpenAPI spec passes validation
- [ ] Documentation matches current implementation
