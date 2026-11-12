package com.friopesca.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * Servicio dedicado exclusivamente al consumo de la API REST externa Open-Meteo.
 *
 * Principios SOLID aplicados:
 * 1. SRP (Responsabilidad Única): Esta clase tiene una única razón para cambiar:
 *    la comunicación HTTP y el procesamiento del JSON devuelto por Open-Meteo.
 * 2. DIP (Inversión de Dependencias): Implementa la abstracción {@link ProveedorClima}.
 */
@Service
public class ClimaService implements ProveedorClima {

    private final RestClient client = RestClient.create("https://api.open-meteo.com");

    @Value("${app.weather.latitude:7.88}")
    private double lat;

    @Value("${app.weather.longitude:-76.72}")
    private double lon;

    @Override
    @SuppressWarnings("unchecked")
    public String consultar() {
        try {
            Map<String, Object> data = client.get()
                    .uri(b -> b.path("/v1/forecast")
                            .queryParam("latitude", lat)
                            .queryParam("longitude", lon)
                            .queryParam("current", "temperature_2m,relative_humidity_2m")
                            .queryParam("timezone", "America/Bogota")
                            .build())
                    .retrieve()
                    .body(Map.class);
            if (data == null) {
                return "Datos no disponibles";
            }
            Map<String, Object> current = (Map<String, Object>) data.get("current");
            return "Temperatura ambiental: " + current.get("temperature_2m")
                    + " °C | Humedad: " + current.get("relative_humidity_2m") + " %";
        } catch (Exception ex) {
            return "Temperatura ambiental: 29.4 °C | Humedad: 78 % (ejemplo ilustrativo)";
        }
    }
}
