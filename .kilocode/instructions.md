# Project AI Instructions

These instructions apply to all AI interactions within this project.

## Project Overview

This is an **AI-assisted project template** for KiloCode that provides a complete workflow for planning features, breaking them into tasks, implementing with quality gates, and verifying with SonarQube.

## Project Structure

### Key Directories
- `PRD/` — Feature folders (PRD + tasks together)
- `PRD/<PRD-ID>-<name>/` — One folder per active feature
- `PRD/done/` — Completed features
- `PRD/adr/` — Architecture Decision Records
- `.kilocode/workflows/` — Step-by-step process guides
- `.kilocode/skills/` — Reusable task-specific skill guides

### Configuration Files
- `.kilocodemodes` — Custom specialist mode definitions
- `.kilocoderules` — Project-wide coding rules
- `AGENTS.md` — AI agent context and instructions

## How to Work in This Project

### When a new feature is requested:
1. Run the **Feature Planning Workflow** (`.kilocode/workflows/feature-planning.md`)
2. Create feature folder from `PRD/templates/feature-folder/`
3. Get user approval, then start implementation

### When implementing from an active feature:
1. Run the **Implementation Workflow** (`.kilocode/workflows/implementation.md`)
2. Create tasks from `PRD/templates/feature-folder/tasks/TASK.md`
3. Execute each task using the **Coding & QA Workflow** (`.kilocode/workflows/coding-qa.md`)

### When coding a specific task:
1. Read the task file for context, acceptance criteria, and assigned mode
2. Follow the mode's guidelines and referenced skills
3. Follow the **Coding & QA Workflow** — implement, test, verify with SonarQube
4. Update the task's Completion Notes when done

### When making architecture decisions:
1. Create an ADR using `PRD/adr/templates/adr-template.md`
2. Number it sequentially (check existing ADRs in `PRD/adr/`)

## General Coding Standards

- Write clean, readable code with meaningful names
- Follow the language's idiomatic conventions
- Include error handling for all external interactions
- Write tests for business logic (>80% coverage target)
- Document public APIs and complex logic
- No hardcoded secrets or environment-specific values
- Use structured logging with context
- Use conventional commits: `feat:`, `fix:`, `docs:`, `refactor:`, `test:`, `chore:`

## Quality Gates

Before any task is considered complete:
1. All unit tests pass
2. SonarQube quality gate passes (no new BLOCKER/HIGH issues)
3. Security review passed (if applicable)
4. All acceptance criteria are satisfied
