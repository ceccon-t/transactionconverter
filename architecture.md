# Transaction Converter - Architecture

## Structure

### Code overview

The code is structured by layers, with packages for controllers, services, repositories, and so on. The main entity is `Transaction`, which models a purchase transaction recorded in dollars and is persisted to the database. The other main concept used is that of a `ConvertedTransaction`, which is just a version of a Transaction enriched with information about the conversion of its value to a different currency. While Transaction is the only entity persisted, its service layer is fairly simple, with most of the complexity being in the service layer of the ConvertedTransaction.

The logic that consumes the Treasury API is bundled in the `integration.ustreasuryapi` package. In order to find a conversion rate that fits the criteria of being less than or equal to the purchase transaction date and no more than 6 months prior to that, the request to their API is constructed with the necessary limits on the dates and sorting from most recent to least recent, so as to bring the most adequate rate as the first element of the returned list. If the list is empty, the system informs that no rate matched the criteria, and as such the conversion is not made.

The endpoints are defined in the `controllers` package.


## Automated Tests

Tests are written with JUnit 5, and the source files can be found under `src/test` hierarchy. There are unit tests for each layer, using Mockito to mock dependencies, as well as a couple of tests that bring up the entire Spring Boot context to validate the main use cases of the system.

To execute all tests, run `./mvnw verify` in the main folder of the application (or the equivalent using `mvnw-cmd` for Windows environments).

## Libraries and Frameworks

[Spring Boot](https://spring.io/guides/gs/spring-boot/) for dependency injection and inversion of control.

[Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) as a build automation tool.

[JUnit](https://junit.org/junit5/docs/current/user-guide/) for automated tests.

[H2](https://www.h2database.com/html/tutorial.html) as a database.

[OpenAPI](https://swagger.io/tools/open-source/getting-started/) for the live documentation.

