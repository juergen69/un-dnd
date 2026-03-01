---
name: implement-auth-middleware
description: "Create authentication and authorization middleware for protecting API endpoints."
---

## Purpose
Create authentication and authorization middleware for protecting API endpoints.

## When to Use
- Setting up authentication for a new project
- Adding auth to previously unprotected endpoints
- Implementing role-based access control (RBAC)

## Input
- Authentication strategy (JWT, session, API key, OAuth2)
- Authorization requirements (roles, permissions, resource ownership)
- Token/session storage approach

## Steps
1. **Choose auth strategy** — JWT for stateless APIs, sessions for server-rendered apps, OAuth2 for third-party auth
2. **Create auth middleware** — Extract and validate credentials from request headers/cookies
3. **Implement token validation** — Verify signature, expiration, issuer, audience
4. **Attach user context** — Add authenticated user info to the request context
5. **Create authorization middleware** — Check roles/permissions against the requested resource
6. **Handle auth errors** — Return 401 for unauthenticated, 403 for unauthorized
7. **Add token refresh** (if JWT) — Implement refresh token rotation
8. **Secure sensitive routes** — Apply middleware to protected route groups

## Output
- Authentication middleware function/class
- Authorization middleware (role/permission checking)
- Token utility functions (generate, validate, refresh)
- Auth-related type definitions
- Auth error response handlers

## Quality Checklist
- [ ] Tokens are validated on every protected request
- [ ] Expired tokens return 401 with clear error message
- [ ] Insufficient permissions return 403
- [ ] No sensitive data in JWT payload (no passwords, minimal PII)
- [ ] Refresh tokens are rotated on use (one-time use)
- [ ] Auth errors don't leak implementation details
- [ ] Rate limiting on auth endpoints (login, token refresh)
