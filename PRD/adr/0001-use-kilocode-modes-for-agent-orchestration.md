# ADR-0001: Use KiloCode Modes for Agent Orchestration

## Status

Accepted

## Date

2026-03-01

## Context

This project template needs a mechanism for orchestrating specialized AI agents across different domains (frontend, backend, QA, security, etc.). Options considered:

1. **Separate agent markdown files** — Individual `.md` files in an `agents/` directory, each describing a specialist role. Requires the AI to read and interpret them manually.
2. **KiloCode custom modes** — Native `.kilocodemodes` configuration that defines specialist roles with permissions, skills, and instructions baked into KiloCode's mode system.

## Decision

Use KiloCode's native custom modes (`.kilocodemodes`) as the primary mechanism for agent specialization, combined with:
- `.kilocode/workflows/` for multi-step processes
- `.kilocode/skills/` for reusable task-specific guidance
- `.kilocoderules` for project-wide conventions
- `AGENTS.md` for project context and AI instructions

## Consequences

### Positive
- Native integration with KiloCode's mode switching, permissions, and tool access
- Workflows can use `new_task` to delegate between modes automatically
- Skills are auto-detected and applied based on task type
- Mode-specific rules allow fine-grained control per specialist

### Negative
- Tightly coupled to KiloCode — less portable to other AI coding assistants
- Mode definitions in YAML are less readable than dedicated markdown files for very long descriptions

### Neutral
- Blueprint plan documentation moved from agent files to this ADR and README

## References

- [KiloCode Custom Modes](https://kilo.ai/docs/customize/custom-modes)
- [KiloCode Workflows](https://kilo.ai/docs/customize/workflows)
- [KiloCode Skills](https://kilo.ai/docs/customize/skills)
