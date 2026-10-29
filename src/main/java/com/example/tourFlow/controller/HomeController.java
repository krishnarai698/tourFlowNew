package com.example.tourFlow.controller;

import com.example.tourFlow.model.Destination;
import com.example.tourFlow.model.LocalAlert;
import com.example.tourFlow.repository.DestinationRepository;
import com.example.tourFlow.repository.LocalAlertRepository;
import com.example.tourFlow.service.CrowdPredictionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final DestinationRepository destinationRepo;
    private final LocalAlertRepository alertRepo;
    private final CrowdPredictionService crowdService;

    public HomeController(DestinationRepository destinationRepo,
                          LocalAlertRepository alertRepo,
                          CrowdPredictionService crowdService) {
        this.destinationRepo = destinationRepo;
        this.alertRepo = alertRepo;
        this.crowdService = crowdService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Destination> allDestinations = destinationRepo.findAllOrderByPressureDesc();
        List<Destination> hiddenGems = destinationRepo.findTopHiddenGems();
        List<LocalAlert> activeAlerts = alertRepo.findByActiveTrueOrderByReportedAtDesc();

        // Simulate live crowd update for demo
        crowdService.simulateLiveCrowdUpdate();

        // Stats
        long totalDestinations = destinationRepo.count();
        long crowdedDestinations = allDestinations.stream()
                .filter(d -> d.getTourismPressure() >= 75).count();
        long hiddenGemCount = allDestinations.stream()
                .filter(Destination::isHiddenGem).count();

        model.addAttribute("destinations", allDestinations);
        model.addAttribute("hiddenGems", hiddenGems.stream().filter(Destination::isHiddenGem).limit(6).toList());
        model.addAttribute("activeAlerts", activeAlerts);
        model.addAttribute("totalDestinations", totalDestinations);
        model.addAttribute("crowdedDestinations", crowdedDestinations);
        model.addAttribute("hiddenGemCount", hiddenGemCount);
        model.addAttribute("alertCount", activeAlerts.size());

        return "index";
    }
}
