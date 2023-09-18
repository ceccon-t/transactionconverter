# Transaction Converter

## Description

The purpose of this system is to allow the conversion of the value of purchase transactions, originally registered in US dollars, to other currencies. For this, it allows the storing of new transactions, the viewing of stored transactions and the conversion of transactions previously stored.

The application is a web service that uses the [Treasury Reporting Rates of Exchange API](https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange) as its data source. The service can be accessed through HTTP requests.

In order to get positive results, make sure you are utilizing currencies that are supported by the Treasury Reporting Rates of Exchange API. Also, conversions will only be performed if there is an exchange rate recorded for at most 6 months prior to the date of the purchase.


## Technical Summary

### Main frameworks and tools

The project is developed in Java, using the Spring Boot framework and Maven as its build tool. For persistence, an H2 database is used, configured to store its data in the filesystem of the host, so as to maintain persistence across restarts of the application (the data is saved to the `persistence` folder in the working directory from which the application was run).

### Automations

The project uses Github Actions to run all tests and build the system whenever the `main` branch receives a new push. An artifact with the resulting jar is also stored for each run of this workflow. The script with the pipeline definition can be found at `.github/workflows/ga-pipeline.yml`.

The version deployed on Render is configured to be updated whenever the `main` branch is updated, as well. It does this by buiding a Docker image from the Dockerfile in the repository and then deploying it as a web service.

### Code organization

To get a short intro to how the code is organized, you can check `architecture.md`.


## How to use

The easiest way to explore the system and learn its capabilities is through its OpenAPI live documentation. If you are running the system locally, this can be found by going to [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) . Alternatively, you can access an online version of it on [Render](https://tc-transaction-converter.onrender.com/swagger-ui.html).

The live documentation offers samples of the necessary requests and endpoints to interact with the system purely through HTTP calls. With them, the system can be used with tools such as Postman or curl.

The most relevant endpoints are the following:

GET `/api/v1/transaction`: list all stored transactions.

POST `/api/v1/transaction`: create and store a new transaction.

GET `/api/v1/conversion/countryAndCurrency/{transactionId}/{country}/{currency}`: get a conversion of the transaction with id {transactionId} to the specific currency of the desired country.


## How to run the application

There are a few ways to achieve this:

### With Maven

The repository comes with a Maven wrapper, so it can be started by simply cloning or downloading the files and running the command `./mvnw spring-boot:run` while at the root of the project (for Unix-like environments, or the equivalent with mvnw-cmd on Windows).

### With Docker

The project contains a Dockerfile that generates a Docker image for the application. To build it, run `docker build -t transactionconverter .` to generate the image, and then `docker run -it -p 8080:8080 transactionconverter` to start a container from it.

### Other alternatives

Since it is a Spring Boot project, the application can also be run from your preferred IDE. Or you can access it online at [https://tc-transaction-converter.onrender.com/](https://tc-transaction-converter.onrender.com/swagger-ui.html) (just please be aware that this instance is open to everyone, so data can change at any time due to other users interacting with the system).


## How to build the project

This is a Maven project, so the easiest way to build it is running `./mvnw clean package` in the root folder. The jar resulting from the build will be located inside the `target` folder that is generated.


## How to run the automated tests

The simplest way is to run `./mvnw verify` in the main folder of the application (or the equivalent using `mvnw-cmd` for Windows environments).

