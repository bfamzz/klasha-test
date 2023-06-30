package com.klasha.test.controller;

import com.klasha.test.controller.response.GetCurrencyConversionResponse;
import com.klasha.test.countriesApi.CountriesApiService;
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
@RequestMapping(path = "/api/v1/convertCurrency")
@Tag(name = "Convert currency",
        description = "REST API to convert currency")
public class ConvertCurrencyController {
    private final CountriesApiService countriesApiService;

    @Autowired
    public ConvertCurrencyController(CountriesApiService countriesApiService) {
        this.countriesApiService = countriesApiService;
    }

    @Operation(summary = "${api.convertCurrency.description}",
            description = "${api.convertCurrency.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400",
                    description = "${api.responseCodes.badRequest.description}")
    })
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
