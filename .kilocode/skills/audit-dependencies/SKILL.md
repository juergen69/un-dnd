---
name: audit-dependencies
description: "Scan project dependencies for known security vulnerabilities and outdated packages."
---

## Purpose
Scan project dependencies for known security vulnerabilities and outdated packages.

## When to Use
- Before a release or deployment
- On a regular schedule (weekly/monthly)
- After adding new dependencies
- When a CVE is announced for a commonly used package

## Input
- Package manifest files (package.json, go.mod, pom.xml, build.gradle)
- Current lock files (package-lock.json, go.sum, etc.)
- Severity threshold (which severity levels to flag)

## Steps
1. **Run vulnerability scan** — Use built-in tools: `npm audit`, `go vuln check`, OWASP Dependency-Check, Snyk
2. **Review findings** — For each vulnerability:
   - Severity (critical, high, medium, low)
   - Affected package and version
   - Fix available? (patched version)
   - Is the vulnerable code path actually used?
3. **Prioritize fixes**:
   - **Critical/High with fix available** → Update immediately
   - **Critical/High without fix** → Evaluate workarounds or alternatives
   - **Medium/Low** → Schedule for next maintenance window
4. **Apply fixes** — Update packages, test for regressions
5. **Document exceptions** — If a vulnerability can't be fixed, document why and the mitigation
6. **Configure automated scanning** — Add to CI pipeline for continuous monitoring

## Output
- Vulnerability report (package, severity, fix status)
- Updated dependency files (if fixes applied)
- Exception documentation for unfixable vulnerabilities
- CI configuration for automated scanning

## Quality Checklist
- [ ] All critical and high vulnerabilities are addressed
- [ ] Updated dependencies don't break existing functionality
- [ ] Lock file is committed after updates
- [ ] Exceptions are documented with justification
- [ ] Automated scanning is configured in CI
- [ ] Direct and transitive dependencies are both scanned
