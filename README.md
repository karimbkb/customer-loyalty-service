# customer-loyalty-service
>  this microservice takes care of customers loyalty points

[![codecov](https://codecov.io/gh/karimbkb/customer-loyalty-service/graph/badge.svg?token=JP8KXT5HCG)](https://codecov.io/gh/karimbkb/customer-loyalty-service)

![build](https://github.com/karimbkb/customer-loyalty-service/actions/workflows/build.yml/badge.svg?branch=master)

## Contents

- [Setup](#setup)
- [Dependencies](#dependencies)
- [Endpoints](#endpoints)
- [OpenAPI](#openapi)
- [Tests](#tests)
- [Code Coverage](#code-coverage)
- [Static Code Analyzer](#static-code-analyzer)

## Setup

Go into the root directory of the application and run

```
cd customer-loyalty-service
docker build -f init/Dockerfile -t customer-loyalty-service .
docker-compose up -d
```

Start the application via you IDE or run the jar file and you can access the api at  `http://localhost:8080/`

## Dependencies

- Java 17
- Kotlin 1.9.22
- Spring Boot 2.7
- Maven
- Postgresql
- Flyway
- JUnit 
- Test Containers
- Detekt
- Ktlint

## Endpoints

| Action               | Endpoint                                                            | Type     | Example                                                                       | Payload                                   |
|----------------------|---------------------------------------------------------------------|----------|-------------------------------------------------------------------------------|-------------------------------------------|
| Get points history by id           | `/api/v1/points-history/{id}`                         | `GET`    | `/api/v1/points-history/08915530-12d5-4d4e-9edf-5339c523ed29`             | -                                         |
| Get points history by customer id           | `/api/v1/points-history/customer/{customerId}`                         | `GET`    | `/api/v1/points-history/customer/08915530-12d5-4d4e-9edf-5339c523ed29`             | -                                         |
| Delete points by id        | `/api/v1/points-history/{id}`                              | `DELETE` | `/api/v1/points-history/08915530-12d5-4d4e-9edf-5339c523ed29`               | -                                         |
| Create new points | `/api/v1/points-history/`                               | `POST`   | `/api/v1/points-history/`                | `{ "customerId": "419d5f27-758e-4aaa-81ee-ecf27074eaf3", "points": 6, "transactionType": "ADD", "loyaltyType": "ORDER", "reason": "Order with number #436272986" }`                                  |

## OpenAPI

SwaggerUI can be accessed through: http://localhost:8080/swagger-ui/

## Tests

To execute Unit Tests run:

```
mvn test
```

## Code Coverage

To check if the code coverage ratio was reached run this command:

**INFO: You have to run `mvn test` first!**

```
mvn jacoco:report
```

## Static Code Analyzer

Detekt was used in this project. (https://github.com/detekt/detekt)

```
mvn detekt:check
```