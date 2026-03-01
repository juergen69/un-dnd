---
name: review-auth-implementation
description: "Conduct a security review of authentication and authorization implementation."
---

## Purpose
Conduct a security review of authentication and authorization implementation.

## When to Use
- After auth features are implemented (login, signup, password reset)
- Before deploying auth changes to production
- During periodic security audits

## Input
- Auth implementation code (controllers, middleware, services)
- Auth configuration (JWT settings, session config, OAuth config)
- Token storage approach (cookies, localStorage, memory)

## Steps
1. **Review password handling**:
   - Passwords hashed with bcrypt/argon2 (cost factor ≥10)
   - No plaintext passwords in logs, responses, or database
   - Password complexity requirements enforced
2. **Review token management**:
   - JWT secret is strong and stored securely
   - Token expiration is set (access: 15-60min, refresh: 7-30 days)
   - Refresh tokens are rotated on use
   - Tokens don't contain sensitive data
3. **Review session security**:
   - Session IDs are cryptographically random
   - Sessions expire after inactivity
   - Session fixation is prevented
4. **Review authorization**:
   - Every protected endpoint checks permissions
   - No authorization bypass through parameter manipulation
   - Resource ownership is verified (users can't access others' data)
5. **Review error handling**:
   - Auth errors don't reveal whether user exists
   - Failed login attempts are rate-limited
   - Account lockout after repeated failures
6. **Review transport security**:
   - Auth endpoints require HTTPS
   - Cookies have Secure, HttpOnly, SameSite flags

## Output
- Security review report with findings
- Severity ratings (critical, high, medium, low)
- Remediation recommendations
- Remediation task files in `tasks/todo/` (if issues found)

## Quality Checklist
- [ ] Password storage uses bcrypt/argon2 with appropriate cost
- [ ] No plaintext credentials in logs or responses
- [ ] Tokens expire and are properly validated
- [ ] Rate limiting on login and token endpoints
- [ ] Authorization checks on every protected endpoint
- [ ] Cookies have security flags set
- [ ] Error messages don't leak user existence
