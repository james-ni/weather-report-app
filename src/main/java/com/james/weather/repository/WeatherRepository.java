package com.james.weather.repository;

import com.james.weather.model.Apikey;
import com.james.weather.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
    Optional<Weather> findTopByCityAndCountryOrderByRefreshedAtDesc(String city, String country);
}
