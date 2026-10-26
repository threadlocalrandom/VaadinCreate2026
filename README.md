# VaadinCreate2026

Demo of a modern, lightweight Vaadin 25 application powered by the Vidocq runtime stack, pure Java 25, virtual threads, and Foy Servlet 6.1.

## Tech Stack

- **Java**: Java 25
- **Framework**: Vaadin 25.3.3 (Flow, Lumo theme, styled after the Vidocq documentation)
- **Runtime**: Vidocq Runtime (`io.vidocq.runtime:vidocq-runtime-parent:0.4.0-RC1`)
- **Servlet Container**: Foy Servlet 6.1
- **Build**: Maven with Vaadin Maven plugin

---

## Prerequisites

- **JDK 25** or compatible
- **Maven 3.9+**
- **Git Bash** / terminal

---

## Building the Application

To perform a clean build, prepare and bundle the frontend assets, and package the executable JAR:

```bash
mvn clean package
```

This runs the Vaadin Maven plugin to prepare frontend dependencies, builds the production frontend bundle, compiles the Java sources, and generates the application JAR in `target/`.

For a one-time frontend preparation only:

```bash
mvn compile vaadin:prepare-frontend
```

---

## Testing the Application

To execute the automated unit and integration tests (such as component selection and formatting tests):

```bash
mvn test
```

To run a specific test class:

```bash
mvn test -Dtest=ComponentSelectionTest
```

---

## Running the Application

`vidocq:dev` and `vidocq:run` work from an empty `target/`: `vaadin-maven-plugin`'s `build-frontend` is bound to `process-classes`, and
`vidocq:dev` builds the project first (`mvn process-classes`) when `target/classes` is empty, and says so.
Production mode is set in `pom.xml` for every goal.

| Mode | Command | Start |
|------|---------|-------|
| Development, reloaded on every change | `mvn vidocq:dev` | http://localhost:8080 |
| Run, as in production | `mvn vidocq:run` | http://localhost:8080 |
| Packaged distribution (needs a JDK 25) | `mvn clean package vidocq:package` | `target/vaadin-create-2026-1.0-SNAPSHOT/bin/vaadin-create-2026.sh` |
| Self-contained image with its own JDK | `mvn -Pjlink clean package vidocq:jlink` | `JDK_JAVA_OPTIONS=-Dvaadin.productionMode=true target/dist/bin/vaadin-create-2026` |

The `jlink` profile runs `vauban:modularize`, since no Vaadin jar is a named module, and sets the two flags Vaadin's
layout needs. `vidocq-runtime-vaadin-extension` lets Vaadin find `AppShell` inside the image: without it, the first
request gets no answer there.

### Accessing the Application

Once started, the following services are available:

- **Web Application**: [http://localhost:8080/](http://localhost:8080/)
- **Vidocq Dev Console**: [http://127.0.0.1:8888/](http://127.0.0.1:8888/)
- **Remote Debugging**: Port `5005`
