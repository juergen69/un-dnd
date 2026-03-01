---
name: check-owasp-top10
description: "Review application code against the OWASP Top 10 most critical web application security risks."
---

## Purpose
Review application code against the OWASP Top 10 most critical web application security risks.

## When to Use
- Before initial production deployment
- After major feature additions
- During periodic security audits
- When onboarding a new codebase

## Input
- Application source code
- Architecture overview (frontend, backend, database, APIs)
- Authentication and authorization implementation
- Data flow diagrams (if available)

## Steps
Review each OWASP Top 10 category:

1. **A01: Broken Access Control** — Verify authorization on every endpoint; check for IDOR, missing function-level access control
2. **A02: Cryptographic Failures** — Check data encryption at rest and in transit; verify no sensitive data in logs/URLs
3. **A03: Injection** — Check for SQL injection, XSS, command injection; verify parameterized queries and input sanitization
4. **A04: Insecure Design** — Review for missing rate limiting, business logic flaws, missing abuse case handling
5. **A05: Security Misconfiguration** — Check default credentials, unnecessary features enabled, missing security headers, verbose errors
6. **A06: Vulnerable Components** — Run dependency audit (see `skills/security/audit-dependencies.md`)
7. **A07: Authentication Failures** — Review auth implementation (see `skills/security/review-auth-implementation.md`)
8. **A08: Data Integrity Failures** — Check for insecure deserialization, missing integrity checks on updates
9. **A09: Logging & Monitoring Failures** — Verify security events are logged, no sensitive data in logs
10. **A10: Server-Side Request Forgery** — Check for SSRF in URL fetching, webhook handling, file imports

## Output
- OWASP Top 10 audit report with pass/fail per category
- Detailed findings with code references
- Remediation recommendations prioritized by severity
- Remediation task files in `tasks/todo/`

## Quality Checklist
- [ ] All 10 categories are reviewed
- [ ] Findings include specific code references
- [ ] Severity is rated for each finding
- [ ] Remediation steps are actionable
- [ ] Critical findings have task files created for immediate fix
- [ ] Report is stored for compliance/audit trail
