# Enterprise E-commerce Microservices Master Project

> **Production-grade Enterprise E-commerce Backend System using Java 21, Spring Boot, Spring Cloud and Microservices Architecture**

---

# Project Overview

This repository contains a complete production-grade Enterprise E-commerce Backend System built from scratch using modern Java Backend technologies.

The project is designed by following real software company development workflows instead of creating demo applications.

Every module is developed one step at a time exactly as enterprise teams work in real projects.

The objective is not only to build software but also to deeply understand the internal working of Java, Spring Boot, Maven, JVM and Microservices Architecture.

---

# Project Goals

The primary goals of this project are:

* Learn Enterprise Java Development
* Learn Spring Boot from Beginner to Advanced
* Build Production-grade Microservices
* Understand Internal Working of Every Technology
* Learn Maven Multi Module Architecture
* Follow Real Company Coding Standards
* Learn Enterprise Project Structure
* Write Reusable Common Library
* Build Secure Authentication Service
* Build Scalable E-commerce Backend
* Prepare for Java Backend Interviews
* Gain Real Industry-Level Development Experience

---

# Learning Philosophy

This project follows one strict rule:

> **Understand First → Design Second → Code Third**

Instead of memorizing code, every topic is learned from its internal working.

Every chapter follows the same workflow:

Requirement

↓

Internal Working

↓

Design

↓

Coding

↓

Line-by-Line Explanation

↓

Compile Verification

↓

Office-Level Task

---

# Project Type

Enterprise Backend Application

Architecture:

* Microservices
* Maven Multi Module
* Layered Architecture
* REST APIs
* JWT Authentication
* Event Driven Communication
* Cloud Native Design

---

# Technology Stack

## Core Language

* Java 21

---

## Backend Framework

* Spring Boot

---

## Build Tool

* Maven

---

## Build Style

* Maven Multi Module

---

## Database

* MySQL

---

## ORM

* Spring Data JPA
* Hibernate

---

## Security

* Spring Security
* JWT Authentication

---

## Cloud

* Spring Cloud

---

## Service Discovery

* Eureka Server

---

## API Gateway

* Spring Cloud Gateway

---

## Configuration

* Config Server

---

## Inter-Service Communication

* OpenFeign

---

## Messaging

* Apache Kafka

---

## Cache

* Redis

---

## Documentation

* OpenAPI
* Swagger

---

## Monitoring

* Spring Boot Actuator

---

## Metrics

* Prometheus

---

## Distributed Tracing

* Zipkin

---

## Containerization

* Docker
* Docker Compose

---

## Version Control

* Git
* GitHub

---

# Development Environment

| Tool                    | Version   |
| ----------------------- | --------- |
| Java                    | 21        |
| IntelliJ IDEA Community | 2025.2.4  |
| Maven                   | Installed |
| Git                     | Installed |

---

# Current Project Structure

```text
ecommerce-parent
│
├── common-lib
│
├── auth-service
│
├── user-service
│
├── product-service
│
├── cart-service
│
├── inventory-service
│
├── order-service
│
├── payment-service
│
├── notification-service
│
├── api-gateway
│
├── config-server
│
└── discovery-server
```

---

# High Level Architecture

```text
                        Client

                           │

                           ▼

                   API Gateway

                           │

        ┌──────────────────┼──────────────────┐

        ▼                  ▼                  ▼

   Auth Service      User Service      Product Service

        │                  │                  │

        ▼                  ▼                  ▼

   MySQL Database     MySQL Database    MySQL Database

        │                  │                  │

        └──────────────────┼──────────────────┘

                           │

                           ▼

                      Common Library
```

---

# Enterprise Development Rules

This project follows strict enterprise development principles.

* No shortcut coding
* No copy-paste architecture
* One topic at a time
* One production class at a time
* Compile verification after every implementation
* Real company workflow
* Clean code principles
* Reusable components
* High readability
* Beginner to Advanced progression

---

# Repository Purpose

This repository serves multiple purposes:

* Learning Resource
* Interview Preparation
* Portfolio Project
* Enterprise Architecture Reference
* Production Coding Practice
* Java Backend Master Journey

---

# Project Progress

## Completed

* Chapter 01
* Chapter 02
* Chapter 03
* Chapter 04

## Current

Chapter 05

Auth Service Development

---

# Documentation Structure

This README is intentionally detailed.

Each chapter of the project has been documented with:

* Objective
* Internal Working
* Architecture
* Coding Decisions
* Enterprise Best Practices
* Build Verification
* Office Tasks
* Interview Preparation

This makes the repository suitable for both learning and professional reference.

---

**README.md Documentation Progress**

* ✅ Part 1 - Project Overview
* ⏳ Part 2 - Chapter 01 Documentation
* ⏳ Part 3 - Chapter 02 Documentation
* ⏳ Part 4 - Chapter 03 Documentation
* ⏳ Part 5 - Chapter 04 Documentation
* ⏳ Part 6 - Project Standards & Future Roadmap
  ==================================================================================

# README.md - Part 2

# Chapter 01 - Project Planning & Enterprise Architecture

---

# Chapter Objective

Chapter 01 was focused on building a strong architectural foundation before writing any business code.

