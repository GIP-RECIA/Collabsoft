# Collabsoft

- [Collabsoft](#collabsoft)
  - [Prérequis](#prérequis)
  - [Setup](#setup)
  - [Run](#run)
  - [Build](#build)

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

## Build

```sh
./scripts/build.sh
```
