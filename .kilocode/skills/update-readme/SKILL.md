---
name: update-readme
description: "Create or update the project README to reflect the current state of the project."
---

## Purpose
Create or update the project README to reflect the current state of the project.

## When to Use
- Project is initialized and needs a README
- New features or setup steps have been added
- Dependencies or prerequisites have changed
- Deployment process has changed

## Input
- Current project state (features, tech stack, structure)
- Setup and installation requirements
- Existing README (if updating)
- Recent changes that need documentation

## Steps
1. **Review current README** — Identify outdated or missing sections
2. **Update/create sections**:
   - **Title and description** — What the project does, in one paragraph
   - **Prerequisites** — Required tools, versions, accounts
   - **Installation** — Step-by-step setup instructions
   - **Configuration** — Environment variables, config files
   - **Usage** — How to run, common commands
   - **Project structure** — Directory layout with descriptions
   - **Testing** — How to run tests
   - **Deployment** — How to deploy
   - **Contributing** — How to contribute (if open source)
3. **Add/update badges** — Build status, coverage, version
4. **Verify instructions** — Follow the setup steps on a clean environment mentally
5. **Check links** — Ensure all internal and external links work

## Output
- Updated `README.md`
- Any supporting documentation files referenced from README

## Quality Checklist
- [ ] A new developer can set up the project by following the README alone
- [ ] All commands are copy-pasteable (no placeholders without explanation)
- [ ] Prerequisites list specific versions where relevant
- [ ] Environment variables are documented with descriptions and examples
- [ ] Project structure section matches actual directory layout
- [ ] All links are valid
- [ ] No outdated information remains
