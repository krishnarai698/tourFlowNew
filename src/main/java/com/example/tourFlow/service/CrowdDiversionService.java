package com.example.tourFlow.service;

import com.example.tourFlow.model.Destination;
import com.example.tourFlow.repository.DestinationRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CrowdDiversionService {

    private final DestinationRepository destinationRepo;

    public CrowdDiversionService(DestinationRepository destinationRepo) {
        this.destinationRepo = destinationRepo;
    }

    /**
     * Calculate optimal tourist distribution when a destination is overcrowded.
     * Distributes expected tourists across nearby alternatives based on capacity.
     */
    public Map<String, Object> calculateDiversion(Long overloadedDestId, int expectedTourists) {
        Map<String, Object> result = new HashMap<>();

        Optional<Destination> optDest = destinationRepo.findById(overloadedDestId);
        if (optDest.isEmpty()) {
            result.put("error", "Destination not found");
            return result;
        }

        Destination overloaded = optDest.get();
        result.put("originalDestination", overloaded.getName());
        result.put("totalExpectedTourists", expectedTourists);

        // Get all destinations sorted by remaining capacity
        List<Destination> alternatives = destinationRepo.findByActiveTrue();
        alternatives.removeIf(d -> d.getId().equals(overloadedDestId));

        // Calculate remaining capacity for each alternative
        List<Map<String, Object>> distribution = new ArrayList<>();
        int remainingToDistribute = expectedTourists;

        // First, allocate to the overloaded destination up to its safe capacity (60%)
        int safeCapacity = (int) (overloaded.getMaxCapacity() * 0.6);
        int allocatedToOriginal = Math.min(expectedTourists, safeCapacity);
        remainingToDistribute -= allocatedToOriginal;

        Map<String, Object> originalAlloc = new HashMap<>();
        originalAlloc.put("destination", overloaded.getName());
        originalAlloc.put("allocated", allocatedToOriginal);
        originalAlloc.put("percentage", (allocatedToOriginal * 100) / expectedTourists);
        originalAlloc.put("capacityUsed", (allocatedToOriginal * 100) / overloaded.getMaxCapacity());
        originalAlloc.put("color", "orange");
        distribution.add(originalAlloc);

        // Sort alternatives by: low crowd + proximity + capacity
        alternatives.sort(Comparator.comparingInt(Destination::getCurrentCrowdPercentage));

        for (Destination alt : alternatives) {
            if (remainingToDistribute <= 0) break;

            double distance = overloaded.distanceTo(alt);
            if (distance > 250) continue; // Skip if too far

            int availableCapacity = alt.getMaxCapacity() - (int)(alt.getMaxCapacity() * alt.getCurrentCrowdPercentage() / 100.0);
            int safeAllocate = (int)(availableCapacity * 0.7); // Only fill to 70% of available

            if (safeAllocate <= 0) continue;

            int allocate = Math.min(remainingToDistribute, safeAllocate);
            remainingToDistribute -= allocate;

            Map<String, Object> altAlloc = new HashMap<>();
            altAlloc.put("destination", alt.getName());
            altAlloc.put("allocated", allocate);
            altAlloc.put("percentage", (allocate * 100) / expectedTourists);
            altAlloc.put("distance", (int) distance);
            altAlloc.put("currentCrowd", alt.getCurrentCrowdPercentage());
            altAlloc.put("capacityUsed", ((alt.getCurrentCrowdPercentage() * alt.getMaxCapacity() / 100 + allocate) * 100) / alt.getMaxCapacity());
            altAlloc.put("color", alt.getCurrentCrowdPercentage() < 40 ? "green" : "yellow");
            distribution.add(altAlloc);
        }

        result.put("distribution", distribution);
        result.put("successfullyDiverted", expectedTourists - remainingToDistribute);
        result.put("unallocated", remainingToDistribute);
        result.put("diversionEfficiency", ((expectedTourists - remainingToDistribute) * 100) / expectedTourists);

        return result;
    }

    /**
     * Get a summary of current tourism pressure across all destinations.
     */
    public Map<String, Object> getSystemOverview() {
        Map<String, Object> overview = new HashMap<>();
        List<Destination> all = destinationRepo.findAllOrderByPressureDesc();

        int totalCapacity = 0;
        int currentLoad = 0;
        int criticalCount = 0;
        int highCount = 0;
        int moderateCount = 0;
        int lowCount = 0;

        for (Destination d : all) {
            totalCapacity += d.getMaxCapacity();
            currentLoad += (int)(d.getMaxCapacity() * d.getCurrentCrowdPercentage() / 100.0);

            if (d.getTourismPressure() >= 90) criticalCount++;
            else if (d.getTourismPressure() >= 75) highCount++;
            else if (d.getTourismPressure() >= 50) moderateCount++;
            else lowCount++;
        }

        overview.put("destinations", all);
        overview.put("totalDestinations", all.size());
        overview.put("totalCapacity", totalCapacity);
        overview.put("currentLoad", currentLoad);
        overview.put("overallPressure", (currentLoad * 100) / Math.max(1, totalCapacity));
        overview.put("criticalCount", criticalCount);
        overview.put("highCount", highCount);
        overview.put("moderateCount", moderateCount);
        overview.put("lowCount", lowCount);

        return overview;
    }
}
