package com.klasha.test.controller;

import com.klasha.test.controller.response.GetCurrencyConversionResponse;
import com.klasha.test.countriesApi.CountriesApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/convert")
public class ConvertCurrencyController {
    private final CountriesApiService countriesApiService;

    @Autowired
    public ConvertCurrencyController(CountriesApiService countriesApiService) {
        this.countriesApiService = countriesApiService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetCurrencyConversionResponse> getCurrencyConversion(
            @RequestParam(name = "country") String country,
            @RequestParam(name = "amount") double amount,
            @RequestParam(name = "targetCurrency") String targetCurrency) {
        var result = countriesApiService
                .convertCurrency(country, amount, targetCurrency);
        return ResponseEntity.ok(GetCurrencyConversionResponse.builder()
                .data(result)
                .build());
    }
}
