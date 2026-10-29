package com.example.tourFlow.service;

import com.example.tourFlow.model.Destination;
import com.example.tourFlow.repository.DestinationRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CrowdPredictionService {

    private final DestinationRepository destinationRepo;

    public CrowdPredictionService(DestinationRepository destinationRepo) {
        this.destinationRepo = destinationRepo;
    }

    /**
     * Predict crowd percentage for a destination at a given future time.
     * Uses weighted algorithm based on:
     * - Current crowd level
     * - Day of week (weekends = higher)
     * - Time of day
     * - Season/month
     * - Holiday calendar
     */
    public Map<String, Object> predictCrowd(Long destinationId, LocalDateTime targetTime) {
        Map<String, Object> prediction = new HashMap<>();

        Optional<Destination> optDest = destinationRepo.findById(destinationId);
        if (optDest.isEmpty()) {
            prediction.put("error", "Destination not found");
            return prediction;
        }

        Destination dest = optDest.get();
        int baseCrowd = dest.getCurrentCrowdPercentage();

        // Day of week factor
        double dayFactor = getDayFactor(targetTime.getDayOfWeek());

        // Time of day factor
        double timeFactor = getTimeFactor(targetTime.getHour());

        // Season factor
        double seasonFactor = getSeasonFactor(targetTime.getMonthValue());

        // Holiday factor
        double holidayFactor = getHolidayFactor(targetTime);

        // Calculate predicted crowd
        double predictedCrowd = baseCrowd * dayFactor * timeFactor * seasonFactor * holidayFactor;
        predictedCrowd = Math.min(100, Math.max(0, predictedCrowd));

        prediction.put("destinationId", destinationId);
        prediction.put("destinationName", dest.getName());
        prediction.put("currentCrowd", baseCrowd);
        prediction.put("predictedCrowd", (int) Math.round(predictedCrowd));
        prediction.put("targetTime", targetTime.toString());
        prediction.put("confidence", calculateConfidence(targetTime));

        // Status label
        if (predictedCrowd >= 90) {
            prediction.put("status", "Critical");
            prediction.put("statusColor", "red");
            prediction.put("recommendation", "Avoid visiting. Consider alternatives.");
        } else if (predictedCrowd >= 75) {
            prediction.put("status", "High");
            prediction.put("statusColor", "orange");
            prediction.put("recommendation", "Expect heavy crowds. Plan early morning visit.");
        } else if (predictedCrowd >= 50) {
            prediction.put("status", "Moderate");
            prediction.put("statusColor", "yellow");
            prediction.put("recommendation", "Manageable crowds. Book accommodations in advance.");
        } else if (predictedCrowd >= 25) {
            prediction.put("status", "Low");
            prediction.put("statusColor", "green");
            prediction.put("recommendation", "Great time to visit! Enjoy the peace.");
        } else {
            prediction.put("status", "Very Low");
            prediction.put("statusColor", "green");
            prediction.put("recommendation", "Perfect conditions. Almost no crowds expected.");
        }

        return prediction;
    }

    /**
     * Get crowd predictions for next 48 hours in 6-hour intervals.
     */
    public List<Map<String, Object>> get48HourForecast(Long destinationId) {
        List<Map<String, Object>> forecast = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i <= 48; i += 6) {
            LocalDateTime targetTime = now.plusHours(i);
            Map<String, Object> prediction = predictCrowd(destinationId, targetTime);
            prediction.put("hoursFromNow", i);
            prediction.put("label", formatTimeLabel(targetTime));
            forecast.add(prediction);
        }

        return forecast;
    }

    /**
     * Simulate real-time crowd fluctuation for live demo.
     * Adds small random variations to make the demo feel "live".
     */
    public void simulateLiveCrowdUpdate() {
        List<Destination> destinations = destinationRepo.findAll();
        Random random = new Random();

        for (Destination dest : destinations) {
            // Small random fluctuation (-5 to +5)
            int fluctuation = random.nextInt(11) - 5;
            int newCrowd = Math.min(100, Math.max(5, dest.getCurrentCrowdPercentage() + fluctuation));
            dest.setCurrentCrowdPercentage(newCrowd);

            // Traffic fluctuates similarly
            int trafficFlux = random.nextInt(9) - 4;
            int newTraffic = Math.min(100, Math.max(5, dest.getCurrentTrafficPercentage() + trafficFlux));
            dest.setCurrentTrafficPercentage(newTraffic);

            // Recalculate tourism pressure
            int pressure = (int) ((newCrowd * 0.4) + (newTraffic * 0.3) + (dest.getEnvironmentalLoad() * 0.3));
            dest.setTourismPressure(Math.min(100, pressure));

            destinationRepo.save(dest);
        }
    }

    // --- Factor calculations ---

    private double getDayFactor(DayOfWeek day) {
        return switch (day) {
            case SATURDAY -> 1.35;
            case SUNDAY -> 1.30;
            case FRIDAY -> 1.15;
            case MONDAY -> 0.85;
            default -> 1.0;
        };
    }

    private double getTimeFactor(int hour) {
        if (hour >= 10 && hour <= 14) return 1.25; // Peak daytime
        if (hour >= 15 && hour <= 18) return 1.10; // Afternoon
        if (hour >= 7 && hour <= 9) return 0.90;   // Morning
        if (hour >= 19 && hour <= 21) return 0.80;  // Evening
        return 0.60; // Night
    }

    private double getSeasonFactor(int month) {
        return switch (month) {
            case 5, 6 -> 1.40;    // Peak summer
            case 10 -> 1.30;      // Autumn
            case 12 -> 1.35;      // Winter holidays
            case 4, 11 -> 1.10;   // Shoulder season
            case 7, 8 -> 0.85;    // Monsoon (less tourists)
            case 1, 2 -> 0.90;    // Cold winter
            default -> 1.0;
        };
    }

    private double getHolidayFactor(LocalDateTime dateTime) {
        int month = dateTime.getMonthValue();
        int day = dateTime.getDayOfMonth();

        // Major Indian holidays (simplified)
        if (month == 8 && day == 15) return 1.50; // Independence Day
        if (month == 1 && day == 26) return 1.40; // Republic Day
        if (month == 10 && (day >= 20 && day <= 25)) return 1.45; // Diwali week
        if (month == 3 && (day >= 25 && day <= 31)) return 1.30; // Holi season
        if (month == 12 && (day >= 25 && day <= 31)) return 1.50; // Christmas-New Year

        return 1.0;
    }

    private int calculateConfidence(LocalDateTime targetTime) {
        long hoursAhead = java.time.Duration.between(LocalDateTime.now(), targetTime).toHours();
        if (hoursAhead <= 6) return 92;
        if (hoursAhead <= 12) return 85;
        if (hoursAhead <= 24) return 78;
        if (hoursAhead <= 48) return 65;
        return 50;
    }

    private String formatTimeLabel(LocalDateTime time) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, HH:mm");
        return time.format(formatter);
    }
}
