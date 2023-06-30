package com.klasha.test.util;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    public static String makeRequest(HttpRequest request)
            throws IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder()
//                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
