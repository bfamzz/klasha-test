package com.klasha.test.countriesApi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    private String name;
    private long population;
    private String capitalCity;
    private Location location;
    private String currency;
    private String iso2;
    private String iso3;

}
