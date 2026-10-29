package com.example.tourFlow.service;

import com.example.tourFlow.model.Destination;
import com.example.tourFlow.repository.DestinationRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SustainabilityService {

    private final DestinationRepository destinationRepo;

    public SustainabilityService(DestinationRepository destinationRepo) {
        this.destinationRepo = destinationRepo;
    }

    /**
     * Calculate comprehensive sustainability score for a destination.
     */
    public Map<String, Object> getSustainabilityReport(Long destinationId) {
        Map<String, Object> report = new HashMap<>();

        Optional<Destination> optDest = destinationRepo.findById(destinationId);
        if (optDest.isEmpty()) {
            report.put("error", "Destination not found");
            return report;
        }

        Destination dest = optDest.get();

        // Environmental Load Score
        int envLoad = dest.getEnvironmentalLoad();
        report.put("environmentalLoad", envLoad);
        report.put("envColor", getColor(envLoad));

        // Traffic Impact
        int trafficImpact = dest.getCurrentTrafficPercentage();
        report.put("trafficImpact", trafficImpact);
        report.put("trafficColor", getColor(trafficImpact));

        // Crowd Density
        int crowdDensity = dest.getCurrentCrowdPercentage();
        report.put("crowdDensity", crowdDensity);
        report.put("crowdColor", getColor(crowdDensity));

        // Local Capacity (inverted — higher is better)
        int localCapacity = dest.getHotelAvailabilityPercentage();
        report.put("localCapacity", localCapacity);
        report.put("capacityColor", getColor(100 - localCapacity));

        // Overall Tourism Pressure
        int overallPressure = (int) ((envLoad * 0.3) + (trafficImpact * 0.25) + (crowdDensity * 0.3) + ((100 - localCapacity) * 0.15));
        report.put("tourismPressure", overallPressure);
        report.put("pressureColor", getColor(overallPressure));
        report.put("pressureLabel", getPressureLabel(overallPressure));

        // Discovery Score
        report.put("discoveryScore", dest.getDiscoveryScore());
        report.put("discoveryColor", getColor(100 - dest.getDiscoveryScore()));

        // Sustainability rating (A-F)
        report.put("rating", getSustainabilityRating(overallPressure));
        report.put("ratingDescription", getRatingDescription(overallPressure));

        // Eco recommendations
        report.put("recommendations", getEcoRecommendations(dest));

        return report;
    }

    private String getColor(int value) {
        if (value >= 75) return "red";
        if (value >= 50) return "yellow";
        return "green";
    }

    private String getPressureLabel(int pressure) {
        if (pressure >= 90) return "Critical — Unsustainable";
        if (pressure >= 75) return "High — Needs Attention";
        if (pressure >= 50) return "Moderate — Manageable";
        if (pressure >= 25) return "Low — Sustainable";
        return "Very Low — Ideal";
    }

    private String getSustainabilityRating(int pressure) {
        if (pressure <= 20) return "A+";
        if (pressure <= 35) return "A";
        if (pressure <= 50) return "B";
        if (pressure <= 65) return "C";
        if (pressure <= 80) return "D";
        return "F";
    }

    private String getRatingDescription(int pressure) {
        if (pressure <= 20) return "Excellent sustainability. Minimal impact on environment and local community.";
        if (pressure <= 35) return "Very good sustainability. Well within carrying capacity.";
        if (pressure <= 50) return "Moderate sustainability. Some strain on local resources.";
        if (pressure <= 65) return "Below average. Local infrastructure under pressure.";
        if (pressure <= 80) return "Poor sustainability. Significant strain on environment and community.";
        return "Critical. Destination is severely over-capacity. Urgent intervention needed.";
    }

    private String getEcoRecommendations(Destination dest) {
        StringBuilder rec = new StringBuilder();
        if (dest.getCurrentCrowdPercentage() > 70) rec.append("• Visit during off-peak hours or weekdays\n");
        if (dest.getCurrentTrafficPercentage() > 60) rec.append("• Use public transport or shared vehicles\n");
        if (dest.getEnvironmentalLoad() > 50) rec.append("• Practice responsible waste disposal\n");
        rec.append("• Support local businesses and homestays\n");
        rec.append("• Respect local customs and environment\n");
        if (dest.getEnvironmentalLoad() > 70) rec.append("• Consider nearby less-impacted alternatives\n");
        return rec.toString();
    }
}
