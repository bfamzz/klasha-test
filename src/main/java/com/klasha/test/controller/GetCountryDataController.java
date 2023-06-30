package com.klasha.test.controller;

import com.klasha.test.controller.response.GetCountryDataResponse;
import com.klasha.test.countriesApi.CountriesApiService;
import com.klasha.test.countriesApi.entity.Country;
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
@RequestMapping(path = "/api/v1/countryData")
@Tag(name = "Get country data",
        description = "REST API to retrieve country data")
public class GetCountryDataController {

    private final CountriesApiService countriesApiService;

    @Autowired
    public GetCountryDataController(CountriesApiService countriesApiService) {
        this.countriesApiService = countriesApiService;
    }

    @Operation(summary = "${api.getCountryData.description}",
            description = "${api.getCountryData.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400",
                    description = "${api.responseCodes.badRequest.description}")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetCountryDataResponse> getCountryData(
            @RequestParam(name = "country") String countryName) {
        Country country = countriesApiService.getCountryData(countryName);
        return ResponseEntity.ok(GetCountryDataResponse.builder()
                .data(country).build());
    }
}
