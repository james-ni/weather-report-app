package com.james.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.james.weather.exception.CityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenWeatherMapService {

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    @Value("${openweathermap.api.apikey}")
    private String apikey;

    private final static ObjectMapper MAPPER = new ObjectMapper();

    public String getWeather(String city, String country) {
        log.info("Read weather information for city: {} and country: {}", city, country);
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(
                    UriComponentsBuilder.fromHttpUrl(apiUrl)
                            .queryParam("q", String.join(",", city, country))
                            .queryParam("appid", apikey)
                            .toUriString(),
                    String.class
            );
            return getDescription(response);
        } catch (HttpServerErrorException | HttpClientErrorException httpClientOrServerEx) {
            if (HttpStatus.NOT_FOUND.equals(httpClientOrServerEx.getStatusCode())) {
                throw new CityNotFoundException(String.format("Weather information not found for City: %s Country: %s", city, country));
            }
            throw new RuntimeException("There is an error when contacting OpenWeather API");
        }
    }

    @SneakyThrows
    private String getDescription(String response) {
        JsonNode weatherInfo = MAPPER.readTree(response);
        return weatherInfo.get("weather").get(0).get("description").asText();
    }


}
