package com.example.tourFlow.controller;

import com.example.tourFlow.model.Destination;
import com.example.tourFlow.model.LocalAlert;
import com.example.tourFlow.repository.DestinationRepository;
import com.example.tourFlow.service.AlertService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/community")
public class CommunityController {

    private final AlertService alertService;
    private final DestinationRepository destinationRepo;

    public CommunityController(AlertService alertService, DestinationRepository destinationRepo) {
        this.alertService = alertService;
        this.destinationRepo = destinationRepo;
    }

    @GetMapping("")
    public String communityPortal(Model model) {
        List<LocalAlert> activeAlerts = alertService.getActiveAlerts();
        List<Destination> destinations = destinationRepo.findByActiveTrue();

        model.addAttribute("activeAlerts", activeAlerts);
        model.addAttribute("destinations", destinations);
        model.addAttribute("newAlert", new LocalAlert());
        model.addAttribute("alertTypes", LocalAlert.AlertType.values());
        model.addAttribute("severityLevels", LocalAlert.AlertSeverity.values());

        return "community";
    }

    @PostMapping("/report")
    public String submitReport(@RequestParam Long destinationId,
                                @RequestParam String destinationName,
                                @RequestParam LocalAlert.AlertType type,
                                @RequestParam LocalAlert.AlertSeverity severity,
                                @RequestParam String description,
                                @RequestParam(required = false) String reportedBy,
                                @RequestParam(required = false) String contactInfo) {
        LocalAlert alert = new LocalAlert();
        alert.setDestinationId(destinationId);
        alert.setDestinationName(destinationName);
        alert.setType(type);
        alert.setSeverity(severity);
        alert.setDescription(description);
        alert.setReportedBy(reportedBy != null ? reportedBy : "Anonymous");
        alert.setContactInfo(contactInfo);

        alertService.submitAlert(alert);

        return "redirect:/community?success=true";
    }
}
