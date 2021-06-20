package com.james.weather.service;

import com.james.weather.dto.WeatherDto;
import com.james.weather.model.Weather;
import com.james.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final OpenWeatherMapService openWeatherMapService;

    @Value("${app.cache-expiration-in-minutes}")
    private long cacheExpirationMinutes;

    public WeatherDto getWeather(String city, String country) {
        OffsetDateTime cacheExpiredAt = OffsetDateTime.now().minusMinutes(cacheExpirationMinutes);
        Weather result = weatherRepository.findTopByCityAndCountryOrderByRefreshedAtDesc(city, country).filter(
                weather -> weather.getRefreshedAt().isAfter(cacheExpiredAt)
        ).orElseGet(() -> refreshWeatherData(city, country));

        return WeatherDto.builder()
                .city(city)
                .country(country)
                .description(result.getDescription())
                .build();
    }

    private Weather refreshWeatherData(String city, String country) {
        log.info("Refreshing weather information for city {} of country {}", city, country);
        String weatherDescription = openWeatherMapService.getWeather(city, country);

        Weather weather = Weather.builder()
                .city(city)
                .country(country)
                .description(weatherDescription)
                .refreshedAt(OffsetDateTime.now())
                .build();
        return weatherRepository.save(weather);
    }
}
