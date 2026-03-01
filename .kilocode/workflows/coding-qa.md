---
description: Code implementation with testing, verification, and SonarQube QA
mode: code
---

# Coding & QA Workflow

Follow this workflow for every implementation task to ensure code quality.

## Step 1: Implement Code

1. Read the task file for requirements, acceptance criteria, and technical details
2. Switch to the appropriate specialist mode (frontend, backend, mobile, database)
3. Implement the feature following the mode's guidelines and referenced skills
4. Write clean, well-documented code following project conventions
5. Handle all error cases and edge cases

## Step 2: Write Tests

1. Switch to **QA mode**
2. Write unit tests for all new business logic (target >80% coverage)
3. Write integration tests for API endpoints and service interactions
4. Write e2e tests for critical user flows (if applicable)
5. Follow the test pyramid: unit (70%) → integration (20%) → e2e (10%)
6. Ensure all tests pass locally

## Step 3: Self-Review

Before requesting QA verification, self-review:

- [ ] Code follows project naming conventions and style
- [ ] All acceptance criteria from the task are met
- [ ] Error handling is comprehensive
- [ ] No hardcoded secrets or environment-specific values
- [ ] No console.log/print debugging statements left in
- [ ] Complex logic has inline documentation
- [ ] All new functions/methods have proper types/signatures
- [ ] No TODO/FIXME comments without linked task IDs

## Step 4: Static Analysis with SonarQube

1. Switch to **QA mode**
2. Use the SonarQube MCP tools to check code quality:

   a. **Find the project**: Use `search_my_sonarqube_projects` to locate the project
   b. **Check quality gate**: Use `get_project_quality_gate_status` to verify the project passes
   c. **Search for issues**: Use `search_sonar_issues_in_projects` with filters:
      - `issueStatuses: ["OPEN"]` — only unresolved issues
      - `severities: ["HIGH", "BLOCKER"]` — focus on critical issues first
   d. **Check security hotspots**: Use `search_security_hotspots` with `status: ["TO_REVIEW"]`
   e. **Analyze specific files**: Use `analyze_code_snippet` for newly created/modified files
   f. **Check coverage**: Use `search_files_by_coverage` to identify files needing more tests

3. Fix all BLOCKER and HIGH severity issues before proceeding
4. Document any accepted MEDIUM/LOW issues with justification

## Step 5: Security Review (if applicable)

If the task involves authentication, authorization, user input, or sensitive data:

1. Switch to **Security mode**
2. Review against OWASP Top 10 using the `check-owasp-top10` skill
3. Audit dependencies for known CVEs using `audit-dependencies` skill
4. Review auth implementation if applicable using `review-auth-implementation` skill
5. Fix all critical and high severity findings

## Step 6: Verification

Final verification before marking the task complete:

- [ ] All unit tests pass
- [ ] All integration tests pass
- [ ] SonarQube quality gate passes (no new BLOCKER/HIGH issues)
- [ ] Security review passed (if applicable)
- [ ] All acceptance criteria from the task file are satisfied
- [ ] Code has been committed with a conventional commit message

## Step 7: Complete Task

1. Fill in the task's **Completion Notes** section with:
   - Summary of what was implemented
   - Files created/modified
   - Key decisions made
   - Test coverage achieved
   - SonarQube status
2. Mark the task file as complete (add `[x]` to the status checkbox)
3. Report completion to the orchestrator

## SonarQube Quick Reference

| Tool | When to Use |
|------|-------------|
| `search_my_sonarqube_projects` | Find project key |
| `get_project_quality_gate_status` | Check overall quality gate |
| `search_sonar_issues_in_projects` | Find bugs, vulnerabilities, code smells |
| `search_security_hotspots` | Find security hotspots |
| `analyze_code_snippet` | Analyze specific file content |
| `search_files_by_coverage` | Find files with low test coverage |
| `get_file_coverage_details` | See line-by-line coverage |
| `get_component_measures` | Get metrics (coverage, complexity, etc.) |
