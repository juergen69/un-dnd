---
name: implement-responsive-layout
description: "Create or refactor a page layout to be fully responsive across all device sizes."
---

## Purpose
Create or refactor a page layout to be fully responsive across all device sizes.

## When to Use
- Building a new page or section layout
- Fixing layout issues on specific screen sizes
- Converting a desktop-only layout to responsive

## Input
- Design specs or wireframes for different breakpoints
- Content structure and priority (what's most important on mobile)
- Existing layout code (if refactoring)

## Steps
1. **Analyze breakpoints** — Define layout behavior at: mobile (320-767px), tablet (768-1023px), desktop (1024-1439px), large (1440px+)
2. **Mobile-first markup** — Start with the mobile layout as the base
3. **Apply layout system** — Use CSS Grid for 2D layouts, Flexbox for 1D layouts
4. **Add breakpoint styles** — Use `min-width` media queries to progressively enhance
5. **Handle typography** — Scale font sizes, line heights, and spacing per breakpoint
6. **Handle images/media** — Use responsive images (`srcset`, `picture`) and `max-width: 100%`
7. **Test overflow** — Ensure no horizontal scroll at any breakpoint
8. **Test touch targets** — Minimum 44x44px for interactive elements on mobile

## Output
- Layout component or CSS file with responsive styles
- Media query breakpoints consistent with project standards

## Quality Checklist
- [ ] No horizontal scrollbar at any viewport width from 320px to 1920px
- [ ] Text is readable without zooming on mobile
- [ ] Touch targets are at least 44x44px on mobile
- [ ] Images don't overflow their containers
- [ ] Layout doesn't break between defined breakpoints
- [ ] Navigation is usable on mobile (hamburger menu, bottom nav, etc.)
