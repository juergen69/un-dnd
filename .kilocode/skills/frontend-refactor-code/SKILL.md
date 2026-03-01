---
name: refactor-code
description: "Restructure existing frontend code to improve component architecture, readability, and maintainability without changing visual behavior."
---

## Purpose
Restructure existing frontend code to improve component architecture, readability, and maintainability without changing visual behavior.

## When to Use
- Components have grown too large or complex
- Shared logic needs to be extracted into hooks or utilities
- State management needs to be reorganized
- CSS/styling has become inconsistent or duplicated
- Preparing the UI layer for a new feature that requires structural changes

## Input
- Components or modules to refactor
- Reason for refactoring (complexity, duplication, poor separation, etc.)
- Constraints: what must not change (public API, visual appearance, behavior)
- Existing test and visual regression coverage

## Steps
1. **Identify scope** — List the components, hooks, and utilities to be refactored
2. **Verify test coverage** — Ensure existing tests (unit, component, visual) cover current behavior; write tests first if missing
3. **Catalog code smells** — Document: oversized components, prop drilling, duplicated markup, mixed concerns, inline styles
4. **Plan refactoring** — Choose patterns: Extract Component, Extract Hook, Lift State Up, Compose Components, Replace Prop Drilling with Context
5. **Apply changes incrementally** — One refactoring at a time; verify tests and visual appearance after each
6. **Update imports and exports** — Ensure all consumers of moved/renamed components are updated
7. **Consolidate styles** — Remove duplicated CSS, extract shared design tokens or utility classes
8. **Remove dead code** — Delete unused components, imports, unreachable branches
9. **Verify behavior** — Run tests and visually inspect affected pages/components
10. **Update documentation** — Update component docs, Storybook stories, and any affected READMEs

## Output
- Refactored component and utility files
- Updated or new tests (if coverage gaps were found)
- List of changes made and rationale
- Confirmation that visual behavior is unchanged

## Quality Checklist
- [ ] Visual behavior is unchanged (tests pass, no visual regressions)
- [ ] Components follow single-responsibility principle
- [ ] No prop drilling deeper than 2 levels without context or state management
- [ ] No duplicated markup or styles
- [ ] No dead code remains
- [ ] Naming is clear and consistent
- [ ] Accessibility is preserved (ARIA attributes, keyboard navigation)
