# Collabsoft

- [Collabsoft](#collabsoft)
  - [Prérequis](#prérequis)
  - [Setup](#setup)
  - [Run](#run)
  - [Tests](#tests)
  - [Build](#build)

![Coverage](/badges/jacoco.svg)
![Branches](/badges/branches.svg)

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
