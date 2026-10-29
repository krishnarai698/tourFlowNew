package com.example.tourFlow.service;

import com.example.tourFlow.model.Destination;
import com.example.tourFlow.model.TouristPreference;
import com.example.tourFlow.repository.DestinationRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final DestinationRepository destinationRepo;

    public RecommendationService(DestinationRepository destinationRepo) {
        this.destinationRepo = destinationRepo;
    }

    /**
     * Core AI recommendation engine.
     * Given tourist preferences, returns ranked list of destinations with match scores and explanations.
     */
    public List<Map<String, Object>> getRecommendations(TouristPreference pref) {
        List<Destination> allDestinations = destinationRepo.findByActiveTrue();
        List<Map<String, Object>> results = new ArrayList<>();

        for (Destination dest : allDestinations) {
            Map<String, Object> result = new HashMap<>();
            result.put("destination", dest);

            // Calculate preference match score (0-100)
            double prefScore = calculatePreferenceScore(dest, pref);

            // Crowd penalty (heavily penalize overcrowded destinations)
            double crowdPenalty = pref.isAvoidCrowds() ? (100.0 - dest.getCurrentCrowdPercentage()) / 100.0 : 0.85;

            // Traffic penalty
            double trafficPenalty = (100.0 - dest.getCurrentTrafficPercentage()) / 100.0;

            // Sustainability bonus (reward eco-friendly choices)
            double sustainabilityBonus = pref.isEcoFriendly() ? (100.0 - dest.getEnvironmentalLoad()) / 100.0 : 0.85;

            // Discovery bonus (reward hidden gems)
            double discoveryBonus = 1.0 + (dest.getDiscoveryScore() / 200.0); // Up to 1.5x

            // Final match score
            double matchScore = prefScore * crowdPenalty * trafficPenalty * sustainabilityBonus * discoveryBonus;
            matchScore = Math.min(99, Math.max(5, matchScore));

            result.put("matchScore", (int) Math.round(matchScore));
            result.put("preferenceScore", (int) Math.round(prefScore));
            result.put("explanation", generateExplanation(dest, pref, matchScore));
            result.put("highlights", getHighlights(dest, pref));

            results.add(result);
        }

        // Sort by match score descending
        results.sort((a, b) -> (int) b.get("matchScore") - (int) a.get("matchScore"));

        return results;
    }

    /**
     * Get smart alternatives for a specific overcrowded destination.
     * This is the "heart" of the system — recommending less crowded alternatives.
     */
    public List<Map<String, Object>> getAlternatives(Long destinationId) {
        Optional<Destination> optDest = destinationRepo.findById(destinationId);
        if (optDest.isEmpty()) return Collections.emptyList();

        Destination original = optDest.get();
        List<Destination> allDest = destinationRepo.findByActiveTrue();
        List<Map<String, Object>> alternatives = new ArrayList<>();

        for (Destination alt : allDest) {
            if (alt.getId().equals(destinationId)) continue;

            double distance = original.distanceTo(alt);
            if (distance > 300) continue; // Only suggest within 300km

            Map<String, Object> altResult = new HashMap<>();
            altResult.put("destination", alt);
            altResult.put("distance", (int) Math.round(distance));

            // Calculate similarity score (how similar is this to the original)
            double similarity = calculateSimilarity(original, alt);
            altResult.put("similarityScore", (int) Math.round(similarity));

            // Calculate overall match considering crowd advantage
            double crowdAdvantage = Math.max(0, original.getCurrentCrowdPercentage() - alt.getCurrentCrowdPercentage());
            double trafficAdvantage = Math.max(0, original.getCurrentTrafficPercentage() - alt.getCurrentTrafficPercentage());

            double matchScore = (similarity * 0.4) + (crowdAdvantage * 0.35) + (trafficAdvantage * 0.25);
            matchScore = Math.min(99, Math.max(5, matchScore));

            altResult.put("matchScore", (int) Math.round(matchScore));
            altResult.put("crowdAdvantage", (int) crowdAdvantage);
            altResult.put("trafficAdvantage", (int) trafficAdvantage);
            altResult.put("explanation", generateAlternativeExplanation(original, alt, matchScore));

            alternatives.add(altResult);
        }

        // Sort by match score descending
        alternatives.sort((a, b) -> (int) b.get("matchScore") - (int) a.get("matchScore"));

        // Return top 5 alternatives
        return alternatives.stream().limit(5).collect(Collectors.toList());
    }

    /**
     * Calculate how well a destination matches tourist preferences.
     */
    private double calculatePreferenceScore(Destination dest, TouristPreference pref) {
        if (pref.getTripTypes() == null || pref.getTripTypes().isEmpty()) return 70.0;

        int matchCount = 0;
        int totalPrefs = pref.getTripTypes().size();

        for (String type : pref.getTripTypes()) {
            switch (type.toLowerCase()) {
                case "adventure" -> { if (dest.isAdventureFriendly()) matchCount++; }
                case "nature" -> { if (dest.isNatureFriendly()) matchCount++; }
                case "family" -> { if (dest.isFamilyFriendly()) matchCount++; }
                case "peaceful" -> { if (dest.isPeacefulFriendly()) matchCount++; }
                case "budget" -> { if (dest.isBudgetFriendly()) matchCount++; }
                case "photography" -> { if (dest.isPhotographyFriendly()) matchCount++; }
                case "snow" -> { if (dest.isSnowFriendly()) matchCount++; }
                case "trekking" -> { if (dest.isTrekkingFriendly()) matchCount++; }
            }
        }

        return (matchCount * 100.0) / totalPrefs;
    }

    /**
     * Calculate similarity between two destinations based on type and attributes.
     */
    private double calculateSimilarity(Destination original, Destination alt) {
        int matches = 0;
        int total = 8;

        if (original.isAdventureFriendly() == alt.isAdventureFriendly()) matches++;
        if (original.isNatureFriendly() == alt.isNatureFriendly()) matches++;
        if (original.isFamilyFriendly() == alt.isFamilyFriendly()) matches++;
        if (original.isPeacefulFriendly() == alt.isPeacefulFriendly()) matches++;
        if (original.isBudgetFriendly() == alt.isBudgetFriendly()) matches++;
        if (original.isPhotographyFriendly() == alt.isPhotographyFriendly()) matches++;
        if (original.isSnowFriendly() == alt.isSnowFriendly()) matches++;
        if (original.isTrekkingFriendly() == alt.isTrekkingFriendly()) matches++;

        // Type match bonus
        if (original.getType().equals(alt.getType())) matches += 2;
        total += 2;

        // State match bonus
        if (original.getState().equals(alt.getState())) matches += 1;
        total += 1;

        return (matches * 100.0) / total;
    }

    /**
     * Generate human-readable explanation for why a destination is recommended.
     */
    private String generateExplanation(Destination dest, TouristPreference pref, double score) {
        StringBuilder sb = new StringBuilder();

        List<String> reasons = new ArrayList<>();

        if (dest.getCurrentCrowdPercentage() < 30) reasons.add("Very low crowds (peaceful experience)");
        else if (dest.getCurrentCrowdPercentage() < 50) reasons.add("Moderate crowd levels");

        if (dest.getDiscoveryScore() > 80) reasons.add("Hidden gem with high Discovery Score");
        if (dest.isBudgetFriendly() && pref.hasType("budget")) reasons.add("Budget-friendly destination");
        if (dest.isAdventureFriendly() && pref.hasType("adventure")) reasons.add("Great adventure activities available");
        if (dest.isNatureFriendly() && pref.hasType("nature")) reasons.add("Stunning natural beauty");
        if (dest.isPeacefulFriendly() && pref.hasType("peaceful")) reasons.add("Peaceful and tranquil atmosphere");
        if (dest.isPhotographyFriendly() && pref.hasType("photography")) reasons.add("Excellent photography spots");
        if (dest.isTrekkingFriendly() && pref.hasType("trekking")) reasons.add("Great trekking trails");
        if (dest.getHotelAvailabilityPercentage() > 70) reasons.add("Good accommodation availability");
        if (dest.getEnvironmentalLoad() < 30) reasons.add("Low environmental impact");

        if (reasons.isEmpty()) reasons.add("Matches your travel preferences");

        sb.append(String.join(". ", reasons)).append(".");
        return sb.toString();
    }

    private String generateAlternativeExplanation(Destination original, Destination alt, double score) {
        StringBuilder sb = new StringBuilder();

        sb.append("Instead of ").append(original.getName()).append(", ");
        sb.append(alt.getName()).append(" offers: ");

        List<String> advantages = new ArrayList<>();

        int crowdDiff = original.getCurrentCrowdPercentage() - alt.getCurrentCrowdPercentage();
        if (crowdDiff > 30) advantages.add(crowdDiff + "% less crowded");
        else if (crowdDiff > 0) advantages.add("Lower crowds");

        int trafficDiff = original.getCurrentTrafficPercentage() - alt.getCurrentTrafficPercentage();
        if (trafficDiff > 30) advantages.add(trafficDiff + "% less traffic");
        else if (trafficDiff > 0) advantages.add("Less traffic");

        if (alt.getHotelAvailabilityPercentage() > original.getHotelAvailabilityPercentage())
            advantages.add("Better hotel availability");

        if (alt.isNatureFriendly()) advantages.add("Beautiful natural scenery");
        if (alt.isPeacefulFriendly()) advantages.add("Peaceful atmosphere");
        if (alt.isBudgetFriendly()) advantages.add("Budget-friendly");
        if (alt.getDiscoveryScore() > 75) advantages.add("Hidden gem experience");

        if (advantages.isEmpty()) advantages.add("Similar experience with better conditions");

        sb.append(String.join(", ", advantages.subList(0, Math.min(4, advantages.size()))));
        sb.append(".");
        return sb.toString();
    }

    /**
     * Get highlight tags for a destination based on preferences.
     */
    private List<String> getHighlights(Destination dest, TouristPreference pref) {
        List<String> highlights = new ArrayList<>();

        if (dest.getCurrentCrowdPercentage() < 25) highlights.add("🟢 Low Crowd");
        if (dest.getDiscoveryScore() > 80) highlights.add("💎 Hidden Gem");
        if (dest.isBudgetFriendly()) highlights.add("💰 Budget");
        if (dest.isAdventureFriendly()) highlights.add("🏔️ Adventure");
        if (dest.isPeacefulFriendly()) highlights.add("🧘 Peaceful");
        if (dest.isPhotographyFriendly()) highlights.add("📸 Photography");
        if (dest.getEnvironmentalLoad() < 25) highlights.add("🌱 Eco-Friendly");

        return highlights.subList(0, Math.min(4, highlights.size()));
    }
}
