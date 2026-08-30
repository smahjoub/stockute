---
name: hexagonal-design-skill
description: 'Use when implementing any new feature or module to ensure hexagonal architecture boundaries are respected across all layers — controllers, ports, services, domain, and adapters.'
---
Respect hexagonal architecture when implementing any new feature:

- Controllers handle only request/response concerns and delegate through inbound ports.
- Application services implement use cases and own orchestration logic.
- Domain models stay free of framework or adapter dependencies.
- Adapters may translate between transport DTOs and domain objects, but must not contain business rules.
- Keep dependency direction inward when code crosses layers.
- Prefer existing ports, mappers, helpers, and conventions over introducing new direct cross-layer calls.
- Preserve the current project conventions for naming, structure, error handling, mapping, and testing.
- Before adding new code, check surrounding modules and follow the patterns already used in the repository.