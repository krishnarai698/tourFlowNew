package com.example.tourFlow.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class TrafficService {

    private final WebClient webClient;
    private final String apiKey;

    public TrafficService(@Value("${tourflow.api.googlemaps.key}") String apiKey,
                          @Value("${tourflow.api.googlemaps.base-url}") String baseUrl) {
        this.apiKey = apiKey;
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    /**
     * Get traffic and route information between origin and destination.
     */
    public Map<String, Object> getRouteInfo(String origin, String destination) {
        try {
            if (apiKey.equals("YOUR_GOOGLE_MAPS_API_KEY")) {
                return getMockRouteInfo(origin, destination);
            }

            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/directions/json")
                            .queryParam("origin", origin)
                            .queryParam("destination", destination)
                            .queryParam("key", apiKey)
                            .queryParam("departure_time", "now")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parseRouteResponse(response);
        } catch (Exception e) {
            System.err.println("Traffic API error: " + e.getMessage());
            return getMockRouteInfo(origin, destination);
        }
    }

    /**
     * Get distance matrix for multiple destinations.
     */
    public Map<String, Object> getDistanceMatrix(String origin, String destinations) {
        try {
            if (apiKey.equals("YOUR_GOOGLE_MAPS_API_KEY")) {
                return getMockDistanceMatrix();
            }

            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/distancematrix/json")
                            .queryParam("origins", origin)
                            .queryParam("destinations", destinations)
                            .queryParam("key", apiKey)
                            .queryParam("departure_time", "now")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parseDistanceResponse(response);
        } catch (Exception e) {
            return getMockDistanceMatrix();
        }
    }

    /**
     * Get best departure time recommendation.
     */
    public Map<String, Object> getBestDepartureTime(String destination) {
        Map<String, Object> recommendation = new HashMap<>();
        // Time-based traffic patterns for hill stations
        int currentHour = java.time.LocalTime.now().getHour();

        if (currentHour >= 6 && currentHour <= 10) {
            recommendation.put("recommendedDeparture", "5:30 AM");
            recommendation.put("expectedReduction", 34);
            recommendation.put("reason", "Early morning departure avoids peak tourist traffic");
        } else if (currentHour >= 10 && currentHour <= 16) {
            recommendation.put("recommendedDeparture", "4:00 AM tomorrow");
            recommendation.put("expectedReduction", 45);
            recommendation.put("reason", "Pre-dawn departure ensures smooth passage through mountain roads");
        } else {
            recommendation.put("recommendedDeparture", "5:00 AM tomorrow");
            recommendation.put("expectedReduction", 38);
            recommendation.put("reason", "Morning departure offers best visibility and minimal traffic");
        }

        recommendation.put("peakHours", "10:00 AM - 2:00 PM");
        recommendation.put("avoidHours", "11:00 AM - 4:00 PM on weekends");
        return recommendation;
    }

    private Map<String, Object> parseRouteResponse(String json) {
        Map<String, Object> route = new HashMap<>();
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> data = mapper.readValue(json, Map.class);

            @SuppressWarnings("unchecked")
            java.util.List<Map<String, Object>> routes = (java.util.List<Map<String, Object>>) data.get("routes");
            if (routes != null && !routes.isEmpty()) {
                @SuppressWarnings("unchecked")
                java.util.List<Map<String, Object>> legs = (java.util.List<Map<String, Object>>) routes.get(0).get("legs");
                if (legs != null && !legs.isEmpty()) {
                    Map<String, Object> leg = legs.get(0);
                    route.put("distance", leg.get("distance"));
                    route.put("duration", leg.get("duration"));
                    route.put("durationInTraffic", leg.get("duration_in_traffic"));
                }
            }
        } catch (Exception e) {
            return getMockRouteInfo("", "");
        }
        return route;
    }

    private Map<String, Object> parseDistanceResponse(String json) {
        Map<String, Object> result = new HashMap<>();
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> data = mapper.readValue(json, Map.class);
            result.putAll(data);
        } catch (Exception e) {
            return getMockDistanceMatrix();
        }
        return result;
    }

    private Map<String, Object> getMockRouteInfo(String origin, String destination) {
        Map<String, Object> route = new HashMap<>();
        Map<String, Object> distance = new HashMap<>();
        distance.put("text", "350 km");
        distance.put("value", 350000);
        route.put("distance", distance);

        Map<String, Object> duration = new HashMap<>();
        duration.put("text", "8 hours 30 min");
        duration.put("value", 30600);
        route.put("duration", duration);

        Map<String, Object> durationTraffic = new HashMap<>();
        durationTraffic.put("text", "10 hours 15 min");
        durationTraffic.put("value", 36900);
        route.put("durationInTraffic", durationTraffic);

        route.put("trafficLevel", "Heavy");
        return route;
    }

    private Map<String, Object> getMockDistanceMatrix() {
        Map<String, Object> matrix = new HashMap<>();
        matrix.put("status", "OK");
        return matrix;
    }
}
