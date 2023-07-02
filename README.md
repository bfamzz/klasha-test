# Klasha (_Backend APIs_)
## Technologies
- Spring Boot
- Docker

## How to run
- Clone the project files
- At the root of the project directory run the following command:
```shell
./mvnw spring-boot:run
```

## How to run (with Docker)
```shell
docker run --name klasha-test -p 8080:8080 -it bfamz/klasha-test:latest
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
http://localhost:8080/api/v1/countryTopCities                       [GET]

Query parameters:
  - number_of_cities [integer]
```

__country data (population, capital city, etc.)__

```sh
http://localhost:8080/api/v1/countryData                            [GET]

Query parameters:
  - country [string]
```

__country states and cities__

```sh
http://localhost:8080/api/v1/countryStatesAndCities                 [GET]

Query parameters:
  - country [string]
```

__convert currency__

```sh
http://localhost:8080/api/v1/convertCurrency                         [GET]

Query parameters:
  - country           [string]
  - amount            [double]
  - targetCurrency    [string]
```

## Observation
* There is a failing case from the external API for getting the cities in `Lagos State, Nigeria`. The input to the endpoint is an output from another endpoint. Additional parsing/formatting was required to make the failing case pass.


* The external API has backward compatibility. However, the new API endpoint should be clearly stated in the documentation for new services dependent on the API as not all http clients are configured to follow redirects.

## Recommendation
* Caching - The results should be cached in an in-memory datastore (e.g. Redis) so that subsequent calls to the endpoint will result in faster response time.