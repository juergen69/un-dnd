---
name: create-dockerfile
description: "Create an optimized, secure Dockerfile for containerizing the application."
---

## Purpose
Create an optimized, secure Dockerfile for containerizing the application.

## When to Use
- Containerizing a new application or service
- Optimizing an existing Dockerfile for size or build speed
- Adding Docker support to a project

## Input
- Application type and runtime (Node.js, Java, Go, etc.)
- Build requirements (dependencies, build commands)
- Runtime requirements (ports, volumes, environment variables)
- Base image preferences

## Steps
1. **Choose base image** — Use official, minimal images (alpine variants preferred)
2. **Use multi-stage build** — Separate build stage from runtime stage
3. **Build stage**:
   - Install build dependencies
   - Copy dependency manifests first (for layer caching)
   - Install dependencies
   - Copy source code
   - Run build command
4. **Runtime stage**:
   - Use minimal base image
   - Copy only built artifacts from build stage
   - Set non-root user
   - Define health check
   - Expose port
   - Set entrypoint
5. **Create `.dockerignore`** — Exclude: `.git`, `node_modules`, `*.md`, test files, IDE configs
6. **Optimize layers** — Combine RUN commands, order from least to most frequently changing

## Output
- `Dockerfile`
- `.dockerignore`
- Documentation for build and run commands

## Quality Checklist
- [ ] Multi-stage build is used (build artifacts only in final image)
- [ ] Runs as non-root user
- [ ] Health check is defined
- [ ] `.dockerignore` excludes unnecessary files
- [ ] Dependency install is cached (copy manifests before source)
- [ ] No secrets or credentials in the image
- [ ] Image size is minimized (alpine base, minimal runtime deps)
- [ ] EXPOSE declares the correct port
