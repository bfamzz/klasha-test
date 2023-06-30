package com.klasha.test.countriesApi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversion {
    private BigDecimal amount;
    private String fromCurrency;
    private String toCurrency;
    private double rate;
    private BigDecimal convertedAmount;
}
