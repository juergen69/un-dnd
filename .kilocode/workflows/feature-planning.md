---
description: Plan a new feature — from idea to approved PRD with verification
mode: architect
---

# Feature Planning Workflow

Follow these steps when a new feature is requested.

## Step 1: Analyze Requirements

1. Review the feature request for clarity and completeness
2. Ask clarifying questions if the request is ambiguous
3. Search existing code and PRDs to understand the current state
4. Identify affected areas of the codebase

## Step 2: Create PRD

1. Copy `PRD/templates/feature-folder/` to `PRD/<PRD-ID>-<name>/`
2. Update the PRD file with:
   - **Problem Statement**: Clear description of the problem and who is affected
   - **Proposed Solution**: High-level approach
   - **Functional Requirements**: Numbered FR-1, FR-2, etc.
   - **Non-Functional Requirements**: Performance, security, accessibility, etc.
   - **Acceptance Criteria**: Measurable, testable criteria with checkboxes
   - **Technical Design**: Architecture notes, data models, API contracts, diagrams
   - **Mode Assignments**: Which KiloCode modes are needed and their scope
   - **Dependencies**: External deps, prerequisite PRDs, blockers

## Step 3: Technical Design Review

1. Verify the technical design is feasible with the current architecture
2. Identify potential risks and mitigation strategies
3. Create sequence diagrams (Mermaid) for complex flows
4. Document API contracts if applicable
5. Note any architectural decisions that warrant an ADR in `PRD/adr/`

## Step 4: Verification Checklist

Before marking the PRD as ready, verify:

- [ ] All acceptance criteria are measurable and testable
- [ ] Technical design covers all functional requirements
- [ ] No circular dependencies between this PRD and others
- [ ] All required modes are assigned with clear scope
- [ ] Non-functional requirements include performance targets
- [ ] Security considerations are documented
- [ ] Dependencies are identified and available
- [ ] Estimated complexity is reasonable for a single PRD

## Step 5: User Approval

1. Present a summary of the PRD to the user
2. Highlight key decisions, risks, and trade-offs
3. Wait for user approval before proceeding

## Step 6: Activate

1. Update status field in the PRD metadata to `active`
2. Inform the user: "PRD is approved and active. Use the **Implementation Workflow** to break it down into tasks and start coding."
