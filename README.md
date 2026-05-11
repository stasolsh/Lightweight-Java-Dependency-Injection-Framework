# Lightweight Java Dependency Injection Framework
![Build](https://github.com/stasolsh/Lightweight-Java-Dependency-Injection-Framework/actions/workflows/custom-action.yml/badge.svg)
![Coverage](https://codecov.io/gh/stasolsh/Lightweight-Java-Dependency-Injection-Framework/branch/master/graph/badge.svg)
![Java](https://img.shields.io/badge/Java-17-blue)
![Maven](https://img.shields.io/badge/Maven-3.9+-blue)
![JUnit](https://img.shields.io/badge/JUnit-5-red?logo=junit5)
![License](https://img.shields.io/badge/license-MIT-green)

> Educational lightweight Dependency Injection (DI) and Inversion of Control (IoC) framework inspired by core Spring concepts such as dependency injection, bean lifecycle management, singleton scope, annotation-based configuration, and reflection-driven object creation.

This project demonstrates how a custom IoC container can be implemented in pure Java using:
- reflection
- annotations
- runtime scanning
- bean definitions
- object configurators
- singleton lifecycle management
- qualifier-based dependency injection

The framework provides a simplified but practical understanding of how dependency injection containers like Spring internally manage object creation and wiring.

---

# Features

- Annotation-based dependency injection
- Constructor-free field injection
- Qualifier-based bean resolution
- Singleton scope support
- Runtime package scanning
- Reflection-based dependency wiring
- Bean definition registry
- Interface-to-implementation mapping
- Support annotations processing
- Lightweight IoC container
- PostConstruct lifecycle support
- Dynamic proxy support for deprecated classes

---

# Technologies Used

- Java
- Reflection API
- Reflections library
- Lombok
- JMX-style runtime metadata concepts
- Dynamic Proxies
- Custom annotations
- Dependency Injection principles

---

# Architecture Overview

```text
ApplicationContext
        ↓
   ObjectFactory
        ↓
ObjectConfigurators
        ↓
 Reflection-based Injection
        ↓
 BeanDefinition Registry
```

---

# Core Concepts

## Dependency Injection

Dependencies are injected automatically using the custom:

```java
@InjectByType
```

annotation. :contentReference[oaicite:0]{index=0}

Example:

```java
@InjectByType
private Validator validator;
```

---

## Singleton Scope

Classes annotated with:

```java
@Singleton
```

are stored and reused within the application context. :contentReference[oaicite:1]{index=1}

Example:

```java
@Singleton
public class UserService {
}
```

---

## Qualifier-Based Injection

Multiple implementations of the same interface can be resolved using qualifiers:

```java
@InjectByType(qualifier = "english")
```

The framework internally resolves implementations through:
- reflection scanning
- cached interface mappings
- qualifier matching

Implemented in:
- `JavaConfig`
- `Ifc2ImplHolder`

:contentReference[oaicite:2]{index=2} :contentReference[oaicite:3]{index=3}

---

# Main Components

| Component                                                                                                                                            | Description |
|------------------------------------------------------------------------------------------------------------------------------------------------------|---|
| [ApplicationContext.java](src/main/java/com/own/di/example/framework/context/ApplicationContext.java)                                                | Central IoC container managing object creation, singleton lifecycle, and dependency resolution |
| [ObjectFactory.java](src/main/java/com/own/di/example/framework/factory/ObjectFactory.java)                                                          | Creates objects, configures dependencies, invokes lifecycle hooks, and creates dynamic proxies |
| [InjectByType.java](src/main/java/com/own/di/example/framework/annotation/InjectByType.java)                                                         | Annotation used for field-based dependency injection |
| [Singleton.java](src/main/java/com/own/di/example/framework/annotation/Singleton.java)                                                               | Marks classes as singleton-scoped beans |
| [JavaConfig.java](src/main/java/com/own/di/example/framework/context/JavaConfig.java)                                                                | Resolves implementation classes using reflection scanning and qualifiers |
| [BeanDefinition.java](src/main/java/com/own/di/example/framework/context/BeanDefinition.java)                                                        | Stores bean metadata and runtime object references |
| [BeanDefinitionHolder.java](src/main/java/com/own/di/example/framework/context/BeanDefinitionHolder.java)                                            | Internal bean registry and singleton storage |
| [Ifc2Impl.java](src/main/java/com/own/di/example/framework/context/Ifc2Impl.java)                                                                    | Interface-to-implementation mapping model |
| [Ifc2ImplHolder.java](src/main/java/com/own/di/example/framework/context/Ifc2ImplHolder.java)                                                        | Stores and resolves interface implementation mappings |
| [InjectByTypeAnnotationObjectConfigurator.java](src/main/java/com/own/di/example/framework/processing/InjectByTypeAnnotationObjectConfigurator.java) | Processes dependency injection annotations and injects runtime dependencies |
| [ObjectConfigurator.java](src/main/java/com/own/di/example/framework/interfaces/ObjectConfigurator.java)                                             | Extension point for custom object configuration logic |
| [Locale.java](src/main/java/com/own/di/example/framework/annotation/support/Locale.java)                                                             | Support annotation for locale-based metadata injection |
| [ValidationPatterns.java](src/main/java/com/own/di/example/framework/annotation/support/ValidationPatterns.java)                                     | Support annotation for validation pattern injection |
| [Utils.java](src/main/java/com/own/di/example/framework/util/Utils.java)                                                                             | Utility helper methods used internally |

---

# ApplicationContext

`ApplicationContext` acts as the central IoC container. :contentReference[oaicite:4]{index=4}

Responsibilities:
- package scanning
- bean lifecycle management
- singleton caching
- implementation resolution
- dependency retrieval

Example:

```java
ApplicationContext context =
        new ApplicationContext("com.example");

UserService userService =
        context.getObject(UserService.class);
```

---

# Object Creation Lifecycle

The framework creates objects using the following pipeline:

```text
1. Resolve implementation
2. Create object via reflection
3. Inject dependencies
4. Process support annotations
5. Invoke @PostConstruct
6. Create proxy if needed
7. Register singleton
```

Implemented in:
- `ObjectFactory`
- `InjectByTypeAnnotationObjectConfigurator`

:contentReference[oaicite:5]{index=5} :contentReference[oaicite:6]{index=6}

---

# Reflection-Based Injection

Dependencies are injected dynamically into fields annotated with:

```java
@InjectByType
```

using Java reflection.

The configurator:
- scans fields
- resolves implementations
- injects runtime instances
- processes additional support annotations

Implemented in:
- `InjectByTypeAnnotationObjectConfigurator`

:contentReference[oaicite:7]{index=7}

---

# Support Annotations

The framework also demonstrates metadata-style annotations.

## Locale

```java
@Locale("ENGLISH")
```

Injects locale metadata into target objects. :contentReference[oaicite:8]{index=8}

---

## ValidationPatterns

```java
@ValidationPatterns({"\\d+", "[a-zA-Z]+"})
```

Injects validation patterns into configured objects. :contentReference[oaicite:9]{index=9}

---

# Dynamic Proxy Support

Deprecated classes are automatically wrapped in Java dynamic proxies.

The proxy:
- intercepts method calls
- logs warnings
- delegates execution to original objects

Implemented in:
- `ObjectFactory`

:contentReference[oaicite:10]{index=10}

---

# Bean Registry

Singleton objects and bean metadata are stored internally using:

- `BeanDefinition`
- `BeanDefinitionHolder`

The registry supports:
- type lookup
- qualifier lookup
- singleton reuse
- runtime bean metadata

:contentReference[oaicite:11]{index=11} :contentReference[oaicite:12]{index=12}

---

# Example Usage

## Define Interface

```java
public interface NotificationService {
    void send();
}
```

---

## Create Implementation

```java
@Singleton
public class EmailNotificationService
        implements NotificationService {

    @Override
    public void send() {
        System.out.println("Sending email");
    }
}
```

---

## Inject Dependency

```java
public class UserController {

    @InjectByType
    private NotificationService service;

    public void process() {
        service.send();
    }
}
```

---

## Start Context

```java
ApplicationContext context =
        new ApplicationContext("com.example");

UserController controller =
        context.getObject(UserController.class);

controller.process();
```

---

# Learning Topics Covered

- Dependency Injection (DI)
- Inversion of Control (IoC)
- reflection-based frameworks
- runtime annotation processing
- singleton lifecycle management
- bean registries
- interface resolution
- dynamic proxies
- object factories
- lightweight container architecture
- framework internals
- Spring-inspired architecture concepts

---

# Real-World Relevance

The concepts demonstrated in this repository are foundational for understanding modern Java frameworks such as:

- Spring Framework
- Spring Boot
- CDI / Jakarta Inject
- Guice
- Micronaut
- Quarkus

This project helps understand:
- how IoC containers work internally
- how dependency injection frameworks manage object graphs
- how runtime reflection and annotations are used in enterprise Java

---

# Future Improvements

- [ ] Constructor injection
- [ ] Circular dependency detection
- [ ] Bean scopes (prototype/request/session)
- [ ] Configuration properties support
- [ ] AOP interception
- [ ] Method injection
- [ ] Bean lifecycle callbacks
- [ ] Lazy initialization
- [ ] YAML/JSON configuration
- [ ] Conditional beans
- [ ] Custom qualifiers
- [ ] Dependency graph visualization

---