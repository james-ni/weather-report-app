package com.james.weather.service;

import com.james.weather.dto.WeatherDto;
import com.james.weather.model.Apikey;
import com.james.weather.model.Weather;
import com.james.weather.repository.ApikeyRepository;
import com.james.weather.repository.ApikeyUsageRepository;
import com.james.weather.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.security.auth.login.AccountException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;

@SpringBootTest
public class WeatherServiceTest {

    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private OpenWeatherMapService openWeatherMapService;

    @InjectMocks
    private WeatherService weatherService;


    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(weatherService, "cacheExpirationMinutes", 2);
    }

    @Test
    void reportWeatherFromLocal() {
        String city = "city";
        String country = "country";
        String description = "sunny";
        OffsetDateTime refreshedAt = OffsetDateTime.now();

        Weather weather = Weather.builder().city(city).country(country).description(description).refreshedAt(refreshedAt).build();
        when(weatherRepository.findTopByCityAndCountryOrderByRefreshedAtDesc(city,country))
                .thenReturn(Optional.of(weather));

        WeatherDto weatherDto = weatherService.getWeather(city, country);

        assertEquals(weatherDto.getCity(), city);
        assertEquals(weatherDto.getCountry(), country);
        assertEquals(weatherDto.getDescription(), description);
        verifyNoInteractions(openWeatherMapService);
    }

    @Test
    void reportWeatherFromRemote() {
        String city = "city";
        String country = "country";
        String description = "sunny";

        when(weatherRepository.findTopByCityAndCountryOrderByRefreshedAtDesc(city,country)).thenReturn(Optional.empty());
        when(weatherRepository.save(any(Weather.class))).thenAnswer(f -> f.getArgument(0));
        when(openWeatherMapService.getWeather(city,country)).thenReturn(description);

        WeatherDto weatherDto = weatherService.getWeather(city, country);

        assertEquals(weatherDto.getCity(), city);
        assertEquals(weatherDto.getCountry(), country);
        assertEquals(weatherDto.getDescription(), description);
        verify(openWeatherMapService).getWeather(city, country);
        verify(weatherRepository).save(any());
    }

    @Test
    void reportWeatherFromRemote_LocalCacheExpired() {
        String city = "city";
        String country = "country";
        String description = "sunny";
        OffsetDateTime refreshedAt = OffsetDateTime.now().minusMinutes(5);

        Weather weather = Weather.builder().city(city).country(country).description(description).refreshedAt(refreshedAt).build();
        when(weatherRepository.findTopByCityAndCountryOrderByRefreshedAtDesc(city,country)).thenReturn(Optional.of(weather));
        when(weatherRepository.save(any(Weather.class))).thenAnswer(f -> f.getArgument(0));
        when(openWeatherMapService.getWeather(city,country)).thenReturn(description);

        WeatherDto weatherDto = weatherService.getWeather(city, country);

        assertEquals(weatherDto.getCity(), city);
        assertEquals(weatherDto.getCountry(), country);
        assertEquals(weatherDto.getDescription(), description);
        verify(openWeatherMapService).getWeather(city, country);
        verify(weatherRepository).save(any());
    }


}
