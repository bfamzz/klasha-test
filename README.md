# Klasha (_Backend APIs_)
## Technologies
- Spring Boot
- Docker

## Installation / How to run
- Clone the project files
- At the root of the project directory run the following command:
```shell
./mvnw spring-boot:run
```

## Swagger-ui Documentation
Available at [http://localhost:8080/klasha-api/swagger-ui.html](http://localhost:8080/klasha-api/swagger-ui.html)

## Endpoints
```sh
http://localhost:8080/api/v1/countryTopCities                               [GET]
http://localhost:8080/api/v1/countryData                                    [GET]
http://localhost:8080/api/v1/countryStatesAndCities                         [GET]
http://localhost:8080/api/v1/convertCurrency                                [GET]
```

__countryTopCities (default countries are: Italy, New Zealand & Ghana)__

```sh
http://localhost:8080/api/v1/countryTopCities                            [GET]

Query parameters:
  - number_of_cities [integer]
```

__country data (population, capital city, etc.)__

```sh
http://localhost:8080/api/v1/countryData                            [POST]

Query parameters:
  - country [string]
```

__country states and cities__

```sh
http://localhost:8080/api/v1/countryStatesAndCities                            [POST]

Query parameters:
  - country [string]
```

__convert currency__

```sh
http://localhost:8080/api/v1/convertCurrency                            [GET]

Query parameters:
  - country           [string]
  - amount            [double]
  - targetCurrency    [string]
```