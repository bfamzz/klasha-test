package com.klasha.test.controller;

import com.klasha.test.controller.response.GetCountryStatesAndCitiesResponse;
import com.klasha.test.countriesApi.CountriesApiService;
import com.klasha.test.countriesApi.entity.CountryStatesAndCities;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/countryStatesAndCities")
@Tag(name = "Get states and cities in a country",
        description = "REST API to retrieve states and cities in a country")
public class GetCountryStatesAndCitiesController {
    private final CountriesApiService countriesApiService;

    @Autowired
    public GetCountryStatesAndCitiesController(CountriesApiService countriesApiService) {
        this.countriesApiService = countriesApiService;
    }

    @Operation(summary = "${api.getCountryStatesAndCities.description}",
            description = "${api.getCountryStatesAndCities.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400",
                    description = "${api.responseCodes.badRequest.description}")
    })
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
