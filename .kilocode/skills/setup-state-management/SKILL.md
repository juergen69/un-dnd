---
name: setup-state-management
description: "Configure and implement client-side state management for the application or a feature module."
---

## Purpose
Configure and implement client-side state management for the application or a feature module.

## When to Use
- Starting a new project that needs shared state
- Adding a feature that requires state shared across multiple components
- Refactoring prop-drilling into proper state management

## Input
- State requirements (what data needs to be shared)
- Framework in use (React, Svelte, Vue)
- Complexity level (local state, context, or full store)

## Steps
1. **Assess complexity** — Determine if you need: component state, context/stores, or a full state management library
2. **Choose approach**:
   - **Simple**: React Context + useReducer, Svelte stores, Vue reactive/provide-inject
   - **Complex**: Zustand/Redux (React), Svelte stores with derived (Svelte), Pinia (Vue)
3. **Define state shape** — Create TypeScript interfaces for the state structure
4. **Create store/context** — Implement the state container with typed actions/mutations
5. **Add selectors/derived state** — Create computed values to avoid redundant calculations
6. **Connect components** — Wire up components to read from and dispatch to the store
7. **Add persistence** (if needed) — localStorage, sessionStorage, or URL state

## Output
- Store/context definition file with TypeScript types
- Custom hooks or utilities for accessing state
- Updated components connected to the store

## Quality Checklist
- [ ] State shape is fully typed with TypeScript interfaces
- [ ] Actions/mutations are typed and documented
- [ ] No unnecessary re-renders from state changes
- [ ] State is normalized (no deeply nested duplicated data)
- [ ] Derived/computed state is used instead of storing computed values
- [ ] State persistence works across page refreshes (if required)
