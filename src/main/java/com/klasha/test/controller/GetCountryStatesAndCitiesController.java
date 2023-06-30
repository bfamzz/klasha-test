package com.klasha.test.controller;

import com.klasha.test.controller.response.GetCountryStatesAndCitiesResponse;
import com.klasha.test.countriesApi.CountriesApiService;
import com.klasha.test.countriesApi.entity.CountryStatesAndCities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/countryStatesAndCities")
public class GetCountryStatesAndCitiesController {
    private final CountriesApiService countriesApiService;

    @Autowired
    public GetCountryStatesAndCitiesController(CountriesApiService countriesApiService) {
        this.countriesApiService = countriesApiService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetCountryStatesAndCitiesResponse> getCountryStatesAndCities(
            @RequestParam(name = "country") String country) {
        CountryStatesAndCities countryStatesAndCities =
                countriesApiService.getCountryStatesAndCities(country);
        return ResponseEntity.ok(GetCountryStatesAndCitiesResponse.builder()
                .data(countryStatesAndCities)
                .build());
    }
}
