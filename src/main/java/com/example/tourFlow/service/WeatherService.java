package com.example.tourFlow.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    private final WebClient webClient;
    private final String apiKey;
    private final String baseUrl;

    public WeatherService(@Value("${tourflow.api.openweathermap.key}") String apiKey,
                          @Value("${tourflow.api.openweathermap.base-url}") String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    /**
     * Get current weather for given coordinates.
     * Returns a map with: temp, feelsLike, humidity, description, icon, windSpeed, visibility
     */
    public Map<String, Object> getCurrentWeather(double lat, double lon) {
        try {
            if (apiKey.equals("YOUR_OPENWEATHERMAP_API_KEY")) {
                return getMockWeather(lat, lon);
            }

            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/weather")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("appid", apiKey)
                            .queryParam("units", "metric")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parseWeatherResponse(response);
        } catch (Exception e) {
            System.err.println("Weather API error: " + e.getMessage());
            return getMockWeather(lat, lon);
        }
    }

    /**
     * Get 5-day forecast for given coordinates.
     */
    public Map<String, Object> getForecast(double lat, double lon) {
        try {
            if (apiKey.equals("YOUR_OPENWEATHERMAP_API_KEY")) {
                return getMockForecast();
            }

            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/forecast")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("appid", apiKey)
                            .queryParam("units", "metric")
                            .queryParam("cnt", 40)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parseForecastResponse(response);
        } catch (Exception e) {
            System.err.println("Forecast API error: " + e.getMessage());
            return getMockForecast();
        }
    }

    private Map<String, Object> parseWeatherResponse(String json) {
        // Simple JSON parsing without additional library dependencies
        Map<String, Object> weather = new HashMap<>();
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> data = mapper.readValue(json, Map.class);

            @SuppressWarnings("unchecked")
            Map<String, Object> main = (Map<String, Object>) data.get("main");
            weather.put("temp", main.get("temp"));
            weather.put("feelsLike", main.get("feels_like"));
            weather.put("humidity", main.get("humidity"));

            @SuppressWarnings("unchecked")
            java.util.List<Map<String, Object>> weatherList = (java.util.List<Map<String, Object>>) data.get("weather");
            if (weatherList != null && !weatherList.isEmpty()) {
                weather.put("description", weatherList.get(0).get("description"));
                weather.put("icon", weatherList.get(0).get("icon"));
                weather.put("main", weatherList.get(0).get("main"));
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> wind = (Map<String, Object>) data.get("wind");
            if (wind != null) {
                weather.put("windSpeed", wind.get("speed"));
            }

            weather.put("visibility", data.get("visibility"));
        } catch (Exception e) {
            return getMockWeather(0, 0);
        }
        return weather;
    }

    private Map<String, Object> parseForecastResponse(String json) {
        // Simplified forecast parsing
        Map<String, Object> forecast = new HashMap<>();
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> data = mapper.readValue(json, Map.class);
            forecast.put("list", data.get("list"));
        } catch (Exception e) {
            return getMockForecast();
        }
        return forecast;
    }

    /**
     * Mock weather for demo when API key not configured.
     */
    private Map<String, Object> getMockWeather(double lat, double lon) {
        Map<String, Object> weather = new HashMap<>();
        // Simulate based on altitude (higher lat in Himachal = colder)
        double baseTemp = 25.0 - (lat - 30.0) * 5.0;
        weather.put("temp", Math.round(baseTemp * 10.0) / 10.0);
        weather.put("feelsLike", Math.round((baseTemp - 2.0) * 10.0) / 10.0);
        weather.put("humidity", 55 + (int)(Math.random() * 30));
        weather.put("description", "partly cloudy");
        weather.put("icon", "02d");
        weather.put("main", "Clouds");
        weather.put("windSpeed", 3.5 + Math.random() * 5.0);
        weather.put("visibility", 8000 + (int)(Math.random() * 2000));
        return weather;
    }

    private Map<String, Object> getMockForecast() {
        Map<String, Object> forecast = new HashMap<>();
        forecast.put("list", java.util.Collections.emptyList());
        return forecast;
    }
}
