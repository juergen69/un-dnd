# AI Agent Instructions

## Context

This is an **AI-assisted project template** for KiloCode. It provides a complete workflow for planning features, breaking them into tasks, implementing with quality gates, and verifying with SonarQube.

The template is **framework-agnostic** — it works for web apps, mobile apps, APIs, or any software project.

## Key Directories

| Directory | Purpose |
|-----------|---------|
| `PRD/` | Feature folders (PRD + tasks together) |
| `PRD/<PRD-ID>-<name>/` | One folder per feature with PRD.md and tasks/ |
| `PRD/done/` | Completed features |
| `PRD/adr/` | Architecture Decision Records |
| `.kilocode/workflows/` | Step-by-step process guides |
| `.kilocode/skills/` | Reusable task-specific guidance for modes |

## Workflows (How Things Get Done)

### 1. New Feature → Feature Planning Workflow
**Trigger**: User requests a new feature
**File**: `.kilocode/workflows/feature-planning.md`
**Mode**: Architect
**Result**: Approved feature folder in `PRD/<PRD-ID>-<name>/`

### 2. PRD → Implementation Workflow
**Trigger**: Feature folder created in PRD/
**File**: `.kilocode/workflows/implementation.md`
**Mode**: Orchestrator
**Result**: Tasks created in PRD's tasks/ subfolder

### 3. Task → Coding & QA Workflow
**Trigger**: Task picked up for implementation
**File**: `.kilocode/workflows/coding-qa.md`
**Mode**: Various (frontend, backend, mobile, database, then qa)
**Result**: Code implemented, tested, and verified via SonarQube

## Available Specialist Modes

| Mode | Slug | Purpose |
|------|------|---------|
| Backend | `backend` | APIs, services, business logic, middleware |
| Database | `database` | Schema design, migrations, query optimization |
| DevOps | `devops` | CI/CD, Docker, deployment, infrastructure |
| Documentation | `documentation` | API docs, READMEs, ADRs |
| Frontend | `frontend` | UI components, responsive web, client-side |
| Mobile | `mobile` | Android, iOS, cross-platform mobile apps |
| Orchestrator | `orchestrator` | Task breakdown, coordination, progress tracking |
| QA | `qa` | Testing, code quality, static analysis |
| Security | `security` | Vulnerability assessment, auth audits |

## Common Tasks

### When a new feature is requested:
1. Run the **Feature Planning Workflow** (`.kilocode/workflows/feature-planning.md`)
2. Create feature folder from `PRD/templates/feature-folder/`
3. Get user approval, then start implementation

### When implementing from a PRD:
1. Run the **Implementation Workflow** (`.kilocode/workflows/implementation.md`)
2. Create tasks from `PRD/templates/feature-folder/tasks/TASK.md`
3. Execute each task using the **Coding & QA Workflow**

### When making an architecture decision:
1. Create ADR from `PRD/adr/templates/adr-template.md`
2. Number sequentially (check existing ADRs in `PRD/adr/`)

## Quality Standards

- SonarQube must pass with no new BLOCKER or HIGH issues
- Unit test coverage >80% for business logic
- All acceptance criteria verified before task completion
- Conventional commits: `feat:`, `fix:`, `docs:`, `refactor:`, `test:`, `chore:`
