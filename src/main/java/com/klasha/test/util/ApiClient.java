package com.klasha.test.util;

import com.klasha.test.exception.types.ResourceNotFoundException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    public static String makeRequest(HttpRequest request)
            throws IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder()
                .build();
        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
            throw new ResourceNotFoundException("Resource not found. Please try again");
        }
        return response.body();
    }
}
