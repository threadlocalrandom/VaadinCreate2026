# AGENTS.md

## Project Overview

**VaadinCreate2026** is a simple Vaadin application built on the Vidocq runtime stack. It demonstrates a basic Vaadin 25 app with a time-aware greeting feature.

### Tech Stack

- **Backend**: Java with Vaadin 25.3.3
- **Runtime**: Vidocq Runtime (parent POM `io.vidocq.runtime:vidocq-runtime-parent:0.4.0-SNAPSHOT`)
- **Build**: Maven with Vaadin Maven plugin
- **Frontend**: Vite, TypeScript (Vaadin Flow handles frontend compilation)
- **Theme**: Lumo (default Vaadin theme)
- **Package**: `org.tlr`

## Key Files

| File | Purpose |
|------|---------|
| `pom.xml` | Maven build configuration, Vaadin and Vidocq dependencies |
| `docs/Vaadin on Vidocq_ from vidocq_dev to a 68 MB jlink Image - Vidocq.pdf` | Tutorial for this project|
| `src/main/java/org/tlr/AppShell.java` | Application shell: theme and PWA configuration |
| `src/main/java/org/tlr/MainView.java` | Main UI view with greeting input field |
| `src/main/java/org/tlr/GreetService.java` | Service layer: time-based greeting logic |
| `vite.config.ts` | Vite frontend configuration |
## shell operations
Always use git bash
## Building & Running
Always use git bash
### Build

```git bash
mvn clean package
```

This runs the Vaadin Maven plugin to prepare and build the frontend, then packages the JAR.

### Run

The application runs via the Vidocq runtime. Check the Vidocq documentation for the specific launch command, or use the Maven plugin:
mvn vidocq:dev

```git bash
mvn compile vaadin:prepare-frontend
```

## Architecture

The application follows a simple three-component structure:

1. **AppShell** — Configures the application theme (`lumo`) and PWA settings.
2. **MainView** — The root route (`@Route("")`) containing a text field bound to the greeting service via value change listener.
3. **GreetService** — Stateless service that returns a greeting based on the current time of day (morning/afternoon/evening).

## Coding Conventions

- **Indentation**: 4 spaces (Java), follows existing code style.
- **Naming**: standard Java conventions — `PascalCase` for classes, `camelCase` for methods and fields.
- **Comments**: Javadoc on public classes and methods; keep frequency consistent with existing code.
- **Imports**: No wildcards; organize imports in the standard Maven/IDEA order.

## Guidelines for Agents

- Keep changes minimal and focused on the stated issue.
- Respect the Vidocq runtime parent POM — do not modify the parent version or groupId.
- When adding new views, use `@Route` annotations and follow the `MainView` pattern.
- Service classes should be simple and stateless unless persistence is explicitly required.
- Do not add new dependencies without a clear need; the Vidocq parent manages most versions.
- **Never use Vaadin development mode** — the application is configured for production mode only via the `vaadin.productionMode=true` property in `pom.xml`.
- always refer to the Vidocq documentation for best practices and guidelines.
- refer to the docs/Vaadin on Vidocq_ from vidocq_dev to a 68 MB jlink Image - Vidocq.pdf for additional guidance.
