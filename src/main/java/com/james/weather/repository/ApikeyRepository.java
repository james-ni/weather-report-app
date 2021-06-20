package com.james.weather.repository;

import com.james.weather.model.Apikey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApikeyRepository extends JpaRepository<Apikey, Long> {
    Optional<Apikey> findByApikey(String apikey);
}
