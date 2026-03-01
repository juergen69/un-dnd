---
name: create-component
description: "Create a new UI component with proper structure, types, styles, and accessibility."
---

## Purpose
Create a new UI component with proper structure, types, styles, and accessibility.

## When to Use
- A new UI element is needed (button, form, card, modal, etc.)
- An existing component needs to be split into smaller parts
- A design mockup needs to be translated into code

## Input
- Component name and purpose
- Design specs or mockup (if available)
- Props/API requirements
- Parent component or page context

## Steps
1. **Create component file** — Use the project's component framework (React `.tsx`, Svelte `.svelte`, Vue `.vue`)
2. **Define props/interface** — Type all props with TypeScript; document each prop's purpose
3. **Implement markup** — Use semantic HTML elements; add ARIA attributes for accessibility
4. **Add styles** — Use the project's styling approach (CSS modules, Tailwind, etc.); ensure responsive design
5. **Handle state** — Add local state if needed; connect to global state if required
6. **Add event handlers** — Implement user interactions with proper types
7. **Export component** — Use named exports; add to barrel file if project uses them

## Output
- Component file (`.tsx`, `.svelte`, `.vue`)
- Style file if using CSS modules (`.module.css`)
- Type definitions (inline or separate `.types.ts`)
- Updated barrel/index file (if applicable)

## Quality Checklist
- [ ] Component has TypeScript types for all props
- [ ] All interactive elements are keyboard-accessible
- [ ] ARIA labels are present on non-semantic interactive elements
- [ ] Component is responsive (tested at 320px, 768px, 1024px, 1440px)
- [ ] No hardcoded strings (i18n-ready)
- [ ] Component handles loading, empty, and error states
- [ ] Props have sensible defaults where appropriate
