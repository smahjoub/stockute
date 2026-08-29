---
name: coding-style-skill
description: 'Use when writing or refactoring code in this project to ensure consistent coding style, variable usage, error handling, and functional/reactive patterns.'
---
Follow the established coding style in this project:

- Prefer `final var` when a local variable does not need to be reassigned.
- Keep the code functional and reactive when possible.
- Prefer `io.vavr` helpers such as `Try` where they fit the existing patterns.
- Avoid `try/catch` unless it is genuinely needed for a specific boundary or recovery path.
- Reuse existing mappers, ports, helpers, and service patterns instead of introducing new styles.
- Keep methods small, explicit, and easy to compose.
- Match the naming, package layout, and layer separation already present in the repository.
- Do not guess missing requirements, layer placement, or behavior — ask when something is ambiguous or when multiple implementations are reasonable.
- Before changing code, check the surrounding files and follow the conventions already in the project.