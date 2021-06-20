package com.james.weather.service;

import com.james.weather.model.Apikey;
import com.james.weather.repository.ApikeyRepository;
import com.james.weather.repository.ApikeyUsageRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RateLimitServiceTest {

    @Mock
    private ApikeyRepository apikeyRepository;

    @Mock
    private ApikeyUsageRepository apikeyUsageRepository;

    @InjectMocks
    private RateLimitService rateLimitServices;

    @Test
    void rateLimitNotExceeded() {
        String requestApikey = "apikey";
        OffsetDateTime requestTime = OffsetDateTime.now();

        Apikey apikey = Apikey.builder().id(1L).apikey(requestApikey).rateLimit(5).build();
        when(apikeyRepository.findByApikey(requestApikey)).thenReturn(Optional.of(apikey));
        when(apikeyUsageRepository.countApikeyUsageByApikeyIsAndRequestTimeAfter(apikey, requestTime.minusHours(1L))).thenReturn(3);

        boolean isLimitExceeded = rateLimitServices.isLimitExceeded(requestApikey, requestTime);

        assertFalse(isLimitExceeded);
        verify(apikeyUsageRepository).save(any());
    }

    @Test
    void rateLimitExceeded() {
        String requestApikey = "apikey";
        OffsetDateTime requestTime = OffsetDateTime.now();

        Apikey apikey = Apikey.builder().id(1L).apikey(requestApikey).rateLimit(5).build();
        when(apikeyRepository.findByApikey(requestApikey)).thenReturn(Optional.of(apikey));
        when(apikeyUsageRepository.countApikeyUsageByApikeyIsAndRequestTimeAfter(apikey, requestTime.minusHours(1L))).thenReturn(5);

        boolean isLimitExceeded = rateLimitServices.isLimitExceeded(requestApikey, requestTime);

        assertTrue(isLimitExceeded);
    }
}
