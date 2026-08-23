---
description: 'Skill enforcing hexagonal architecture boundaries for API interactions.'
---
- API layer interacts only through inbound ports.
- Domain layer contains no framework dependencies.
- Adapters implement outbound ports, never domain logic.
