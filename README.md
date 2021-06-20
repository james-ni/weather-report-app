# Weather Report Application

A Spring Boot application that fronts the OpenWeatherMap service. 

e.g. http://samples.openweathermap.org/data/2.5/weather?q=London,uk

## Run the application

Run application from your terminal with Gradle. For Mac users, you can use below commands. It would be similar but slightly different on Windows.

```bash
./gradlew bootrun
```

This will start a server running at port 8081. You can access the weather front API via below command.
```bash
curl --location --request GET 'http://localhost:8081/weather?city=london&country=uk&apikey=apikey_1'
```

Or you can use Postman or any HTTP client to test the API.

For demo purpose, 2 API key (`apikey_1`, `apikey_2`) has been seeded in database via flyway script.



## Implementation

This application implemented following features:
- Report weather information for a given city/country combination. The response payload is in JSON format. (below is a sample payload)
    ```json
    {
      "city": "melburne",
      "country": "au",
      "description": "scattered clouds"
    }
    ```
- Rate limit the request to weather API. Default to 5 request per API Key. Can be configure to different rate.
- Cache the response from Openweathermap API to H2 database. Default TTL set to 60 minutes. Also configurable.
- REST style API using status code + error message to provide meaningful response
- Database migration script using flyway

## Configuration

- change cache TTL: `app.cache-expiration-in-minutes` in application.yml file
- replace Openweathermap API key: `openweathermap.api.apikey` in application.yml file
- change rate limit: rate limit is stored in database at apikey level

## Tech Stack

- Java 11
- SpringBoot
- Spring Data JPA
- Lombok
- Mockito/MockMvc
- Wiremock
- Flyway