---
name: run-static-analysis
description: "Run static code analysis tools to automatically detect code quality issues, bugs, security vulnerabilities, and maintainability problems before they reach production."
---

## Purpose
Run static code analysis tools to automatically detect code quality issues, bugs, security vulnerabilities, and maintainability problems before they reach production.

## When to Use
- After completing code implementation tasks
- Before merging feature branches
- As part of the QA testing phase
- When refactoring existing code
- To establish a baseline for code quality metrics

## Input
- Codebase directory or specific files to analyze
- Configuration files for analysis tools (sonar-project.properties, .eslintrc, etc.)
- Quality gates or thresholds (e.g., no critical issues, coverage >80%)
- Context on what changed and why

## Steps
1. **Setup tools** — Ensure analysis tools are installed and configured (SonarQube Scanner, ESLint, etc.)
2. **Configure project** — Verify or create configuration files for the tools
3. **Run analysis** — Execute the static analysis tools on the codebase
4. **Review results** — Examine the output for issues, warnings, and metrics
5. **Categorize findings** — Classify issues by severity (critical, major, minor, info)
6. **Generate report** — Create a summary report with findings and recommendations
7. **Fail/pass decision** — Determine if the code meets quality standards

## Output
- Static analysis report with issue counts and severities
- Detailed list of findings with file locations and descriptions
- Quality metrics (complexity, duplication, coverage if available)
- Pass/fail recommendation based on quality gates
- Suggested fixes or remediation steps for critical issues

## Tools and Configuration
- **SonarQube**: For comprehensive code quality analysis
  - Requires sonar-project.properties in project root
  - Analyzes complexity, bugs, vulnerabilities, code smells
- **ESLint**: For JavaScript linting
  - Configuration via .eslintrc.js or package.json
  - Focuses on code style and potential errors
- **Other tools**: Prettier for formatting, TypeScript compiler for type checking

## Quality Gates
- No critical or blocker issues
- Complexity: Cyclomatic complexity < 10 per function
- Duplication: < 3% code duplication
- Maintainability: Grade A or B
- Security: No high-severity vulnerabilities

## Guidelines
- Run static analysis early and often in the development cycle
- Address critical issues immediately
- Use automated tools to supplement manual code reviews
- Configure tools to match project coding standards
- Integrate with CI/CD pipelines for automated quality checks
