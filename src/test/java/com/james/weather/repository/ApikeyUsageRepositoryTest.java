package com.james.weather.repository;

import com.james.weather.model.Apikey;
import com.james.weather.model.ApikeyUsage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ApikeyUsageRepositoryTest {
    @Autowired
    private ApikeyUsageRepository apikeyUsageRepository;

    @Autowired
    private ApikeyRepository apikeyRepository;

    private String requestApikey = "test_api_key";

    private int rateLimit = 5;

    private Apikey apikey = null;

    @BeforeEach
    void setUp() {
        apikey = apikeyRepository.save(Apikey.builder().apikey(requestApikey).rateLimit(rateLimit).build());
    }

    @Test
    void countApiUsage() {
        IntStream.range(0, 3).forEach(
            i->apikeyUsageRepository.save(ApikeyUsage.builder().apikey(apikey).requestTime(OffsetDateTime.now()).build())
        );

        int count = apikeyUsageRepository.countApikeyUsageByApikeyIsAndRequestTimeAfter(apikey, OffsetDateTime.now().minusHours(1));

        assertEquals(3, count);
    }
}