Instead of directly creating Spring Boot services, the project started with planning because enterprise software is always designed before implementation.

The objective was to understand:

* Why Microservices?
* Why Maven Multi Module?
* Why Parent POM?
* Why Common Library?
* Why Separate Services?
* How enterprise projects are structured?

---

# Why Planning Comes First

One of the biggest mistakes beginners make is starting coding immediately.

Enterprise companies never start development like this.

The real workflow is:

Business Requirement

↓

Architecture Discussion

↓

Technology Selection

↓

Project Structure

↓

Module Design

↓

Coding

↓

Testing

↓

Deployment

This project follows the same workflow.

---

# Problem Statement

Suppose an e-commerce company has the following features:

* User Registration
* Login
* Product Management
* Shopping Cart
* Inventory
* Orders
* Payments
* Notifications

Question:

Should everything be written inside one Spring Boot application?

Answer:

No.

Because large enterprise systems become difficult to maintain, test and scale.

---

# Why Microservices?

Instead of one large application, the project is divided into multiple independent services.

Each service has a single responsibility.

Example:

```text
Auth Service
    ↓
Authentication & JWT

User Service
    ↓
User Management

Product Service
    ↓
Product Catalog

Cart Service
    ↓
Shopping Cart

Inventory Service
    ↓
Stock Management

Order Service
    ↓
Order Processing

Payment Service
    ↓
Payment Integration

Notification Service
    ↓
Email / SMS / Push Notifications
```

This architecture allows every service to evolve independently.

---

# Benefits of Microservices

* Independent deployment
* Independent scaling
* Smaller codebase
* Easier maintenance
* Team-wise ownership
* Better fault isolation
* Faster development
* Technology flexibility (when required)

---

# Why Maven Multi Module?

If every service is created as an independent project:

```text
Auth Project

User Project

Product Project

Order Project
```

Every project would duplicate:

* DTOs
* Exceptions
* Utilities
* Constants

This violates the DRY Principle.

To solve this problem, Maven Multi Module Architecture was selected.

---

# Maven Multi Module Architecture

```text
ecommerce-parent

│

├── common-lib

├── auth-service

├── user-service

├── product-service

├── cart-service

├── inventory-service

├── order-service

├── payment-service

├── notification-service

├── api-gateway

├── config-server

└── discovery-server
```

The parent project manages every child module.

---

# Why Parent POM?

The Parent POM acts as the central controller of the entire project.

Responsibilities include:

* Java version
* Spring Boot version
* Dependency versions
* Plugin versions
* Module management
* Build management

Instead of configuring every service separately, all common configuration is inherited.

---

# Parent-Child Relationship

```text
Parent POM

↓

common-lib

↓

auth-service

↓

user-service

↓

product-service

↓

order-service
```

Every child inherits common configuration from the parent.

This keeps every service consistent.

---

# Why Common Library?

Every microservice requires reusable code.

Examples:

* ApiResponse
* ErrorResponse
* Common Exceptions
* Utility Classes
* Constants
* Validation Helpers

Instead of rewriting the same code repeatedly, a reusable module called **common-lib** was planned.

---

# High-Level Architecture

```text
                    Client

                       │

                       ▼

                 API Gateway

                       │

       ┌───────────────┼───────────────┐

       ▼               ▼               ▼

 Auth Service    User Service    Product Service

       │               │               │

       ▼               ▼               ▼

   Own Database    Own Database    Own Database

       └───────────────┼───────────────┘

                       ▼

                  common-lib
```

---

# Why Separate Databases?

Every service owns its own database.

Example:

```text
Auth Service
↓

Authentication Database
```

```text
Product Service
↓

Product Database
```

This prevents tight coupling.

No service directly modifies another service's database.

---

# Initial Module Planning

The following modules were planned during Chapter 01:

| Module               | Responsibility        |
| -------------------- | --------------------- |
| common-lib           | Shared reusable code  |
| auth-service         | Authentication & JWT  |
| user-service         | User Management       |
| product-service      | Product Catalogue     |
| cart-service         | Shopping Cart         |
| inventory-service    | Stock Management      |
| order-service        | Order Processing      |
| payment-service      | Payment Processing    |
| notification-service | Email/SMS             |
| api-gateway          | Single Entry Point    |
| config-server        | Central Configuration |
| discovery-server     | Service Discovery     |

---

# Enterprise Design Principles Selected

The project intentionally follows:

* SOLID Principles
* DRY Principle
* Layered Architecture
* Single Responsibility Principle
* Reusability
* Loose Coupling
* High Cohesion
* Clean Code
* Modular Design

---

# Expected Learning Outcomes

After completing Chapter 01, the following concepts became clear:

* Difference between Monolith and Microservices
* Enterprise project planning
* Module separation
* Maven Multi Module overview
* Parent POM purpose
* Common Library planning
* Enterprise folder structure
* High-level architecture design

---

# Interview Questions

1. Why do companies choose Microservices?
2. What are the disadvantages of Monolithic Architecture?
3. Why use Maven Multi Module?
4. What is the purpose of a Parent POM?
5. What is a Common Library?
6. Why should services own separate databases?
7. What is the responsibility of an API Gateway?
8. Why should architecture be designed before coding?

