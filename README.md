# VaadinCreate2026

Demo of a modern, lightweight Vaadin 25 application powered by the Vidocq runtime stack, pure Java 25, virtual threads, and Foy Servlet 6.1.

## Tech Stack

- **Java**: Java 25
- **Framework**: Vaadin 25.3.3 (Flow, Lumo theme, styled after the Vidocq documentation)
- **Runtime**: Vidocq Runtime (`io.vidocq.runtime:vidocq-runtime-parent:0.4.0-SNAPSHOT`)
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

Start the application using `vidocq:dev` with production mode JVM arguments:

```bash
mvn vidocq:dev '-Dvidocq.dev.jvmArgs=-Dvaadin.productionMode=true'
```

> **Important**: Always use `vidocq:dev` with `-Dvaadin.productionMode=true`. Do not use `vidocq:run`, as it re-runs `prepare-frontend` without `build-frontend`, which can cause frontend bundle loading issues.

### Accessing the Application

Once started, the following services are available:

- **Web Application**: [http://localhost:8080/](http://localhost:8080/)
- **Vidocq Dev Console**: [http://127.0.0.1:8888/](http://127.0.0.1:8888/)
- **Remote Debugging**: Port `5005`
