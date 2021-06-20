package com.james.weather.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getWeather_validRequest_returnSuccess() throws Exception {
        mockMvc.perform(get("/weather")
                .param("city", "city")
                .param("country", "country"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("city"))
                .andExpect(jsonPath("$.country").value("country"))
                .andExpect(jsonPath("$.description").value(""));
    }

    @Test
    public void getWeather_missingCity_returnError() throws Exception {
        mockMvc.perform(get("/weather")
                .param("country", "country"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Required request parameter 'city' for method parameter type String is not present"));
    }

    @Test
    public void getWeather_emptyCity_returnError() throws Exception {
        mockMvc.perform(get("/weather")
                .param("city", "")
                .param("country", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("getWeather.city: must not be blank"));
    }
}
