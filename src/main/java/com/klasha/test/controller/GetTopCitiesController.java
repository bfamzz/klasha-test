package com.klasha.test.controller;

import com.klasha.test.controller.response.GetTopCitiesResponse;
import com.klasha.test.countriesApi.CountriesApiService;
import com.klasha.test.countriesApi.response.FilteredCitiesWithPopulation;
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

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/countryTopCities")
@Tag(name = "Get top cities",
        description = "REST API to retrieve top (number_of_cities) cities in Italy, New Zealand, Ghana")
public class GetTopCitiesController {

    private final CountriesApiService countriesApiService;

    @Autowired
    public GetTopCitiesController(CountriesApiService countriesApiService) {
        this.countriesApiService = countriesApiService;
    }

    @Operation(summary = "${api.getTopCities.description}",
            description = "${api.getTopCities.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400",
                    description = "${api.responseCodes.badRequest.description}")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetTopCitiesResponse> getTopCities(
            @RequestParam(name = "number_of_cities") int numberOfCities) {
        List<FilteredCitiesWithPopulation> result = countriesApiService
                .getTopCities(numberOfCities);
        return ResponseEntity.ok(GetTopCitiesResponse.builder()
                .data(result)
                .build());
    }
}
