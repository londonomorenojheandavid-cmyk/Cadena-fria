package com.cadenafrio.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ClimaApiClient {

    private static final String OPEN_METEO_URL =
            "https://api.open-meteo.com/v1/forecast?latitude=7.88&longitude=-76.72&current=temperature_2m,relative_humidity_2m&timezone=America/Bogota";

    private final HttpClient httpClient;

    public ClimaApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public String obtenerResumenClimaUraba() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OPEN_METEO_URL))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200 && response.body() != null) {
                return response.body();
            }
            return "{\"temperature_2m\":29.4,\"relative_humidity_2m\":78,\"source\":\"fallback\"}";
        } catch (Exception e) {
            return "{\"temperature_2m\":29.4,\"relative_humidity_2m\":78,\"source\":\"fallback\"}";
        }
    }
}
