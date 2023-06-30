package com.klasha.test.countriesApi;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.klasha.test.ConcurrencyConfig;
import com.klasha.test.countriesApi.entity.CityWithPopulation;
import com.klasha.test.countriesApi.request.GetTopCitiesRequest;
import com.klasha.test.countriesApi.response.FilteredCitiesWithPopulation;
import com.klasha.test.util.ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountriesApiService {

    private final String BASE_URL = "https://countriesnow.space/api/v0.1/countries";

    private final ConcurrencyConfig concurrencyConfig;

    private final Gson gson = new Gson();

    public List<FilteredCitiesWithPopulation> getTopCities(GetTopCitiesRequest requestBody, int limit)
            throws URISyntaxException, IOException, InterruptedException
    {
        String[] countries = {"Italy", "New Zealand", "Ghana"};
        return Arrays.stream(countries).map(c -> CompletableFuture.supplyAsync(() -> {
            try {
                var iRequest = (GetTopCitiesRequest) requestBody.cloneObj();
                iRequest.setCountry(c);
                iRequest.setLimit(limit);
                return getTopCitiesTask(iRequest);
            } catch (URISyntaxException | IOException | InterruptedException | CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }, concurrencyConfig.getThreadPool())).toList().stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    private FilteredCitiesWithPopulation getTopCitiesTask(GetTopCitiesRequest requestBody)
            throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/population/cities/filter"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();
        String response = ApiClient.makeRequest(request);
        System.out.println(response);
        JsonObject object = gson.fromJson(response, JsonObject.class);
        JsonArray data = object.getAsJsonArray("data");

        List<CityWithPopulation> subResult = new ArrayList<>();
        for (JsonElement e : data) {
            var item = gson.fromJson(e, CityWithPopulation.class);
            subResult.add(item);
        }
        return FilteredCitiesWithPopulation.builder()
                .country(requestBody.getCountry())
                .citiesWithPopulation(subResult)
                .build();
    }
}
