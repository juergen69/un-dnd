---
name: configure-deployment
description: "Set up automated deployment configuration for staging and production environments."
---

## Purpose
Set up automated deployment configuration for staging and production environments.

## When to Use
- Setting up deployment for a new project
- Adding a new environment (staging, production)
- Migrating deployment strategy

## Input
- Target environment (cloud provider, container orchestrator, PaaS)
- Deployment strategy (rolling, blue-green, canary)
- Environment-specific configuration (URLs, secrets, resource limits)
- Domain and TLS requirements

## Steps
1. **Define environments** — List environments (dev, staging, production) with their configurations
2. **Create deployment manifests** — Docker Compose, Kubernetes manifests, or platform-specific configs
3. **Configure environment variables** — Separate config per environment; use secret management
4. **Set resource limits** — CPU, memory, replica count per environment
5. **Add health checks** — Liveness and readiness probes
6. **Configure networking** — Ingress, load balancer, domain, TLS certificates
7. **Add rollback strategy** — Define how to rollback on failed deployment
8. **Create deployment pipeline** — CD stage that deploys after CI passes
9. **Add smoke tests** — Post-deployment verification

## Output
- Deployment configuration files (docker-compose, k8s manifests, etc.)
- Environment-specific config files
- CD pipeline configuration
- Deployment documentation (runbook)

## Quality Checklist
- [ ] Each environment has isolated configuration
- [ ] Secrets are managed through secret management (not in config files)
- [ ] Health checks are configured for all services
- [ ] Resource limits are set (CPU, memory)
- [ ] Rollback procedure is documented and tested
- [ ] TLS is configured for all public endpoints
- [ ] Deployment can be triggered manually for emergency fixes
- [ ] Smoke tests verify deployment success
