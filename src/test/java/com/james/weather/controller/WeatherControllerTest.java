package com.james.weather.controller;

import com.james.weather.exception.InvalidApikeyException;
import com.james.weather.service.RateLimitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RateLimitService rateLimitService;

    @Test
    public void getWeather_validRequest_returnSuccess() throws Exception {
        when(rateLimitService.isLimitExceeded(anyString(), any(OffsetDateTime.class))).thenReturn(false);

        mockMvc.perform(get("/weather")
                .param("city", "city")
                .param("country", "country")
                .param("apikey", "apikey"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("city"))
                .andExpect(jsonPath("$.country").value("country"))
                .andExpect(jsonPath("$.description").value(""));
    }

    @Test
    public void getWeather_missingCity_returnError() throws Exception {
        when(rateLimitService.isLimitExceeded(anyString(), any(OffsetDateTime.class))).thenReturn(false);

        mockMvc.perform(get("/weather")
                .param("country", "country")
                .param("apikey", "apikey"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Required request parameter 'city' for method parameter type String is not present"));
    }

    @Test
    public void getWeather_emptyCity_returnError() throws Exception {
        when(rateLimitService.isLimitExceeded(anyString(), any(OffsetDateTime.class))).thenReturn(false);

        mockMvc.perform(get("/weather")
                .param("city", "")
                .param("country", "country")
                .param("apikey", "apikey"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("getWeather.city: must not be blank"));
    }

    @Test
    public void getWeather_missingApikey_returnError() throws Exception {
        when(rateLimitService.isLimitExceeded(anyString(), any(OffsetDateTime.class))).thenReturn(false);

        mockMvc.perform(get("/weather")
                .param("city", "city")
                .param("country", "country"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Required request parameter 'apikey' for method parameter type String is not present"));
    }

    @Test
    public void getWeather_inValidApikey_returnError() throws Exception {
        when(rateLimitService.isLimitExceeded(anyString(), any(OffsetDateTime.class))).thenThrow(new InvalidApikeyException());

        mockMvc.perform(get("/weather")
                .param("city", "city")
                .param("country", "country")
                .param("apikey", "invalid_apikey"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid Apikey"));
    }

    @Test
    public void getWeather_rateLimitExceeded_returnError() throws Exception {
        when(rateLimitService.isLimitExceeded(anyString(), any(OffsetDateTime.class))).thenReturn(true);

        mockMvc.perform(get("/weather")
                .param("city", "city")
                .param("country", "country")
                .param("apikey", "apikey"))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.message").value("Rate limit exceeded"));
    }
}
