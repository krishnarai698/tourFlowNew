package com.example.tourFlow.controller;

import com.example.tourFlow.model.TouristPreference;
import com.example.tourFlow.service.ItineraryService;
import com.example.tourFlow.service.RecommendationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final ItineraryService itineraryService;

    public RecommendationController(RecommendationService recommendationService,
                                     ItineraryService itineraryService) {
        this.recommendationService = recommendationService;
        this.itineraryService = itineraryService;
    }

    @GetMapping("/recommend")
    public String recommendPage(Model model) {
        model.addAttribute("preference", new TouristPreference());
        return "recommend";
    }

    @PostMapping("/recommend")
    public String getRecommendations(@ModelAttribute TouristPreference preference, Model model) {
        List<Map<String, Object>> recommendations = recommendationService.getRecommendations(preference);

        model.addAttribute("preference", preference);
        model.addAttribute("recommendations", recommendations);
        model.addAttribute("hasResults", true);

        return "recommend";
    }

    @GetMapping("/itinerary/{destinationId}")
    public String getItinerary(@PathVariable Long destinationId,
                                @RequestParam(defaultValue = "3") int days,
                                Model model) {
        Map<String, Object> itinerary = itineraryService.generateItinerary(destinationId, days);
        model.addAttribute("itinerary", itinerary);
        return "itinerary";
    }
}
