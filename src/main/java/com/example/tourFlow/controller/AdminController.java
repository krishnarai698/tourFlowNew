package com.example.tourFlow.controller;

import com.example.tourFlow.model.Destination;
import com.example.tourFlow.model.LocalAlert;
import com.example.tourFlow.repository.DestinationRepository;
import com.example.tourFlow.service.AlertService;
import com.example.tourFlow.service.CrowdDiversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DestinationRepository destinationRepo;
    private final AlertService alertService;
    private final CrowdDiversionService diversionService;

    public AdminController(DestinationRepository destinationRepo,
                            AlertService alertService,
                            CrowdDiversionService diversionService) {
        this.destinationRepo = destinationRepo;
        this.alertService = alertService;
        this.diversionService = diversionService;
    }

    @GetMapping("")
    public String dashboard(Model model) {
        Map<String, Object> overview = diversionService.getSystemOverview();
        List<LocalAlert> activeAlerts = alertService.getActiveAlerts();
        List<LocalAlert> unverifiedAlerts = alertService.getUnverifiedAlerts();

        model.addAttribute("overview", overview);
        model.addAttribute("activeAlerts", activeAlerts);
        model.addAttribute("unverifiedAlerts", unverifiedAlerts);

        return "admin";
    }

    @PostMapping("/verify/{alertId}")
    public String verifyAlert(@PathVariable Long alertId) {
        alertService.verifyAlert(alertId);
        return "redirect:/admin";
    }

    @PostMapping("/resolve/{alertId}")
    public String resolveAlert(@PathVariable Long alertId) {
        alertService.resolveAlert(alertId);
        return "redirect:/admin";
    }

    @PostMapping("/diversion")
    public String simulateDiversion(@RequestParam Long destinationId,
                                     @RequestParam int expectedTourists,
                                     Model model) {
        Map<String, Object> diversion = diversionService.calculateDiversion(destinationId, expectedTourists);
        Map<String, Object> overview = diversionService.getSystemOverview();
        List<LocalAlert> activeAlerts = alertService.getActiveAlerts();
        List<LocalAlert> unverifiedAlerts = alertService.getUnverifiedAlerts();

        model.addAttribute("overview", overview);
        model.addAttribute("activeAlerts", activeAlerts);
        model.addAttribute("unverifiedAlerts", unverifiedAlerts);
        model.addAttribute("diversion", diversion);

        return "admin";
    }

    @PostMapping("/alert")
    public String issueSystemAlert(@RequestParam Long destinationId,
                                    @RequestParam String message,
                                    Model model) {
        Destination dest = destinationRepo.findById(destinationId).orElse(null);
        if (dest != null) {
            dest.setAlertActive(true);
            dest.setAlertMessage("📢 Admin Alert: " + message);
            destinationRepo.save(dest);
        }
        return "redirect:/admin";
    }

    @PostMapping("/limit/{destinationId}")
    public String limitInflow(@PathVariable Long destinationId) {
        Destination dest = destinationRepo.findById(destinationId).orElse(null);
        if (dest != null) {
            dest.setAlertActive(true);
            dest.setAlertMessage("🚫 Tourist inflow temporarily limited by administration");
            // Reduce max capacity by 50% to simulate limiting
            dest.setMaxCapacity(dest.getMaxCapacity() / 2);
            destinationRepo.save(dest);
        }
        return "redirect:/admin";
    }
}
