package com.klasha.test.countriesApi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityWithPopulation {
    private String city;

    private List<PopulationCount> populationCounts;
}