---

# Chapter 01 Deliverables

✅ Enterprise Architecture Planned

✅ Project Modules Identified

✅ Parent Project Designed

✅ Microservice Boundaries Defined

✅ Development Workflow Established

---

# Chapter 01 Status

**Completed Successfully**

This chapter created the architectural blueprint that the remaining project follows.

---

## README Documentation Progress

* ✅ Part 1 - Project Overview
* ✅ Part 2 - Chapter 01 Documentation
* ⏳ Part 3 - Chapter 02 Documentation
* ⏳ Part 4 - Chapter 03 Documentation
* ⏳ Part 5 - Chapter 04 Documentation
* ⏳ Part 6 - Final Project Guide & Future Roadmap
  =================================================================================

# README.md - Part 3

# Chapter 02 - Maven Beginner to Advanced

---

# Chapter Objective

The purpose of this chapter was to gain a deep understanding of Maven instead of using it only as a build command.

The focus was to understand:

* Why Maven exists
* How Maven works internally
* Build Lifecycle
* Dependency Management
* Local Repository
* Central Repository
* Maven Plugins
* Parent POM
* Multi Module Build
* Enterprise Build Workflow

---

# What is Maven?

Maven is a Build Automation and Dependency Management Tool for Java projects.

It helps developers:

* Download project dependencies
* Compile source code
* Execute tests
* Package applications
* Install artifacts
* Maintain consistent builds

Instead of manually downloading every JAR file, Maven manages everything automatically.

---

# Why Maven?

Without Maven:

```text
Developer

↓

Search JAR files manually

↓

Download JAR

↓

Copy into Project

↓

Configure Classpath

↓

Repeat for every dependency
```

Problems:

* Version conflicts
* Manual updates
* Missing dependencies
* Difficult maintenance

---

With Maven:

```text
pom.xml

↓

Maven

↓

Download Dependencies

↓

Compile

↓

Package

↓

Run
```

Everything is automated.

---

# Maven Architecture

```text
Developer

↓

pom.xml

↓

Maven

↓

Local Repository (.m2)

↓

If Missing

↓

Maven Central Repository

↓

Download

↓

Local Cache

↓

Build Project
```

---

# Project Object Model (POM)

The **pom.xml** file is the heart of every Maven project.

It defines:

* Project Information
* Dependencies
* Plugins
* Build Configuration
* Java Version
* Packaging
* Parent Information
* Modules

---

# Standard Maven Directory Structure

```text
project

│

├── src

│   ├── main

│   │     ├── java

│   │     └── resources

│   │

│   └── test

│         └── java

│

├── target

│

└── pom.xml
```

---

# Maven Build Lifecycle

The Default Build Lifecycle contains multiple phases.

The most commonly used are:

```text
validate

↓

compile

↓

test

↓

package

↓

verify

↓

install

↓

deploy
```

Each phase automatically executes all previous phases.

Example:

Running

```bash
mvn install
```

actually performs:

```text
validate

↓

compile

↓

test

↓

package

↓

verify

↓

install
```

---

# Build Lifecycle Explained

## validate

Checks whether the project structure and configuration are correct.

---

## compile

Compiles Java source files into `.class` files.

```text
.java

↓

javac

↓

.class
```

---

## test

Runs unit tests.

---

## package

Creates the final artifact.

Example:

```text
common-lib-1.0.0.jar
```

---

## verify

Performs additional verification before installation.

---

## install

Copies the generated JAR into the local Maven repository.

Example:

```text
~/.m2/repository
```

Windows:

```text
C:\Users\<username>\.m2\repository
```

---

## deploy

Uploads artifacts to a remote Maven repository such as Nexus or Artifactory.

Normally used in enterprise CI/CD pipelines.

---

# Dependency Management

Dependencies are declared inside:

```xml
<dependencies>

    <dependency>

    </dependency>

</dependencies>
```

Example:

* Spring Boot
* Lombok
* MySQL Driver
* JWT
* Validation

Maven automatically downloads required libraries.

---

# Dependency Resolution

When Maven reads:

```xml
<dependency>

    Spring Boot Starter Web

</dependency>
```

Flow:

```text
pom.xml

↓

Search Local Repository

↓

Found?

↓

Yes → Use

↓

No

↓

Download from Maven Central

↓

Store in Local Repository

↓

Compile Project
```

---

# Local Repository

Location:

```text
~/.m2/repository
```

Purpose:

* Cache downloaded libraries
* Improve build speed
* Enable offline builds

---

# Maven Central Repository

Default online repository from where Maven downloads artifacts.

Flow:

```text
Maven Central

↓

Download

↓

Local Repository

↓

Project
```

---

# Transitive Dependencies

Many dependencies depend on other libraries.

Example:

```text
Spring Boot Starter Web

↓

Spring MVC

↓

Jackson

↓

Validation

↓

Logging
```

You declare one dependency, Maven downloads all required child dependencies automatically.

---

# Dependency Scope

Common scopes include:

| Scope    | Purpose                         |
| -------- | ------------------------------- |
| compile  | Available everywhere            |
| test     | Only during testing             |
| provided | Runtime environment provides it |
| runtime  | Needed only at runtime          |

