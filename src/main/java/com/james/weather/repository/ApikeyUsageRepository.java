package com.james.weather.repository;

import com.james.weather.model.Apikey;
import com.james.weather.model.ApikeyUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface ApikeyUsageRepository extends JpaRepository<ApikeyUsage, Long> {
    int countApikeyUsageByApikeyIsAndRequestTimeAfter(Apikey apikey, OffsetDateTime requestedOn);
}
