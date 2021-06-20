package com.james.weather.controller;

import com.james.weather.dto.WeatherDto;
import com.james.weather.exception.RateLimitExceededException;
import com.james.weather.service.RateLimitService;
import com.james.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@RestController
@Validated
@RequiredArgsConstructor
public class WeatherController {

    private final RateLimitService rateLimitService;

    private final WeatherService weatherService;

    @GetMapping("/weather")
    public ResponseEntity<WeatherDto> getWeather(
            @RequestParam("city") @NotBlank String city,
            @RequestParam("country") @NotBlank String country,
            @RequestParam("apikey") @NotBlank String apikey
    ) {
        if (rateLimitService.isLimitExceeded(apikey, OffsetDateTime.now())) {
            throw new RateLimitExceededException();
        }

        return ResponseEntity.ok(weatherService.getWeather(city,country));
    }
}
