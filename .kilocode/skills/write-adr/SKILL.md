---
name: write-adr
description: "Create an Architecture Decision Record to document a significant technical decision."
---

## Purpose
Create an Architecture Decision Record to document a significant technical decision.

## When to Use
- A technology choice is made (database, framework, library)
- An architectural pattern is adopted (microservices, event-driven, etc.)
- A significant trade-off decision is made
- A previous decision is being reversed or superseded

## Input
- The decision or question being addressed
- Context and constraints that influenced the decision
- Alternatives that were considered
- The template at `docs/adr/templates/adr-template.md`

## Steps
1. **Determine next ADR number** — Check existing ADRs in `docs/adr/` for the highest number
2. **Create ADR file** — `docs/adr/NNNN-<short-title>.md`
3. **Write Context** — Describe the problem, constraints, and forces at play
4. **Document alternatives** — List options considered with pros/cons
5. **State the Decision** — Clearly state what was decided and why
6. **List Consequences** — Both positive and negative impacts of the decision
7. **Set Status** — `Proposed` (for review) or `Accepted` (if already agreed)
8. **Link related ADRs** — Reference any ADRs this supersedes or relates to

## Output
- ADR file in `docs/adr/`
- Updated status of superseded ADRs (if applicable)

## Quality Checklist
- [ ] ADR number is sequential and unique
- [ ] Context clearly explains why a decision was needed
- [ ] Alternatives are documented (not just the chosen option)
- [ ] Decision is stated clearly and unambiguously
- [ ] Both positive and negative consequences are listed
- [ ] Status is set correctly
- [ ] Related ADRs are cross-referenced
