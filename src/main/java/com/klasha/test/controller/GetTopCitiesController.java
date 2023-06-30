package com.klasha.test.controller;

import com.klasha.test.controller.request.GetTopCitiesRequest;
import com.klasha.test.controller.response.GetTopCitiesResponse;
import com.klasha.test.countriesApi.CountriesApiService;
import com.klasha.test.countriesApi.response.FilteredCitiesWithPopulation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class GetTopCitiesController {

    private final CountriesApiService countriesApiService;

    @Autowired
    public GetTopCitiesController(CountriesApiService countriesApiService) {
        this.countriesApiService = countriesApiService;
    }

    @PostMapping(path = "/topCities", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetTopCitiesResponse> getTopCities(@RequestBody GetTopCitiesRequest request,
                                                             @RequestParam(name = "number_of_cities") int numberOfCities) {
        List<FilteredCitiesWithPopulation> result = countriesApiService
                .getTopCities(request, numberOfCities);;
        return ResponseEntity.ok(GetTopCitiesResponse.builder()
                .data(result)
                .build());
    }
}