Choosing the correct scope keeps the application lightweight.

---

# Maven Plugins

Plugins extend Maven functionality.

Examples:

* Compiler Plugin
* Surefire Plugin
* Jar Plugin
* Spring Boot Maven Plugin

Plugins are responsible for performing build tasks.

---

# Parent POM Concept

Instead of repeating configuration across multiple services:

Every common configuration is centralized inside the Parent POM.

Benefits:

* Single version management
* Cleaner child projects
* Easier upgrades
* Consistent builds

---

# Multi Module Build

Project Structure:

```text
Parent

├── common-lib

├── auth-service

├── user-service

├── product-service
```

Running:

```bash
mvn clean install
```

Builds every module automatically in the correct order.

---

# Maven Reactor

The Maven Reactor manages:

* Module order
* Dependency graph
* Build sequence

Flow:

```text
Parent

↓

Read Modules

↓

Resolve Dependencies

↓

Build Order

↓

Compile

↓

Package

↓

Install
```

---

# Enterprise Build Workflow

Typical enterprise workflow:

Developer

↓

Code Changes

↓

mvn clean install

↓

Git Commit

↓

Push

↓

CI Pipeline

↓

Automated Build

↓

Automated Tests

↓

Deployment

---

# Best Practices

* Keep dependency versions centralized.
* Avoid duplicate dependencies.
* Use Parent POM effectively.
* Always build from the parent project.
* Execute `mvn clean install` before pushing code.
* Remove unused dependencies.
* Use appropriate dependency scopes.
* Keep project structure consistent.

---

# Common Mistakes

❌ Manually downloading JAR files.

❌ Adding duplicate dependencies.

❌ Ignoring dependency conflicts.

❌ Building child modules independently without understanding dependencies.

❌ Forgetting to execute a clean build.

---

# Interview Questions

1. What is Maven?
2. Why is Maven required?
3. What is a POM?
4. Explain the Maven Build Lifecycle.
5. Difference between package and install.
6. What is the Local Repository?
7. What is Maven Central?
8. What are Transitive Dependencies?
9. Explain Dependency Scope.
10. What is Maven Reactor?
11. What is a Parent POM?
12. Why use Maven Multi Module?

---

# Chapter 02 Deliverables

✅ Maven Fundamentals

✅ Build Lifecycle

✅ Dependency Management

✅ Repository Management

✅ Plugin Architecture

✅ Parent POM Understanding

✅ Multi Module Build

✅ Reactor Build

✅ Enterprise Build Workflow

---

# Chapter 02 Status

**Completed Successfully**

This chapter established the complete Maven foundation required for enterprise microservices development.

---

## README Documentation Progress

* ✅ Part 1 - Project Overview
* ✅ Part 2 - Chapter 01 Documentation
* ✅ Part 3 - Chapter 02 Documentation
* ⏳ Part 4 - Chapter 03 Documentation
* ⏳ Part 5 - Chapter 04 Documentation
* ⏳ Part 6 - Final Project Guide & Future Roadmap
  =================================================================================

# README.md - Part 4

# Chapter 03 - Parent Project Setup & Common Library Foundation

---

# Chapter Objective

After completing project planning and Maven fundamentals, the next step was to build the actual enterprise project foundation.

The objective of this chapter was to:

* Create the Enterprise Parent Project
* Configure Parent POM
* Create the Common Library Module
* Establish Parent-Child Relationships
* Verify Multi Module Build
* Configure Local Git Repository
* Prepare the project for future microservices

This chapter created the foundation that every future microservice depends on.

---

# Why Parent Project First?

In enterprise applications, developers do not create individual services independently.

Instead, a single parent project controls:

* Java Version
* Spring Boot Version
* Dependency Versions
* Plugin Versions
* Build Configuration
* Module Management

This ensures consistency across the entire system.

---

# Enterprise Project Structure

```text
ecommerce-parent
│
├── pom.xml
│
├── common-lib
│     ├── pom.xml
│     └── src
│
├── auth-service
│
├── user-service
│
├── product-service
│
├── cart-service
│
├── inventory-service
│
├── order-service
│
├── payment-service
│
├── notification-service
│
├── api-gateway
│
├── config-server
│
└── discovery-server
```

The parent project acts as the root of the complete enterprise system.

---

# Parent POM Responsibilities

The Parent POM became the central configuration point for all modules.

It manages:

* Project Version
* Java Version
* Encoding
* Dependency Versions
* Plugin Versions
* Module Registration
* Build Configuration

Child projects inherit these configurations automatically.

---

# Parent → Child Inheritance

```text
Parent POM

        │

        ▼

common-lib

        │

        ▼

auth-service

        │

        ▼

user-service

        │

        ▼

product-service
```

This inheritance eliminates duplicate configuration across projects.

---

# Why common-lib Was Created First

Every microservice requires reusable components.

Examples:

* DTOs
* Response Models
* Exceptions
* Utility Classes
* Constants
* Validation Helpers

Instead of implementing them repeatedly, they are placed inside a reusable Maven module.

Benefits:

* No duplicate code
* Better maintainability
* Shared standards
* Easier future development

---

