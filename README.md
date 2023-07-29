# Task Manager

[![Actions Status](https://github.com/GearSL/java-project-73/workflows/hexlet-check/badge.svg)](https://github.com/GearSL/java-project-73/actions)
[![Maintainability](https://api.codeclimate.com/v1/badges/ab0096defb830d5d0b20/maintainability)](https://codeclimate.com/github/GearSL/java-project-73/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/ab0096defb830d5d0b20/test_coverage)](https://codeclimate.com/github/GearSL/java-project-73/test_coverage)

## About
Task Manager is a task management system similar to [redmine.org](http://www.redmine.org). 
It allows you to set tasks, assign performers and change their statuses. Registration and authentication are required 
to work with the system.

## System requirements
- Java 20;
- Gradle 8.2.1;
- PostgreSQL;

## Setup
```shell
### For start project:
start # start app with dev profile.

start-prod # start app with prod profile.

start-dist # Installs the project as a distribution as-is. Depends on clean and install.

### Another tasks you can see in "Makefile"
```

## Deploy
### Create database
For application need a database that will be used for storage users, tasks and etc.

### Prepare environment variables:
```dotenv
APP_ENV # application profile "dev" or "prod"
DATABASE_HOST
DATABASE_PORT
DATABASE_NAME
JDBC_DATABASE_USERNAME
JDBC_DATABASE_PASSWORD
```

### Start application
Start application using Dockerfile in project root. 