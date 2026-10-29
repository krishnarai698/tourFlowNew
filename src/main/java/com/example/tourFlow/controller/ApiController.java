package com.example.tourFlow.controller;

import com.example.tourFlow.model.Destination;
import com.example.tourFlow.repository.DestinationRepository;
import com.example.tourFlow.service.CrowdPredictionService;
import com.example.tourFlow.service.WeatherService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final DestinationRepository destinationRepo;
    private final CrowdPredictionService crowdService;
    private final WeatherService weatherService;

    public ApiController(DestinationRepository destinationRepo,
                          CrowdPredictionService crowdService,
                          WeatherService weatherService) {
        this.destinationRepo = destinationRepo;
        this.crowdService = crowdService;
        this.weatherService = weatherService;
    }

    /**
     * Get live status of all destinations (for AJAX polling).
     */
    @GetMapping("/destinations/status")
    public List<Map<String, Object>> getAllStatus() {
        // Simulate live update
        crowdService.simulateLiveCrowdUpdate();

        return destinationRepo.findByActiveTrue().stream()
                .map(this::toStatusMap)
                .collect(Collectors.toList());
    }

    /**
     * Get status of a single destination.
     */
    @GetMapping("/destination/{id}/status")
    public Map<String, Object> getStatus(@PathVariable Long id) {
        return destinationRepo.findById(id)
                .map(this::toStatusMap)
                .orElse(Map.of("error", "Not found"));
    }

    /**
     * Get weather for a destination.
     */
    @GetMapping("/weather/{id}")
    public Map<String, Object> getWeather(@PathVariable Long id) {
        return destinationRepo.findById(id)
                .map(d -> weatherService.getCurrentWeather(d.getLatitude(), d.getLongitude()))
                .orElse(Map.of("error", "Not found"));
    }

    /**
     * Get crowd prediction for a destination.
     */
    @GetMapping("/crowd-prediction/{id}")
    public List<Map<String, Object>> getCrowdPrediction(@PathVariable Long id) {
        return crowdService.get48HourForecast(id);
    }

    /**
     * Search destinations (for autocomplete).
     */
    @GetMapping("/search")
    public List<Map<String, Object>> search(@RequestParam String q) {
        return destinationRepo.searchDestinations(q).stream()
                .map(d -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", d.getId());
                    item.put("name", d.getName());
                    item.put("state", d.getState());
                    item.put("type", d.getType());
                    item.put("crowd", d.getCurrentCrowdPercentage());
                    item.put("crowdColor", d.getCrowdColor());
                    return item;
                })
                .collect(Collectors.toList());
    }

    private Map<String, Object> toStatusMap(Destination d) {
        Map<String, Object> status = new HashMap<>();
        status.put("id", d.getId());
        status.put("name", d.getName());
        status.put("crowd", d.getCurrentCrowdPercentage());
        status.put("traffic", d.getCurrentTrafficPercentage());
        status.put("parking", d.getCurrentParkingPercentage());
        status.put("hotelAvailability", d.getHotelAvailabilityPercentage());
        status.put("tourismPressure", d.getTourismPressure());
        status.put("environmentalLoad", d.getEnvironmentalLoad());
        status.put("crowdColor", d.getCrowdColor());
        status.put("trafficColor", d.getTrafficColor());
        status.put("pressureColor", d.getPressureColor());
        status.put("alertActive", d.isAlertActive());
        status.put("alertMessage", d.getAlertMessage());
        status.put("lat", d.getLatitude());
        status.put("lng", d.getLongitude());
        return status;
    }
}
