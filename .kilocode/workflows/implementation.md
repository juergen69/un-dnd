---
description: Break down an active PRD into tasks and coordinate implementation
mode: orchestrator
---

# Implementation Workflow

Use this workflow after a PRD has been approved (folder exists in PRD/).

## Step 1: Read Active PRD

1. Find the PRD folder in `PRD/` (folders without corresponding entry in `PRD/done/`)
2. Read the PRD file inside the folder
3. Review all requirements, acceptance criteria, and technical design
4. Identify the mode assignments table

## Step 2: Task Decomposition

Break the PRD into discrete task files following these rules:

1. Each task must be completable by a single KiloCode mode
2. Tasks should be small enough for one focused session
3. Each task must have clear, unambiguous acceptance criteria
4. Use the template at `PRD/templates/feature-folder/tasks/TASK.md`
5. Name tasks: `TASK-<PRD-ID>-<SEQ>-<short-description>.md`
6. Create task files in the PRD's `tasks/` subfolder

### Sequencing Rules

1. **Database** schema tasks before Backend API tasks
2. **Backend** API tasks before Frontend/Mobile integration tasks
3. **Implementation** tasks before QA tasks
4. All **implementation** before DevOps deployment tasks
5. **Security** review after core implementation, before deployment
6. **Documentation** can run in parallel with implementation

### Mode Assignment Guide

| Task Type | Mode |
|-----------|------|
| UI components, styling, client-side logic | frontend |
| Android/iOS/mobile app development | mobile |
| APIs, services, business logic, middleware | backend |
| Database schema, migrations, queries | database |
| Unit/integration/e2e tests | qa |
| CI/CD, Docker, deployment | devops |
| Auth, input validation, vulnerability checks | security |
| API docs, READMEs, ADRs | documentation |

## Step 3: Implementation Execution

For each task, follow the **Coding & QA Workflow**:

1. Update task status to `active`
2. Switch to the assigned mode using `new_task`
3. Implement following the mode's guidelines and skills
4. Run the **Coding & QA Workflow** for verification
5. Update task status to `done` and fill in Completion Notes
6. Report progress to the user

## Step 4: Progress Tracking

After each task completion:
1. Check remaining tasks in the PRD's `tasks/` folder
2. Verify dependencies are met for the next task
3. Report progress to the user

## Step 5: PRD Completion

When ALL tasks for the PRD are marked as `done`:

1. Verify all PRD acceptance criteria are satisfied
2. Move the entire PRD folder from `PRD/` to `PRD/done/`
3. Update PRD status to `done` and add a Completion Summary
4. Update `CHANGELOG.md` under `[Unreleased]` with the PRD summary
5. Report completion to user with summary of what was delivered
