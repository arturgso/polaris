# AGENTS.md

## Purpose

This repository is a Spring Boot API for shopping lists, gift lists, and a private vault used to keep surprise gifts hidden from normal access.

## Tech stack

- Java 25
- Spring Boot 4.0.6
- Gradle
- PostgreSQL
- Flyway
- Spring Security + JWT
- Springdoc OpenAPI

## Important paths

- `src/main/java/io/vexis/polaris/application` — controllers, services, config, security, bootstrap
- `src/main/java/io/vexis/polaris/domain` — entities, DTOs, repositories, interfaces, exceptions
- `src/main/resources/db/migration` — schema history
- `src/test/java/io/vexis/polaris` — integration tests

## Working rules

- Keep the README in Portuguese and English.
- Keep this file in English only.
- Do not run `spotlessApply`; leave formatting changes to the developer.
- Before creating commits, inspect at least the 5 most recent complete commits and match their structure, language, and level of detail.
- Before creating a commit, review the current branch context and changed files for known security gaps, and add TODO comments where future changes should be considered.
- Do not edit code inside the project without asking first; assume the developer prefers to implement changes themselves unless explicitly asked to edit.
- Use the codebase as the source of truth for endpoints, request/response shapes, and security rules.

## Commands

```bash
./gradlew test
./gradlew build
./gradlew bootRun
```

Use `./gradlew spotlessCheck` if you need formatting validation. Do not apply formatting automatically.

## Security notes

- Never commit secrets or local environment values.
- The vault is protected by regular ADMIN authentication plus a vault token.
- Vault requests send the token in `X-Vault-Token`.
- Keep surprise-related data private unless the user explicitly asks otherwise.

## Documentation notes

- Document the actual API routes under `/api/v1`.
- Mention the vault unlock flow when updating user-facing docs.
- Keep examples consistent with the existing controllers and tests.
