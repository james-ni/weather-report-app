package com.james.weather.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.james.weather.exception.CityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OpenWeatherMapServiceTest {
    private static final String API_URL = "http://localhost:9999/weather";
    private static final String API_KEY = "fakeApiKey";

    private WireMockServer wireMockServer;

    @Autowired
    private OpenWeatherMapService openWeatherMapService;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(9999);
        wireMockServer.start();

        ReflectionTestUtils.setField(openWeatherMapService, "apikey", API_KEY);
        ReflectionTestUtils.setField(openWeatherMapService, "apiUrl", API_URL);
    }

    @AfterEach
    public void teardown() {
        wireMockServer.stop();
    }

    @Test
    public void shouldGetWeatherData() {
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/weather?q=city,country&appid=fakeApiKey"))
                .willReturn(aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(" {" +
                                "     \"weather\": [" +
                                "       {" +
                                "         \"id\": 802," +
                                "         \"main\": \"Clouds\"," +
                                "         \"description\": \"sunny\"," +
                                "         \"icon\": \"03d\"" +
                                "       }" +
                                "     ]}")));


        String desc = openWeatherMapService.getWeather("city", "country");
        assertEquals("sunny", desc);
    }

    @Test
    void errorWhenCityNotFound() {
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/weather?q=city,country&appid=fakeApiKey"))
                .willReturn(aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(404)));

        assertThrows(CityNotFoundException.class, () -> openWeatherMapService.getWeather("city", "country"));
    }

    @Test
    void errorWhenHttpException() {
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/weather?q=city,country&appid=fakeApiKey"))
                .willReturn(aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(500)));

        assertThrows(RuntimeException.class, () -> openWeatherMapService.getWeather("city", "country"));
    }


}
