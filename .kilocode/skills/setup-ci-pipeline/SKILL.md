---
name: setup-ci-pipeline
description: "Create a continuous integration pipeline that automatically builds, tests, and validates code on every push."
---

## Purpose
Create a continuous integration pipeline that automatically builds, tests, and validates code on every push.

## When to Use
- Setting up CI for a new project
- Adding new stages to an existing pipeline (linting, security scanning)
- Migrating CI between platforms

## Input
- CI platform (GitHub Actions, GitLab CI, Jenkins)
- Build and test commands
- Required environment variables and secrets
- Branch strategy (which branches trigger CI)

## Steps
1. **Define triggers** — Run on push to main/develop, on pull requests
2. **Setup environment** — Define runtime (Node.js version, Java version, Go version)
3. **Add caching** — Cache dependencies (node_modules, .m2, go mod cache)
4. **Add stages**:
   - **Install** — Install dependencies
   - **Lint** — Run linter (ESLint, golangci-lint, checkstyle)
   - **Build** — Compile/build the application
   - **Test** — Run unit and integration tests
   - **Coverage** — Generate and report test coverage
   - **Security** — Run dependency vulnerability scan
5. **Add artifacts** — Save build outputs, test reports, coverage reports
6. **Add notifications** — Notify on failure (Slack, email, PR comment)
7. **Add branch protection** — Require CI pass before merge

## Output
- CI configuration file (`.github/workflows/ci.yml`, `.gitlab-ci.yml`, etc.)
- Documentation for CI setup and required secrets

## Quality Checklist
- [ ] Pipeline runs on all PRs and pushes to main branches
- [ ] Dependencies are cached for faster builds
- [ ] All stages fail fast (lint before build, build before test)
- [ ] Test results and coverage are reported
- [ ] Secrets are stored in CI platform, not in config files
- [ ] Pipeline completes in <10 minutes for typical changes
- [ ] Branch protection requires CI pass
