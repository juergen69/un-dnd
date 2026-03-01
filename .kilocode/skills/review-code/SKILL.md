---
name: review-code
description: "Perform a structured code review to identify quality issues, bugs, security concerns, and adherence to project standards."
---

## Purpose
Perform a structured code review to identify quality issues, bugs, security concerns, and adherence to project standards.

## When to Use
- Before merging a feature branch or PR
- After an agent completes an implementation task
- When reviewing existing code for quality improvements
- As part of a handoff between agents

## Input
- Files or directories to review
- Coding standards and conventions (from agent guidelines or project config)
- Acceptance criteria from the parent task or PRD
- Context on what changed and why

## Steps
1. **Understand context** — Read the task description and acceptance criteria to understand intent
2. **Review structure** — Check file organization, naming conventions, and module boundaries
3. **Check correctness** — Verify logic handles all cases: happy path, edge cases, error paths
4. **Check readability** — Ensure clear naming, appropriate comments, no dead code, no magic numbers
5. **Check maintainability** — Look for DRY violations, God classes, tight coupling, missing abstractions
6. **Check error handling** — Verify errors are caught, logged, and surfaced appropriately
7. **Check security** — Look for input validation gaps, hardcoded secrets, injection risks
8. **Check performance** — Identify N+1 queries, unnecessary allocations, missing pagination
9. **Check test coverage** — Verify tests exist for critical paths and edge cases
10. **Document findings** — Categorize as: critical, major, minor, suggestion

## Output
- Review summary with categorized findings
- Specific file and line references for each issue
- Suggested fixes or improvement patterns
- Pass/fail recommendation with conditions

## Quality Checklist
- [ ] All files in scope have been reviewed
- [ ] Findings are categorized by severity (critical/major/minor/suggestion)
- [ ] Each finding includes a specific file reference and explanation
- [ ] Suggestions include actionable remediation guidance
- [ ] Review covers correctness, readability, maintainability, security, and performance
- [ ] Acceptance criteria from the task/PRD are verified
