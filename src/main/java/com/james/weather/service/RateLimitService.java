package com.james.weather.service;

import com.james.weather.exception.InvalidApikeyException;
import com.james.weather.model.Apikey;
import com.james.weather.model.ApikeyUsage;
import com.james.weather.repository.ApikeyRepository;
import com.james.weather.repository.ApikeyUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final ApikeyRepository apikeyRepository;
    private final ApikeyUsageRepository apikeyUsageRepository;

    public boolean isLimitExceeded(String requestApikey, OffsetDateTime requestTime) {
        Optional<Apikey> apikeys = apikeyRepository.findByApikey(requestApikey);

        return apikeys.map(
                apikey -> {
                    int count = apikeyUsageRepository.countApikeyUsageByApikeyIsAndRequestTimeAfter(apikey, requestTime.minusHours(1L));
                    if (count >= apikey.getRateLimit()) {
                        return true;
                    }
                    apikeyUsageRepository.save(ApikeyUsage.builder().apikey(apikey).requestTime(requestTime).build());
                    return false;
                }
        ).orElseThrow(() -> new InvalidApikeyException());
    }
}
