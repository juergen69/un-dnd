---
name: audit-accessibility
description: "Review frontend code and rendered UI for WCAG 2.1 compliance, ensuring the application is accessible to users with disabilities."
---

## Purpose
Review frontend code and rendered UI for WCAG 2.1 compliance, ensuring the application is accessible to users with disabilities.

## When to Use
- After implementing new UI components or pages
- Before a release to verify accessibility standards
- When accessibility has been flagged as a concern in a code review
- As part of a QA or compliance check

## Input
- Components or pages to audit
- Target WCAG conformance level (A, AA, or AAA — default: AA)
- User flows to test for keyboard and screen reader accessibility
- Any known accessibility issues to verify

## Steps
1. **Run automated scan** — Use axe-core, Lighthouse, or pa11y to identify programmatically detectable issues
2. **Check semantic HTML** — Verify correct use of landmarks (`<nav>`, `<main>`, `<aside>`), headings hierarchy (h1→h2→h3), and list structures
3. **Check ARIA attributes** — Verify `aria-label`, `aria-describedby`, `role`, `aria-live` are used correctly and not redundantly
4. **Test keyboard navigation** — Tab through all interactive elements; verify focus order is logical, focus is visible, and no keyboard traps exist
5. **Test screen reader** — Navigate with a screen reader (NVDA, VoiceOver) and verify: all content is announced, interactive elements have accessible names, dynamic content updates are announced
6. **Check color contrast** — Verify text/background contrast ratios meet WCAG AA (4.5:1 normal text, 3:1 large text)
7. **Check responsive behavior** — Verify content is usable at 200% zoom and on small viewports without horizontal scrolling
8. **Check forms** — Verify: labels are associated with inputs, error messages are descriptive and programmatically linked, required fields are indicated
9. **Check media** — Verify images have alt text, videos have captions, animations respect `prefers-reduced-motion`
10. **Document findings** — Categorize issues by WCAG criterion, severity, and component

## Output
- Accessibility audit report with categorized findings
- Specific component and element references for each issue
- Remediation recommendations with WCAG criterion references
- Pass/fail summary per WCAG conformance level

## Quality Checklist
- [ ] Automated scan completed with zero critical violations
- [ ] All interactive elements are keyboard accessible
- [ ] Focus order follows logical reading order
- [ ] Color contrast meets AA requirements (4.5:1 / 3:1)
- [ ] All images have meaningful alt text (or alt="" for decorative)
- [ ] Forms have associated labels and accessible error messages
- [ ] ARIA attributes are correct and not redundant with native semantics
- [ ] Content is usable at 200% zoom
