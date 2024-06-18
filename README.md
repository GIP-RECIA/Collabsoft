# Collabsoft

- [Collabsoft](#collabsoft)
  - [Prérequis](#prérequis)
  - [Setup](#setup)
  - [Run](#run)
  - [Tests](#tests)
  - [Build](#build)

[![Coverage](https://raw.githubusercontent.com/GIP-RECIA/Collabsoft/badges/jacoco.svg)](https://github.com/GIP-RECIA/Collabsoft/actions/workflows/badges.yml)
[![Branches](https://raw.githubusercontent.com/GIP-RECIA/Collabsoft/badges/branches.svg)](https://github.com/GIP-RECIA/Collabsoft/actions/workflows/badges.yml)

## Prérequis

- [nvm](https://github.com/nvm-sh/nvm)
- [sdkman](https://sdkman.io)

## Setup

```sh
./scripts/init.sh
```

Personnalisez les fichiers :

- `.env.local`
- `src/main/resources/config/application-dev.yml`

## Run

```sh
yarn dev
./mvnw clean spring-boot:run -Pdev
```

## Tests

**Prérequis** :

- docker
- mysql-client-core-8.0

```sh
./scripts/test.sh
```

## Build

```sh
./scripts/build.sh
```
