package com.james.weather.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "apikey")
public class Apikey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String apikey;

    @Column(nullable = false)
    private int rateLimit;

    @OneToMany(mappedBy = "apikey")
    private List<ApikeyUsage> apikeyUsageList;
}