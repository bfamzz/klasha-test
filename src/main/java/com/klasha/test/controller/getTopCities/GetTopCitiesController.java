package com.klasha.test.controller.getTopCities;

import com.klasha.test.controller.response.GetTopCitiesResponse;
import com.klasha.test.countriesApi.CountriesApiService;
import com.klasha.test.countriesApi.request.GetTopCitiesRequest;
import com.klasha.test.countriesApi.response.FilteredCitiesWithPopulation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class GetTopCitiesController {

    private final CountriesApiService countriesApiService;

    @Autowired
    public GetTopCitiesController(CountriesApiService countriesApiService) {
        this.countriesApiService = countriesApiService;
    }

    @PostMapping(path = "/topCities")
    public ResponseEntity<GetTopCitiesResponse> getTopCities(@RequestBody GetTopCitiesRequest request,
                                                             @RequestParam(name = "number_of_cities") int numberOfCities) {
        List<FilteredCitiesWithPopulation> result;
        try {
            result = countriesApiService.getTopCities(request, numberOfCities);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(GetTopCitiesResponse.builder()
                .data(result)
                .build());
    }
}
