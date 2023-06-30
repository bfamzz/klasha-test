package com.klasha.test.countriesApi;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CountriesApiServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    Gson gson = new Gson();

    @Test
    void itShouldGetTopCities() throws Exception {
        // Given
        int limit = 15;

        ResultActions getTopCitiesActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/countryTopCities?number_of_cities="
                                + limit)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // When
        // Then
        getTopCitiesActions.andExpect(status().isOk());
        MvcResult result = getTopCitiesActions.andReturn();
        String resultString = result.getResponse().getContentAsString();

        JsonObject obj = gson.fromJson(resultString, JsonObject.class);
        assertThat(obj.has("data")).isTrue();

        JsonArray dataArr = obj.getAsJsonArray("data");
        assertThat(dataArr.size()).isEqualTo(3);
    }

    @Test
    void itShouldGetCountryData() throws Exception {
        // Given
        String country = "Ghana";
        ResultActions getCountryDataActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/countryData?country="
                                + country)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // When
        // Then
        getCountryDataActions.andExpect(status().isOk());
        MvcResult result = getCountryDataActions.andReturn();
        String resultString = result.getResponse().getContentAsString();

        JsonObject obj = gson.fromJson(resultString, JsonObject.class);
        assertThat(obj.has("data")).isTrue();

        JsonObject dataObj = obj.getAsJsonObject("data");
        assertThat(dataObj.get("name").getAsString()).isEqualTo(country);
    }

    @Test
    void itShouldGetCountryStateAndCities() throws Exception {
        // Given
        String country = "Nigeria";
        ResultActions getCountryStatesAndCities = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/countryStatesAndCities?country="
                                + country)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // When
        // Then
        getCountryStatesAndCities.andExpect(status().isOk());
        MvcResult result = getCountryStatesAndCities.andReturn();
        String resultString = result.getResponse().getContentAsString();

        JsonObject obj = gson.fromJson(resultString, JsonObject.class);
        assertThat(obj.has("data")).isTrue();

        JsonObject dataObj = obj.getAsJsonObject("data");
        assertThat(dataObj.get("name").getAsString()).isEqualTo(country);
        assertThat(dataObj.getAsJsonArray("states")
                .size()).isGreaterThan(0);
    }

    @Test
    void itShouldConvertCurrency() throws Exception {
        // Given
        String country = "Italy";
        double amount = 10.56;
        double rate = 493.06;
        double convertedAmount = 5206.71;
        String fromCurrency = "EUR";
        String targetCurrency = "NGN";

        // When
        // Then
        ResultActions convertCurrencyActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/convertCurrency?country="
                                + country + "&amount=" + amount
                        + "&targetCurrency=" + targetCurrency)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // When
        // Then
        convertCurrencyActions.andExpect(status().isOk());
        MvcResult result = convertCurrencyActions.andReturn();
        String resultString = result.getResponse().getContentAsString();

        JsonObject obj = gson.fromJson(resultString, JsonObject.class);
        assertThat(obj.has("data")).isTrue();

        JsonObject dataObj = obj.getAsJsonObject("data");
        assertThat(dataObj.get("amount").getAsDouble()).isEqualTo(amount);
        assertThat(dataObj.get("rate").getAsDouble()).isEqualTo(rate);
        assertThat(dataObj.get("convertedAmount").getAsDouble()).isEqualTo(convertedAmount);
        assertThat(dataObj.get("fromCurrency").getAsString()).isEqualTo(fromCurrency);
        assertThat(dataObj.get("toCurrency").getAsString()).isEqualTo(targetCurrency);
    }

    @Test
    void itShouldNotConvertCurrencyBecauseOfInvalidAmount() throws Exception {
        // Given
        String country = "Italy";
        double amount = 0.0;
        String targetCurrency = "NGN";

        // When
        // Then
        ResultActions convertCurrencyActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/convertCurrency?country="
                                + country + "&amount=" + amount
                                + "&targetCurrency=" + targetCurrency)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // When
        // Then
        convertCurrencyActions.andExpect(status().isBadRequest());
        MvcResult result = convertCurrencyActions.andReturn();
        String resultString = result.getResponse().getContentAsString();

        JsonObject obj = gson.fromJson(resultString, JsonObject.class);
        assertThat(obj.has("message")).isTrue();

        String message = obj.get("message").getAsString();
        assertThat(message).isEqualTo("Amount must be greater than 0.0");
    }

    @Test
    void itShouldNotConvertCurrencyBecauseSameCurrency() throws Exception {
        // Given
        String country = "Nigeria";
        double amount = 10.0;
        String targetCurrency = "NGN";

        // When
        // Then
        ResultActions convertCurrencyActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/convertCurrency?country="
                                + country + "&amount=" + amount
                                + "&targetCurrency=" + targetCurrency)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // When
        // Then
        convertCurrencyActions.andExpect(status().isBadRequest());
        MvcResult result = convertCurrencyActions.andReturn();
        String resultString = result.getResponse().getContentAsString();

        JsonObject obj = gson.fromJson(resultString, JsonObject.class);
        assertThat(obj.has("message")).isTrue();

        String message = obj.get("message").getAsString();
        assertThat(message).isEqualTo("Please input a different target currency");
    }

    @Test
    void itShouldNotConvertCurrencyBecauseOfUnsupportedConversion() throws Exception {
        // Given
        String country = "Nigeria";
        double amount = 10.0;
        String targetCurrency = "USD";

        // When
        // Then
        ResultActions convertCurrencyActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/convertCurrency?country="
                                + country + "&amount=" + amount
                                + "&targetCurrency=" + targetCurrency)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // When
        // Then
        convertCurrencyActions.andExpect(status().isBadRequest());
        MvcResult result = convertCurrencyActions.andReturn();
        String resultString = result.getResponse().getContentAsString();

        JsonObject obj = gson.fromJson(resultString, JsonObject.class);
        assertThat(obj.has("message")).isTrue();

        String message = obj.get("message").getAsString();
        assertThat(message).isEqualTo("NGN to " + targetCurrency + " is not supported at this time");
    }
}
