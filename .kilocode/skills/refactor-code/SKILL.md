---
name: refactor-code
description: "Restructure existing backend code to improve readability, maintainability, and performance without changing external behavior."
---

## Purpose
Restructure existing backend code to improve readability, maintainability, and performance without changing external behavior.

## When to Use
- Code has grown too complex or tightly coupled
- Duplicated logic needs to be consolidated
- A module needs to be split or reorganized
- Technical debt has been identified in a code review
- Preparing code for a new feature that requires structural changes

## Input
- Files or modules to refactor
- Reason for refactoring (complexity, duplication, coupling, etc.)
- Constraints: what must not change (public API, database schema, etc.)
- Existing test coverage (to ensure behavior is preserved)

## Steps
1. **Identify scope** — List the files, classes, and functions to be refactored
2. **Verify test coverage** — Ensure existing tests cover the behavior being preserved; write tests first if missing
3. **Catalog code smells** — Document: long methods, God classes, feature envy, primitive obsession, shotgun surgery
4. **Plan refactoring** — Choose specific refactoring patterns: Extract Method, Extract Class, Move Method, Replace Conditional with Polymorphism, Introduce Parameter Object
5. **Apply changes incrementally** — Make one refactoring at a time; verify tests pass after each change
6. **Update imports and references** — Ensure all callers of moved/renamed code are updated
7. **Remove dead code** — Delete unused functions, imports, and variables
8. **Verify behavior** — Run the full test suite to confirm no regressions
9. **Update documentation** — Update inline comments, JSDoc/GoDoc, and any affected READMEs
10. **Document decisions** — Note significant structural decisions in task completion notes

## Output
- Refactored source files
- Updated or new tests (if coverage gaps were found)
- List of changes made and rationale
- Confirmation that all existing tests pass

## Quality Checklist
- [ ] External behavior is unchanged (all existing tests pass)
- [ ] No new code smells introduced
- [ ] Cyclomatic complexity is reduced or unchanged
- [ ] No dead code remains
- [ ] Naming is clear and consistent
- [ ] Module boundaries are well-defined
- [ ] Changes are documented in task completion notes
