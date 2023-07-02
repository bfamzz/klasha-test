package com.klasha.test.util;

import com.klasha.test.exception.types.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApiClientTest {

    @Test
    void itShouldGetOkResponse()
            throws URISyntaxException, IOException, InterruptedException {
        // Given
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://www.google.com"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        // When
        String response = ApiClient.makeRequest(request);

        // Then
        assertThat(!response.isEmpty()).isTrue();
    }

    @Test
    void itShouldGetNotFoundResponse()
            throws URISyntaxException, IOException, InterruptedException {
        // Given
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://countriesnow.space/api/v0.1/countries/notfound"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        // When
        // Then
        assertThatThrownBy(() -> ApiClient.makeRequest(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found. Please try again");
    }
}