package com.klasha.test.controller.response;

import com.klasha.test.countriesApi.response.FilteredCitiesWithPopulation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTopCitiesResponse {
    private List<FilteredCitiesWithPopulation> data;
}
