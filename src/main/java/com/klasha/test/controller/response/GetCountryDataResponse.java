package com.klasha.test.controller.response;

import com.klasha.test.countriesApi.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCountryDataResponse {
    private Country data;
}
