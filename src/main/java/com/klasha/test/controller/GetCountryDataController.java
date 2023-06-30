package com.klasha.test.controller;

import com.klasha.test.controller.request.GetCountryDataRequest;
import com.klasha.test.controller.response.GetCountryDataResponse;
import com.klasha.test.countriesApi.CountriesApiService;
import com.klasha.test.countriesApi.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/countries")
public class GetCountryDataController {

    private final CountriesApiService countriesApiService;

    @Autowired
    public GetCountryDataController(CountriesApiService countriesApiService) {
        this.countriesApiService = countriesApiService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetCountryDataResponse> getCountryData(@RequestBody GetCountryDataRequest request) {
        Country country = countriesApiService.getCountryData(request);
        return ResponseEntity.ok(GetCountryDataResponse.builder()
                .data(country).build());
    }
}
