package com.example.tourFlow.controller;

import com.example.tourFlow.model.Destination;
import com.example.tourFlow.model.LocalAlert;
import com.example.tourFlow.model.LocalBusiness;
import com.example.tourFlow.repository.DestinationRepository;
import com.example.tourFlow.repository.LocalBusinessRepository;
import com.example.tourFlow.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class DestinationController {

    private final DestinationRepository destinationRepo;
    private final LocalBusinessRepository businessRepo;
    private final WeatherService weatherService;
    private final TrafficService trafficService;
    private final CrowdPredictionService crowdService;
    private final RecommendationService recommendationService;
    private final SustainabilityService sustainabilityService;
    private final AlertService alertService;

    public DestinationController(DestinationRepository destinationRepo,
                                  LocalBusinessRepository businessRepo,
                                  WeatherService weatherService,
                                  TrafficService trafficService,
                                  CrowdPredictionService crowdService,
                                  RecommendationService recommendationService,
                                  SustainabilityService sustainabilityService,
                                  AlertService alertService) {
        this.destinationRepo = destinationRepo;
        this.businessRepo = businessRepo;
        this.weatherService = weatherService;
        this.trafficService = trafficService;
        this.crowdService = crowdService;
        this.recommendationService = recommendationService;
        this.sustainabilityService = sustainabilityService;
        this.alertService = alertService;
    }

    @GetMapping("/destination/{id}")
    public String destinationDetail(@PathVariable Long id, Model model) {
        Optional<Destination> optDest = destinationRepo.findById(id);
        if (optDest.isEmpty()) return "redirect:/";

        Destination dest = optDest.get();

        // Get live weather
        Map<String, Object> weather = weatherService.getCurrentWeather(dest.getLatitude(), dest.getLongitude());

        // Get crowd prediction (48-hour forecast)
        List<Map<String, Object>> crowdForecast = crowdService.get48HourForecast(id);

        // Get smart alternatives
        List<Map<String, Object>> alternatives = recommendationService.getAlternatives(id);

        // Get sustainability report
        Map<String, Object> sustainability = sustainabilityService.getSustainabilityReport(id);

        // Get local alerts
        List<LocalAlert> alerts = alertService.getAlertsByDestination(id);

        // Get local businesses
        List<LocalBusiness> businesses = businessRepo.findByDestinationIdAndVerifiedTrue(id);

        // Best departure time
        Map<String, Object> bestTime = trafficService.getBestDepartureTime(dest.getName());

        model.addAttribute("destination", dest);
        model.addAttribute("weather", weather);
        model.addAttribute("crowdForecast", crowdForecast);
        model.addAttribute("alternatives", alternatives);
        model.addAttribute("sustainability", sustainability);
        model.addAttribute("alerts", alerts);
        model.addAttribute("businesses", businesses);
        model.addAttribute("bestTime", bestTime);

        return "destination";
    }

    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "") String q, Model model) {
        List<Destination> results;
        if (q.isEmpty()) {
            results = destinationRepo.findByActiveTrue();
        } else {
            results = destinationRepo.searchDestinations(q);
        }

        model.addAttribute("query", q);
        model.addAttribute("results", results);
        model.addAttribute("resultCount", results.size());

        return "search";
    }

    @GetMapping("/explore")
    public String explore(Model model) {
        List<Destination> hiddenGems = destinationRepo.findTopHiddenGems().stream()
                .filter(Destination::isHiddenGem)
                .toList();

        List<Destination> allDestinations = destinationRepo.findByActiveTrue();

        model.addAttribute("hiddenGems", hiddenGems);
        model.addAttribute("allDestinations", allDestinations);

        return "explore";
    }
}
