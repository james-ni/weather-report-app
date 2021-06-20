package com.james.weather.repository;

import com.james.weather.model.Weather;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class WeatherRepositoryTest {

    @Autowired
    private WeatherRepository weatherRepository;

    private String city = "city";

    private String country = "country";

    private String description = "sunny";

    private Weather weather = null;

    @BeforeEach
    void setUp() {
        weather = weatherRepository.save(
                Weather.builder()
                        .city(city)
                        .country(country)
                        .description(description)
                        .refreshedAt(OffsetDateTime.now())
                        .build()
        );
    }

    @AfterEach
    void tearDown() {
        weatherRepository.deleteById(weather.getId());
    }

    @Test
    void foundWeatherForCity() {
        Optional<Weather> result = weatherRepository.findTopByCityAndCountryOrderByRefreshedAtDesc("city", "country");
        assertEquals(result.get().getDescription(), "sunny");
    }

    @Test
    void weatherNotFoundForCity() {
        Optional<Weather> result = weatherRepository.findTopByCityAndCountryOrderByRefreshedAtDesc("city1", "country");
        assertTrue(result.isEmpty());
    }
}
