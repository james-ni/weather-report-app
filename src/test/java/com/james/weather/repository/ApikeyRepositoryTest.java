package com.james.weather.repository;

import com.james.weather.model.Apikey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ApikeyRepositoryTest {

    @Autowired
    private ApikeyRepository apikeyRepository;

    private String requestApikey = "test_api_key";

    private int rateLimit = 5;

    private Apikey apikey = null;

    @BeforeEach
    void setUp() {
        apikey = apikeyRepository.save(Apikey.builder().apikey(requestApikey).rateLimit(rateLimit).build());
    }

    @AfterEach
    void tearDown() {
        apikeyRepository.deleteById(apikey.getId());
    }

    @Test
    void findOneApikey() {
        Optional<Apikey> result = apikeyRepository.findByApikey(requestApikey);
        assertEquals(result.get().getApikey(), "test_api_key");
    }

    @Test
    void apikeyNotFound() {
        Optional<Apikey> result = apikeyRepository.findByApikey("non_exist_apikey");
        assertTrue(result.isEmpty());
    }
}
