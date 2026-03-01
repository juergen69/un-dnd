---
name: manage-api-versioning
description: "Implement and manage API versioning to support backward-compatible evolution of APIs while maintaining existing client contracts."
---

## Purpose
Implement and manage API versioning to support backward-compatible evolution of APIs while maintaining existing client contracts.

## When to Use
- Introducing breaking changes to an existing API
- Setting up versioning strategy for a new API
- Migrating clients from one API version to another
- Deprecating an old API version

## Input
- Current API contract (routes, request/response schemas)
- Proposed changes and which are breaking vs. non-breaking
- Versioning strategy preference (URL path, header, query parameter)
- Client migration timeline and constraints

## Steps
1. **Classify changes** — Categorize each change as breaking or non-breaking. Breaking: removing fields, changing types, removing endpoints. Non-breaking: adding optional fields, adding endpoints
2. **Choose versioning strategy** — Select approach: URL path (`/api/v2/`), custom header (`API-Version: 2`), or content negotiation (`Accept: application/vnd.api.v2+json`). URL path is recommended for simplicity
3. **Design version routing** — Implement version-aware routing that directs requests to the correct handler based on version
4. **Implement new version** — Create the new API version handlers, reusing shared logic where possible
5. **Maintain backward compatibility** — Keep the old version functional; use adapter patterns to map between versions if they share underlying logic
6. **Add deprecation headers** — Return `Deprecation` and `Sunset` headers on old versions with dates and migration links
7. **Update API documentation** — Document both versions, clearly marking deprecated endpoints and migration guides
8. **Create migration guide** — Write a client-facing guide: what changed, how to migrate, timeline for old version sunset
9. **Add version tests** — Ensure both old and new versions have test coverage; verify old version still works
10. **Plan sunset** — Set a deprecation date, communicate to clients, monitor old version usage

## Output
- Version-aware routing implementation
- New version API handlers
- Deprecation headers on old version
- Migration guide document
- Updated API documentation for both versions
- Test coverage for both versions

## Quality Checklist
- [ ] Versioning strategy is consistent across all endpoints
- [ ] Old version continues to work unchanged
- [ ] New version is fully tested
- [ ] Deprecation headers include sunset date and documentation link
- [ ] Migration guide is clear and actionable
- [ ] Shared logic between versions is not duplicated
- [ ] API documentation covers both versions
- [ ] Breaking changes are clearly documented
