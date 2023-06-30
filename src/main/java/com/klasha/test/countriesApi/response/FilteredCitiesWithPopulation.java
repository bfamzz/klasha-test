package com.klasha.test.countriesApi.response;

import com.klasha.test.countriesApi.entity.CityWithPopulation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilteredCitiesWithPopulation {

    private String country;
    private List<CityWithPopulation> citiesWithPopulation;
}
