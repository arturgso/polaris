# Polaris

Polaris is a private family planner for shopping lists, gift lists, and a vault for surprise items.

## Português

### Visão geral

Este projeto é uma API Spring Boot para organizar:

- listas de compras;
- listas de presentes;
- pessoas vinculadas aos presentes;
- itens secretos no vault para manter surpresas fora do acesso normal.

### Stack

- Java 25
- Spring Boot 4.0.6
- Gradle
- PostgreSQL
- Flyway
- Spring Security + JWT
- Springdoc OpenAPI

### Funcionalidades

- autenticação com login por senha usando o usuário bootstrap;
- CRUD de pessoas;
- CRUD de presentes e listas de presentes;
- CRUD de itens e listas de compras;
- filtros por status, evento, categoria e título;
- vault com token próprio para itens secretos.

### Endpoints principais

- `POST /api/v1/auth/login`
- `GET /api/v1/persons`
- `GET /api/v1/gifts`
- `GET /api/v1/gift-lists`
- `GET /api/v1/shopping-items`
- `GET /api/v1/shopping-lists`
- `POST /api/v1/vault/unlock`
- `GET /api/v1/vault/gifts`
- `GET /api/v1/vault/gift-lists`
- `GET /api/v1/vault/shopping-items`
- `GET /api/v1/vault/shopping-lists`

### Vault

O vault protege itens marcados como secretos. O fluxo é:

1. autenticar com uma conta ADMIN;
2. chamar `POST /api/v1/vault/unlock` com a senha do vault;
3. usar o token retornado no header `X-Vault-Token` nas requisições do vault.

O login recebe apenas senha e autentica sempre com o usuário definido em `BOOTSTRAP_ADMIN_USERNAME`.

Para alterar ou remover um item secreto diretamente, envie também `X-Vault-Password`; ele só valida a senha e não abre o vault.

### Configuração local

Variáveis de ambiente principais:

| Variável | Uso |
| --- | --- |
| `DB_HOST` | host do PostgreSQL |
| `DB_PORT` | porta do PostgreSQL |
| `DB_NAME` | nome do banco |
| `DB_SCHEMA` | schema do banco |
| `DB_USER` | usuário do banco |
| `DB_PASSWORD` | senha do banco |
| `CORS_ALLOWED_ORIGINS` | origens liberadas no frontend |
| `APP_DB_POPULATE_ENABLED` | popula dados iniciais |
| `JWT_SECRET` | segredo do JWT |
| `JWT_EXPIRATION_SECONDS` | expiração do token normal |
| `BOOTSTRAP_ADMIN_USERNAME` | username do admin em memória |
| `BOOTSTRAP_ADMIN_PASSWORD` | senha do admin em memória |
| `VAULT_PASSWORD` | senha para liberar o vault |

### Executando

```bash
./gradlew test
./gradlew build
./gradlew bootRun
```

### Versionamento automático

- A workflow `Version Bump` roda em push para `master` (ou manualmente).
- Ela incrementa a versão **minor** no `build.gradle` (ex.: `1.0.0` -> `1.1.0`), cria commit de release e gera tag `vX.Y.Z`.
- Incremento **major** permanece manual.

### Estrutura

- `src/main/java/io/vexis/polaris` — aplicação e módulos de domínio
- `src/main/resources/db/migration` — migrations Flyway
- `src/test/java/io/vexis/polaris` — testes de integração

---

## English

### Overview

This project is a Spring Boot API for organizing:

- shopping lists;
- gift lists;
- people linked to gifts;
- secret vault items used to keep surprises hidden from normal access.

### Stack

- Java 25
- Spring Boot 4.0.6
- Gradle
- PostgreSQL
- Flyway
- Spring Security + JWT
- Springdoc OpenAPI

### Features

- password-only login authentication using the bootstrap user;
- people CRUD;
- gift and gift list CRUD;
- shopping item and shopping list CRUD;
- filters by status, event, category, and title;
- vault access with its own token for secret items.

### Main endpoints

- `POST /api/v1/auth/login`
- `GET /api/v1/persons`
- `GET /api/v1/gifts`
- `GET /api/v1/gift-lists`
- `GET /api/v1/shopping-items`
- `GET /api/v1/shopping-lists`
- `POST /api/v1/vault/unlock`
- `GET /api/v1/vault/gifts`
- `GET /api/v1/vault/gift-lists`
- `GET /api/v1/vault/shopping-items`
- `GET /api/v1/vault/shopping-lists`

### Vault

The vault protects items marked as secret. The flow is:

1. authenticate with an ADMIN account;
2. call `POST /api/v1/vault/unlock` with the vault password;
3. send the returned token in the `X-Vault-Token` header for vault requests.

Login accepts only password and always authenticates with the user configured in `BOOTSTRAP_ADMIN_USERNAME`.

To update or delete a secret item directly, also send `X-Vault-Password`; it only validates the password and does not unlock the vault.

### Local setup

Main environment variables:

| Variable | Purpose |
| --- | --- |
| `DB_HOST` | PostgreSQL host |
| `DB_PORT` | PostgreSQL port |
| `DB_NAME` | database name |
| `DB_SCHEMA` | database schema |
| `DB_USER` | database user |
| `DB_PASSWORD` | database password |
| `CORS_ALLOWED_ORIGINS` | allowed frontend origins |
| `APP_DB_POPULATE_ENABLED` | seeds initial data |
| `JWT_SECRET` | JWT secret |
| `JWT_EXPIRATION_SECONDS` | normal token lifetime |
| `BOOTSTRAP_ADMIN_USERNAME` | in-memory admin username |
| `BOOTSTRAP_ADMIN_PASSWORD` | in-memory admin password |
| `VAULT_PASSWORD` | password that unlocks the vault |

### Run

```bash
./gradlew test
./gradlew build
./gradlew bootRun
```

### Automatic versioning

- The `Version Bump` workflow runs on pushes to `master` (or manually).
- It increments the **minor** version in `build.gradle` (for example `1.0.0` -> `1.1.0`), creates a release commit, and creates a `vX.Y.Z` tag.
- **Major** increments remain manual.

### Structure

- `src/main/java/io/vexis/polaris` — application and domain modules
- `src/main/resources/db/migration` — Flyway migrations
- `src/test/java/io/vexis/polaris` — integration tests
