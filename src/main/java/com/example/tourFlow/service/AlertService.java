package com.example.tourFlow.service;

import com.example.tourFlow.model.Destination;
import com.example.tourFlow.model.LocalAlert;
import com.example.tourFlow.repository.DestinationRepository;
import com.example.tourFlow.repository.LocalAlertRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AlertService {

    private final LocalAlertRepository alertRepo;
    private final DestinationRepository destinationRepo;

    public AlertService(LocalAlertRepository alertRepo, DestinationRepository destinationRepo) {
        this.alertRepo = alertRepo;
        this.destinationRepo = destinationRepo;
    }

    /**
     * Submit a new community alert.
     */
    public LocalAlert submitAlert(LocalAlert alert) {
        alert.setReportedAt(LocalDateTime.now());
        alert.setActive(true);
        alert.setVerified(false);

        // Auto-set impact score based on type and severity
        alert.setImpactScore(calculateImpactScore(alert));

        LocalAlert saved = alertRepo.save(alert);

        // If severity is CRITICAL, auto-update destination status
        if (alert.getSeverity() == LocalAlert.AlertSeverity.CRITICAL) {
            updateDestinationAlert(alert.getDestinationId(), true,
                    alert.getType().getEmoji() + " " + alert.getType().getLabel() + ": " + alert.getDescription());
        }

        return saved;
    }

    /**
     * Verify an alert (admin action).
     */
    public LocalAlert verifyAlert(Long alertId) {
        Optional<LocalAlert> optAlert = alertRepo.findById(alertId);
        if (optAlert.isEmpty()) return null;

        LocalAlert alert = optAlert.get();
        alert.setVerified(true);
        alert.setVerifiedAt(LocalDateTime.now());

        // Update destination status when verified
        updateDestinationAlert(alert.getDestinationId(), true,
                alert.getType().getEmoji() + " " + alert.getType().getLabel() + " (Verified): " + alert.getDescription());

        return alertRepo.save(alert);
    }

    /**
     * Resolve an alert.
     */
    public LocalAlert resolveAlert(Long alertId) {
        Optional<LocalAlert> optAlert = alertRepo.findById(alertId);
        if (optAlert.isEmpty()) return null;

        LocalAlert alert = optAlert.get();
        alert.setActive(false);
        alert.setResolvedAt(LocalDateTime.now());

        // Check if there are remaining active alerts for this destination
        long remainingAlerts = alertRepo.countByDestinationIdAndActiveTrue(alert.getDestinationId());
        if (remainingAlerts <= 1) { // Current alert is still counted
            updateDestinationAlert(alert.getDestinationId(), false, null);
        }

        return alertRepo.save(alert);
    }

    /**
     * Get all active alerts.
     */
    public List<LocalAlert> getActiveAlerts() {
        return alertRepo.findByActiveTrueOrderByReportedAtDesc();
    }

    /**
     * Get alerts for a specific destination.
     */
    public List<LocalAlert> getAlertsByDestination(Long destinationId) {
        return alertRepo.findByDestinationIdAndActiveTrue(destinationId);
    }

    /**
     * Get unverified alerts (for admin review).
     */
    public List<LocalAlert> getUnverifiedAlerts() {
        return alertRepo.findByVerifiedFalseAndActiveTrue();
    }

    private void updateDestinationAlert(Long destinationId, boolean alertActive, String message) {
        Optional<Destination> optDest = destinationRepo.findById(destinationId);
        if (optDest.isPresent()) {
            Destination dest = optDest.get();
            dest.setAlertActive(alertActive);
            dest.setAlertMessage(message);
            destinationRepo.save(dest);
        }
    }

    private int calculateImpactScore(LocalAlert alert) {
        int base = switch (alert.getSeverity()) {
            case LOW -> 20;
            case MEDIUM -> 45;
            case HIGH -> 70;
            case CRITICAL -> 90;
        };

        // Adjust based on type
        int typeBonus = switch (alert.getType()) {
            case STRIKE_PROTEST -> 15;
            case ROAD_BLOCKAGE -> 12;
            case OVERCROWDING -> 10;
            case SAFETY_CONCERN -> 10;
            case TRANSPORT -> 8;
            case WATER_SHORTAGE -> 7;
            case WEATHER_WARNING -> 5;
            case WASTE_PROBLEM -> 3;
            case INFRASTRUCTURE -> 5;
        };

        return Math.min(100, base + typeBonus);
    }
}
