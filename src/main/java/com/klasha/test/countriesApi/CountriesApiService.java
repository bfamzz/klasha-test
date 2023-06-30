package com.klasha.test.countriesApi;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.klasha.test.config.ConcurrencyConfig;
import com.klasha.test.countriesApi.entity.CityWithPopulation;
import com.klasha.test.countriesApi.entity.Country;
import com.klasha.test.countriesApi.entity.CountryState;
import com.klasha.test.countriesApi.entity.CountryStatesAndCities;
import com.klasha.test.countriesApi.entity.CurrencyConversion;
import com.klasha.test.countriesApi.entity.Location;
import com.klasha.test.countriesApi.response.FilteredCitiesWithPopulation;
import com.klasha.test.enums.CountryTask;
import com.klasha.test.exception.types.CurrencyConversionNotSupported;
import com.klasha.test.exception.types.InternalServerErrorException;
import com.klasha.test.exception.types.InvalidAmountException;
import com.klasha.test.exception.types.SameCurrencyNotSupportedException;
import com.klasha.test.repository.CsvRatesRepository;
import com.klasha.test.util.ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountriesApiService {

    private final String BASE_URL = "https://countriesnow.space/api/v0.1/countries";

    private final ConcurrencyConfig concurrencyConfig;

    private final CsvRatesRepository csvRatesRepository;

    private final Gson gson = new Gson();

    public List<FilteredCitiesWithPopulation> getTopCities(int limit)
    {
        String[] countries = {"Italy", "New Zealand", "Ghana"};
        return Arrays.stream(countries).map(c -> CompletableFuture.supplyAsync(() -> {
            try {
                return getTopCitiesTask(c, limit);
            } catch (Exception e) {
                throw new InternalServerErrorException("Unable to get data. Please try again!");
            }
        }, concurrencyConfig.getThreadPool())).toList().stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public Country getCountryData(String countryName) {
        String encodedCountry = URLEncoder.encode(countryName, StandardCharsets.UTF_8);
        Country country = Country.builder()
                .name(encodedCountry)
                .build();

        Map<CountryTask, String> taskMappings = new HashMap<>();
        taskMappings.put(CountryTask.POPULATION,
                BASE_URL + "/population/q?country=" + encodedCountry);
        taskMappings.put(CountryTask.CAPITAL_CITY_ISO2_ISO3,
                BASE_URL + "/capital/q?country=" + encodedCountry);
        taskMappings.put(CountryTask.LOCATION,
                BASE_URL + "/positions/q?country=" + encodedCountry);
        taskMappings.put(CountryTask.CURRENCY,
                BASE_URL + "/currency/q?country=" + encodedCountry);

        taskMappings.entrySet().stream().map(s -> CompletableFuture.runAsync(() -> {
            try {
                getCountryDataPrivate(country, s.getValue(), s.getKey());
            } catch (URISyntaxException | IOException | InterruptedException e) {
                throw new InternalServerErrorException("Unable to process request. Please try again");
            }
        }, concurrencyConfig.getThreadPool())).toList().stream().map(CompletableFuture::join).collect(Collectors.toList());
        return country;
    }

    public CountryStatesAndCities getCountryStatesAndCities(String country) {
         String response = getCountryStates(country);

        JsonObject obj = gson.fromJson(response, JsonObject.class);
        JsonObject data = obj.getAsJsonObject("data");
        JsonArray states = data.getAsJsonArray("states");

        var result = states.asList().stream().map(e -> CompletableFuture.supplyAsync(() -> {
            String state = e.getAsJsonObject().get("name").getAsString();

            String citiesResponse = getStateCities(country, state);

            JsonObject citiesObj = gson.fromJson(citiesResponse, JsonObject.class);
            JsonArray citiesArray = citiesObj.getAsJsonArray("data");

            var cities = gson.fromJson(citiesArray, String[].class);
            return CountryState.builder()
                    .name(state)
                    .cities(Arrays.stream(cities).toList())
                    .build();
        }, concurrencyConfig.getThreadPool())).toList().stream().map(CompletableFuture::join).collect(Collectors.toList());

        return CountryStatesAndCities.builder()
                .name(country)
                .states(result)
                .build();
    }

    public CurrencyConversion convertCurrency(String country, double amount, String targetCurrency) {
        if (amount <= 0.0) {
            throw new InvalidAmountException("Amount must be greater than 0.0");
        }
        String fromCurrency = getCountryCurrency(country);
        if (fromCurrency.equalsIgnoreCase(targetCurrency)) {
            throw new SameCurrencyNotSupportedException("Please input a different target currency");
        }

        var conversion =  csvRatesRepository.availableExchanges().stream()
                .filter(v -> v.get(0).equalsIgnoreCase(fromCurrency)
                        && v.get(1).equalsIgnoreCase(targetCurrency)).findFirst();
        if (conversion.isEmpty()) {
            throw new CurrencyConversionNotSupported(fromCurrency + " to "
                    + targetCurrency + " is not supported at this time");
        }

        List<String> conversionParams = conversion.get();

        var amountBD = new BigDecimal(amount);
        var rate = Double.parseDouble(conversionParams.get(2));
        var rateBD = new BigDecimal(rate);
        var convertedAmountBD = amountBD.multiply(rateBD);

        return CurrencyConversion.builder()
                .amount(amountBD.setScale(2, RoundingMode.HALF_EVEN))
                .fromCurrency(fromCurrency)
                .toCurrency(targetCurrency)
                .rate(rate)
                .convertedAmount(convertedAmountBD
                        .setScale(2, RoundingMode.HALF_EVEN))
                .build();
    }

    private String getCountryCurrency(String country) {
        String response;
        try {
            String encodedCountry = URLEncoder.encode(country, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/currency/q?" + "country="
                            + encodedCountry))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            response = ApiClient.makeRequest(request);
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new InternalServerErrorException("Unable to process request. Please try again");
        }

        JsonObject object = gson.fromJson(response, JsonObject.class);
        JsonObject data = object.getAsJsonObject("data");

        return data.get("currency").getAsString();
    }

    private String getStateCities(String country, String state) {
        // FAILING CASE - [country: Nigeria, state: Lagos State]
        if (country.equalsIgnoreCase("nigeria") && state.toLowerCase().contains("lagos")) {
            state = state.split(" ")[0];
        }
        try {
            String encodedCountry = URLEncoder.encode(country, StandardCharsets.UTF_8);
            String encodedState = URLEncoder.encode(state, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/state/cities/q?" + "country="
                            + encodedCountry + "&state=" + encodedState))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            return ApiClient.makeRequest(request);
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new InternalServerErrorException("Unable to process request. Please try again");
        }
    }

    private String getCountryStates(String country) {
        try {
            String encodedCountry = URLEncoder.encode(country, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/states/q?" + "country=" + encodedCountry))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            return ApiClient.makeRequest(request);
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new InternalServerErrorException("Unable to process request. Please try again");
        }
    }

    private void getCountryDataPrivate(Country country, String url, CountryTask task)
            throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        String response = ApiClient.makeRequest(request);
        processCountryTaskResult(country, task, response);
    }

    private void processCountryTaskResult(Country country, CountryTask task, String response) {
        JsonObject object = gson.fromJson(response, JsonObject.class);
        JsonObject data = object.getAsJsonObject("data");

        switch (task) {
            case POPULATION -> {
                JsonArray populationCounts = data.
                        getAsJsonArray("populationCounts");
                JsonElement e = populationCounts.get(populationCounts.size() - 1);
                int latestPopulationData = e.getAsJsonObject()
                        .get("value")
                        .getAsInt();
                country.setPopulation(latestPopulationData);
            }
            case CAPITAL_CITY_ISO2_ISO3 -> {
                String capitalCity = data.get("capital")
                        .getAsString();
                String iso2 = data.get("iso2")
                        .getAsString();
                String iso3 = data.get("iso3")
                        .getAsString();
                country.setCapitalCity(capitalCity);
                country.setIso2(iso2);
                country.setIso3(iso3);
            }
            case LOCATION -> {
                Location location = gson.fromJson(data, Location.class);
                country.setLocation(location);
            }
            case CURRENCY -> {
                String currency = data.get("currency").getAsString();
                country.setCurrency(currency);
            }
        }
    }

    private FilteredCitiesWithPopulation getTopCitiesTask(String country, int limit)
            throws URISyntaxException, IOException, InterruptedException {
        String encodedCountry = URLEncoder.encode(country, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL
                        + "/population/cities/filter/q?country=" + encodedCountry
                        + "&limit=" + limit + "&order=desc&orderBy=population"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        String response = ApiClient.makeRequest(request);
        JsonObject object = gson.fromJson(response, JsonObject.class);
        JsonArray data = object.getAsJsonArray("data");

        List<CityWithPopulation> subResult = new ArrayList<>();
        for (JsonElement e : data) {
            var item = gson.fromJson(e, CityWithPopulation.class);
            subResult.add(item);
        }
        return FilteredCitiesWithPopulation.builder()
                .country(country)
                .citiesWithPopulation(subResult)
                .build();
    }
}
