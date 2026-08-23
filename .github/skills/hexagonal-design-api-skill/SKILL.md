---
name: hexagonal-design-api-skill
description: 'Use when implementing API features that must respect hexagonal architecture boundaries across controller, port, and domain layers.'
---
Keep API work aligned with hexagonal architecture:

- Controllers should only handle request/response concerns and delegate through inbound ports.
- Application services implement the use cases and contain orchestration logic.
- Domain models stay free of framework dependencies.
- Adapters may translate between transport DTOs and domain objects, but must not hold business rules.
- When adding or changing an endpoint, trace the flow across all affected layers and keep the dependency direction inward.
- Prefer existing ports, mappers, and helpers over introducing direct cross-layer calls.
- If a change crosses layer boundaries, verify each layer still has a single responsibility and no framework leakage into domain code.