# common-lib Responsibilities

The Common Library is responsible for reusable business-independent components.

It contains:

* Common DTOs
* Generic Response Models
* Exception Hierarchy
* Utility Classes
* Validation Helpers
* Shared Constants

It intentionally does **not** contain:

* Business Logic
* Database Configuration
* Security Configuration
* Service Implementations

---

# Parent-Child Dependency Flow

```text
Parent Project

        │

        ▼

common-lib

        │

        ▼

JAR Generated

        │

        ▼

Future Microservices

        │

        ▼

Reusable Components
```

This approach follows the DRY (Don't Repeat Yourself) principle.

---

# Build Verification

The first successful enterprise build was verified using:

```bash
mvn clean install
```

Build Flow:

```text
Clean

↓

Compile

↓

Test

↓

Package

↓

Install

↓

BUILD SUCCESS
```

This confirmed that:

* Parent Project was valid.
* Child module was correctly registered.
* Parent-Child inheritance worked.
* Maven Reactor Build worked.
* Local Repository installation succeeded.

---

# Generated Build Artifact

The build produced:

```text
common-lib-1.0.0.jar
```

This artifact became reusable across all future services.

---

# Local Maven Repository

After installation, the generated JAR was copied into:

Windows:

```text
C:\Users\<username>\.m2\repository
```

Linux/macOS:

```text
~/.m2/repository
```

Future services automatically consume this artifact during compilation.

---

# Maven Reactor Build

Instead of compiling modules manually:

```text
Compile common-lib

↓

Package common-lib

↓

Install common-lib

↓

Compile dependent modules

↓

Build Complete
```

Maven determines the correct build sequence automatically.

---

# Local Git Foundation

Version control was initialized before developing business logic.

The repository was prepared with:

* Git Initialization
* Initial Commit Strategy
* Standard Ignore Rules
* Enterprise Repository Structure

This ensures every future change is tracked.

---

# Enterprise Decisions Taken

The following architectural decisions were finalized:

✅ Maven Multi Module

✅ Parent POM

✅ Common Library

✅ Shared Dependency Management

✅ Incremental Development

✅ Production Package Structure

✅ Build Verification After Every Change

---

# Lessons Learned

After completing this chapter, the following concepts became clear:

* Parent Project Creation
* Parent POM Configuration
* Child Module Registration
* Parent-Child Inheritance
* Maven Reactor Build
* Artifact Generation
* Local Repository Installation
* Git Foundation

---

# Best Practices

* Always build from the Parent Project.
* Keep version management centralized.
* Do not duplicate dependency versions.
* Keep common code inside common-lib.
* Verify builds after every implementation.
* Commit small, meaningful changes.
* Maintain consistent module naming.

---

# Common Mistakes

❌ Creating independent projects for every service.

❌ Duplicating Parent POM configuration.

❌ Building only child modules without understanding dependencies.

❌ Keeping reusable code inside business services.

❌ Ignoring build verification.

---

# Interview Questions

1. Why do we need a Parent Project?
2. What is Parent-Child Inheritance in Maven?
3. Why is common-lib created first?
4. What is Maven Reactor Build?
5. What happens during `mvn clean install`?
6. Where is the generated JAR stored?
7. Why should reusable code be isolated?
8. What are the responsibilities of a Parent POM?
9. What is the difference between packaging and installation?
10. Why initialize Git before development?

---

# Chapter 03 Deliverables

✅ Parent Project Created

✅ Parent POM Configured

✅ common-lib Module Created

✅ Parent-Child Relationship Verified

✅ Maven Reactor Build Verified

✅ Build Successfully Installed

✅ Local Git Repository Initialized

✅ Enterprise Foundation Ready

---

# Chapter 03 Status

**Completed Successfully**

This chapter transformed the project from planning into a real enterprise-ready multi-module Maven application.

---

## README Documentation Progress

* ✅ Part 1 - Project Overview
* ✅ Part 2 - Chapter 01 Documentation
* ✅ Part 3 - Chapter 02 Documentation
* ✅ Part 4 - Chapter 03 Documentation
* ⏳ Part 5 - Chapter 04 Documentation (Common Library Development)
* ⏳ Part 6 - Final Project Guide, Roadmap & Repository Standards
  ============================================================================================

# README.md - Part 5

# Chapter 04 - Common Library Development (Production Foundation)

---

# Chapter Objective

This chapter focused on building a reusable production-grade Common Library that can be shared across all microservices.

Instead of writing duplicate code inside every service, all reusable components were centralized into a dedicated Maven module called **common-lib**.

The objective was to build a strong enterprise foundation before starting the first microservice.

---

# Why common-lib?

In large enterprise applications, many classes are reused across services.

Examples include:

* Standard API Response Models
* Error Response Models
* Common Exceptions
* Utility Classes
* Validation Helpers
* Constants
* Shared Enums

Without a common library, every service would duplicate the same code.

This increases maintenance cost and introduces inconsistency.

---

# Responsibilities of common-lib

The common library is responsible for:

* Shared DTOs
* Generic API Responses
* Shared Exceptions
* Validation Utilities
* Utility Classes
* Constants
* Reusable Enums

It intentionally does **not** contain:

* Business Logic
* Controllers
* Services
* Repositories
* Security Configuration
* Database Configuration

This separation keeps the library lightweight and reusable.

---

# Package Structure

```text id="4frd7a"
common-lib
│
└── src
    └── main
        └── java
            └── com.ecommerce.common
                │
                ├── constants
                │
                ├── dto
                │
                ├── enums
                │
                ├── exception
                │
                ├── util
                │
                └── config
```

Every package has a single responsibility.

---

# ApiResponse<T>

A generic response wrapper was created to standardize every successful REST API response.

Benefits:

* Consistent API format
* Generic payload support
* Reusable response model
* Easier frontend integration
* Cleaner controller code

Example response:

```json
{
  "success": true,
  "message": "User created successfully.",
  "data": {
    "id": 101,
    "name": "Asif"
  }
}
```

---

# Generic Programming

Instead of creating multiple response classes:

```text id="upv05u"
UserResponse

ProductResponse

OrderResponse
```

A single generic class was used:

```text id="rgptj6"
ApiResponse<T>
```

This makes the library scalable and reusable.

---

# ResponseStatus Enum

A dedicated enum was introduced for standardized response states.

Current design:

* SUCCESS
* FAILED

Future expansion may include:

* WARNING
* PARTIAL_SUCCESS
* PROCESSING

Using enums avoids hardcoded strings throughout the project.

---

# Common Constants

Reusable application constants were centralized inside:

```text id="hj6zlh"
AppConstants
```

Purpose:

* Remove magic strings
* Improve maintainability
* Centralize reusable values

Only application-wide constants belong here.

Feature-specific constants should remain inside their own modules.

---

# Exception Hierarchy

A scalable exception hierarchy was designed.

```text id="u1j4xa"
RuntimeException

↓

BusinessException

├── ResourceNotFoundException

└── ValidationException
```

Benefits:

* Cleaner error handling
* Better readability
* Easier exception mapping
* Future extensibility

---

# BusinessException

Acts as the base exception for all business-related failures.

Examples:

* Duplicate email
* Invalid operation
* Business rule violation

Every future business exception will inherit from this class.

---

# ResourceNotFoundException

Used when requested data is unavailable.

Examples:

* User not found
* Product not found
* Order not found

Instead of returning null values, meaningful exceptions improve API quality.

---

# ValidationException

Used when client input is invalid.

Examples:

* Invalid email
* Invalid mobile number
* Weak password
* Required field missing

Keeping validation exceptions separate from business exceptions improves clarity.

---

# ErrorResponse DTO

A dedicated response model was created for error responses.

It contains information such as:

* Timestamp
* HTTP Status
* Error Code
* Error Message
* Request Path

Keeping ErrorResponse separate from ApiResponse follows enterprise API design standards.

---

# Global Exception Handling

The architecture was designed around a centralized exception handling approach.

The actual implementation will be revisited inside running microservices.

Benefits:

* Centralized error handling
* Consistent error responses
* Cleaner controllers
* Better maintainability

---

# Utility Classes

Reusable utility classes were created.

Current utilities:

```text id="z73q2q"
DateTimeUtil

StringUtil

ValidationUtil
```

Each utility class has a single responsibility.

---

# DateTimeUtil

Responsibilities:

* Current Date-Time
* Date Formatting
* Future reusable date helpers

The design follows static utility principles.

---

# StringUtil

Responsibilities:

* Blank checks
* Common string helper methods
* Reusable string operations

This prevents repeated string validation across services.

---

# ValidationUtil

Responsibilities:

* Email Validation
* Mobile Validation
* Password Validation

Validation logic is centralized to ensure consistency across all APIs.

---

# Regex Optimization

Validation patterns use precompiled Pattern objects.

Benefits:

* Better performance
* Reusable compiled patterns
* Thread-safe usage
* Reduced object creation

This follows enterprise Java best practices.

---

# Logging Design Decision

One important architectural decision was taken.

A generic LoggingUtil class was intentionally **not** created.

Reason:

Logging is class-specific.

Each service should maintain its own logger instance.

Enterprise approach:

```text id="ryzb9f"
UserService Logger

ProductService Logger

OrderService Logger
```

Instead of:

```text id="3nhxx4"
LoggingUtil.log(...)
```

---

# Common Configuration Decision

Another important architectural decision.

The common library should **not** contain unnecessary Spring Boot configuration.

Configuration should only be added when there is an actual reusable requirement.

This keeps common-lib framework-independent and lightweight.

---

# Maven Module Consumption

Future services consume common-lib through Maven dependency.

Flow:

```text id="vwf3wb"
common-lib

↓

JAR

↓

Local Repository

↓

user-service

↓

auth-service

↓

product-service
```

No source-code duplication is required.

---

# Internal Build Flow

```text id="xv8vle"
Source Code

↓

Compile

↓

Package

↓

common-lib.jar

↓

Local Maven Repository

↓

Future Services
```

This is the core of Maven Multi Module architecture.

---

# Production Standards Followed

Throughout this chapter the following standards were maintained:

* Single Responsibility Principle
* Reusable Design
* Generic Programming
* Exception Hierarchy
* Utility Isolation
* Incremental Development
* Compile Verification
* Enterprise Package Structure
* Requirement-driven Design

---

# Common Mistakes Avoided

* Creating one large CommonUtil class
* Using magic strings
* Duplicating validation logic
* Logging sensitive information
* Adding unnecessary Spring configuration
* Returning raw exceptions to clients
* Mixing business logic with utilities

---

# Best Practices

* Build reusable components.
* Keep utilities focused.
* Separate business and validation exceptions.
* Use generic programming where appropriate.
* Verify builds after every implementation.
* Keep configuration minimal.
* Design for future extensibility.

---

# Interview Questions

1. Why do enterprise projects use a common library?
2. What is Generic Programming?
3. Why use ApiResponse<T>?
4. Why separate ErrorResponse from ApiResponse?
5. Why should utility classes be final?
6. Why use a private constructor in utility classes?
7. Explain the exception hierarchy.
8. Why extend RuntimeException?
9. Why precompile regex patterns?
10. Why should LoggingUtil generally be avoided?
11. Why avoid unnecessary configuration in common libraries?
12. How does a microservice consume common-lib?

---

# Chapter 04 Deliverables

✅ Production Package Structure

✅ Generic ApiResponse

✅ ResponseStatus Enum

✅ AppConstants

✅ BusinessException

✅ ResourceNotFoundException

✅ ValidationException

✅ ErrorResponse

✅ Utility Classes

✅ Validation Utilities

✅ Logging Architecture

✅ Configuration Decisions

✅ Maven Module Integration

---

# Chapter 04 Status

**Completed Successfully**

The project now has a production-ready reusable foundation that will be shared across every future microservice.

---

# Overall Project Progress

```text id="j6ojtu"
Phase 1 - Project Planning               ✅ Completed

Phase 2 - Maven Foundation               ✅ Completed

Phase 3 - Project Foundation             ✅ Completed

Phase 4 - Common Library                 ✅ Completed

Phase 5 - Auth Service                   🔄 Starting Next
```

---

## README Documentation Progress

* ✅ Part 1 - Project Overview
* ✅ Part 2 - Chapter 01
* ✅ Part 3 - Chapter 02
* ✅ Part 4 - Chapter 03
* ✅ Part 5 - Chapter 04 (Common Library)
* ⏳ Part 6 - Final Repository Guide, Development Workflow, Git Strategy & Future Roadmap
  ============================================================================================

# README.md - Part 6 (Final)

# Development Workflow, Standards & Future Roadmap

---

# Enterprise Development Workflow

This project is developed using the same workflow followed in enterprise software companies.

Every feature follows the sequence below:

```text
Business Requirement

↓

Requirement Analysis

↓

Architecture Design

↓

Package Structure

↓

Class Design

↓

Implementation

↓

Compile Verification

↓

Testing

↓

Git Commit

↓

Code Review

↓

Deployment
```

This ensures that every feature is designed before implementation.

---

# Coding Standards

The following coding standards are followed throughout the project.

## General Rules

* Use meaningful class names.
* Use meaningful method names.
* Avoid duplicate code.
* Keep methods small and focused.
* Follow Single Responsibility Principle.
* Prefer composition over duplication.
* Keep business logic inside the service layer.
* Never hardcode reusable values.

---

## Package Naming Convention

```text
com.ecommerce
```

Example:

```text
com.ecommerce.auth.controller

com.ecommerce.auth.service

com.ecommerce.auth.repository

com.ecommerce.common.dto

com.ecommerce.common.util
```

Package names remain lowercase for consistency.

---

## Class Naming Convention

Examples:

```text
UserService

ProductController

OrderRepository

ValidationUtil

DateTimeUtil

BusinessException
```

Class names always follow PascalCase.

---

## Method Naming Convention

Examples:

```text
registerUser()

login()

createOrder()

findByEmail()

isValidEmail()

formatDate()
```

Method names follow camelCase.

---

## Constant Naming Convention

Examples:

```text
SUCCESS

FAILED

DEFAULT_PAGE_SIZE

JWT_PREFIX
```

Constants use uppercase with underscores.

---

# Git Workflow

Each completed feature should be committed separately.

Recommended flow:

```bash
git status

git add .

git commit -m "Meaningful commit message"

git push origin main
```

Example commit messages:

```text
feat(common-lib): add generic API response

feat(auth): create authentication module

fix(validation): improve email validation

refactor(common): simplify utility methods
```

Small, focused commits make code reviews easier.

---

# Build Instructions

Clone the repository:

```bash
git clone <repository-url>
```

Move to the project directory:

```bash
cd ecommerce-parent
```

Build the complete project:

```bash
mvn clean install
```

Run an individual microservice:

```bash
mvn spring-boot:run
```

Always execute builds from the parent project whenever possible.

---

# Project Modules

The planned microservices are:

| Module               | Responsibility            |
| -------------------- | ------------------------- |
| common-lib           | Shared reusable library   |
| auth-service         | Authentication & JWT      |
| user-service         | User management           |
| product-service      | Product catalogue         |
| cart-service         | Shopping cart             |
| inventory-service    | Inventory management      |
| order-service        | Order processing          |
| payment-service      | Payment integration       |
| notification-service | Email & notifications     |
| api-gateway          | API gateway               |
| discovery-server     | Service discovery         |
| config-server        | Centralized configuration |

Each service has a single responsibility.

---

# Learning Roadmap

The complete learning journey is divided into chapters.

| Chapter                              | Status      |
| ------------------------------------ | ----------- |
| Chapter 01 - Planning & Architecture | ✅ Completed |
| Chapter 02 - Maven                   | ✅ Completed |
| Chapter 03 - Parent Project Setup    | ✅ Completed |
| Chapter 04 - Common Library          | ✅ Completed |
| Chapter 05 - Auth Service            | 🔄 Next     |
| Chapter 06 - User Service            | ⏳ Planned   |
| Chapter 07 - Product Service         | ⏳ Planned   |
| Chapter 08 - Inventory Service       | ⏳ Planned   |
| Chapter 09 - Cart Service            | ⏳ Planned   |
| Chapter 10 - Order Service           | ⏳ Planned   |
| Chapter 11 - Payment Service         | ⏳ Planned   |
| Chapter 12 - Notification Service    | ⏳ Planned   |
| Chapter 13 - API Gateway             | ⏳ Planned   |
| Chapter 14 - Config Server           | ⏳ Planned   |
| Chapter 15 - Discovery Server        | ⏳ Planned   |
| Chapter 16 - Docker                  | ⏳ Planned   |
| Chapter 17 - Kafka                   | ⏳ Planned   |
| Chapter 18 - Redis                   | ⏳ Planned   |
| Chapter 19 - Monitoring              | ⏳ Planned   |
| Chapter 20 - Production Deployment   | ⏳ Planned   |

---

# Skills Covered

By the end of this project, the following skills will be covered:

## Java

* Core Java
* Collections
* Generics
* Exception Handling
* Multithreading Basics
* Java 21 Features

---

## Spring

* Spring Core
* Spring Boot
* Spring Data JPA
* Spring Security
* Spring Validation

---

## Cloud

* Spring Cloud
* Config Server
* Eureka
* API Gateway
* OpenFeign

---

## Database

* MySQL
* JPA
* Hibernate

---

## Messaging

* Apache Kafka

---

## Caching

* Redis

---

## DevOps

* Docker
* Docker Compose
* Git
* GitHub

---

## Monitoring

* Spring Boot Actuator
* Prometheus
* Zipkin

---

# Interview Preparation

The project is intentionally designed to prepare for enterprise Java backend interviews.

Every chapter includes:

* Internal Working
* JVM Concepts
* Spring Internals
* Real Office Scenarios
* Compile Verification
* Best Practices
* Common Mistakes
* Interview Questions
* Practical Assignments

This approach builds both practical and theoretical understanding.

---

# Enterprise Principles Followed

Throughout the project, the following principles are applied:

* SOLID Principles
* DRY (Don't Repeat Yourself)
* KISS (Keep It Simple, Stupid)
* Single Responsibility Principle
* Layered Architecture
* Reusable Components
* Loose Coupling
* High Cohesion
* Incremental Development
* Clean Code Practices

---

# Repository Guidelines

* Build from the parent project.
* Keep dependencies updated.
* Follow naming conventions.
* Do not commit generated files.
* Write meaningful commit messages.
* Verify builds before pushing.
* Keep reusable code inside common-lib.
* Avoid unnecessary complexity.

---

# Future Enhancements

Possible future improvements include:

* Refresh Token Support
* Role-Based Authorization
* OAuth2 Integration
* Distributed Caching
* Circuit Breaker
* Centralized Logging
* Distributed Tracing
* Kubernetes Deployment
* CI/CD Pipelines
* Performance Testing
* Integration Testing
* Unit Testing
* API Versioning
* Rate Limiting

---

# Learning Outcome

This repository is intended to provide much more than a working application.

It demonstrates:

* Enterprise Architecture Thinking
* Production Code Organization
* Scalable Project Structure
* Real Development Workflow
* Maintainable Code Design
* Modern Java Backend Practices

The objective is to understand **why** something is built before learning **how** it is coded.

---

# Acknowledgements

This project is developed as a structured learning journey to understand enterprise backend development from first principles.

Every chapter builds upon the previous one, allowing gradual progression from beginner concepts to production-grade microservices.

---

# Current Status

```text
Project Name:
Enterprise E-commerce Microservices Backend

Current Phase:
Chapter 05 - Auth Service

Project Foundation:
100% Complete

Common Library:
Completed

Next Milestone:
Production-grade Authentication Service
```

---

# Final Repository Vision

By the end of this journey, the repository will contain a complete enterprise-grade microservices ecosystem with production-quality architecture, reusable components, modern development practices, and comprehensive documentation.

It is intended to serve as:

* A learning resource
* A portfolio project
* An interview preparation reference
* A reusable enterprise architecture template

---

# README Status

```text
README Documentation

✅ Part 1 - Project Overview

✅ Part 2 - Chapter 01

✅ Part 3 - Chapter 02

✅ Part 4 - Chapter 03

✅ Part 5 - Chapter 04

✅ Part 6 - Development Workflow & Roadmap

Status:
COMPLETE
```

