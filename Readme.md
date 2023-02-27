# Parking system backend

The application allow users to authenticate them in the parking system application and let them interact with parkings (occuping and freeing them).

## Technologies

System is implemented using Ktor framework as application server and his plugins for authentication, routing and serialization.

The application uses two databases, one for parking slots and one for users. Both databases are hosted by MongoDB Cloud.

User authentication was implemented using a JWT token that encode the user id (his email).

## Installation

This project uses Gradle as build automation tool, so to run the application just clone the repository and run:

```
gradlew run
```