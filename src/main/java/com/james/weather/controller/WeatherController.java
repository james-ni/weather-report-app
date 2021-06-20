package com.james.weather.controller;

import com.james.weather.dto.WeatherDto;
import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class WeatherController {
    @GetMapping("/weather")
    public ResponseEntity<WeatherDto> getWeather(@RequestParam("city") @NotBlank String city,
                                                 @RequestParam("country") @NotNull String country) {
        return ResponseEntity.ok(new WeatherDto(city, country, ""));
    }
}
