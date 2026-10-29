package com.example.tourFlow.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Non-entity model for capturing tourist preferences in the AI recommendation flow.
 * This is used as a form-backing bean, not persisted to DB.
 */
public class TouristPreference {

    private List<String> tripTypes = new ArrayList<>(); // adventure, nature, family, peaceful, budget, photography, snow, trekking

    private String originLocation; // where the tourist is coming from
    private int maxDistanceKm = 200; // max travel distance willing
    private int maxBudgetPerDay = 5000; // INR per day
    private int groupSize = 2;
    private int tripDays = 3;
    private boolean avoidCrowds = true;
    private boolean ecoFriendly = false;
    private String preferredState = "Himachal Pradesh";

    // Constructors
    public TouristPreference() {}

    // Getters and Setters
    public List<String> getTripTypes() { return tripTypes; }
    public void setTripTypes(List<String> tripTypes) { this.tripTypes = tripTypes; }

    public String getOriginLocation() { return originLocation; }
    public void setOriginLocation(String originLocation) { this.originLocation = originLocation; }

    public int getMaxDistanceKm() { return maxDistanceKm; }
    public void setMaxDistanceKm(int maxDistanceKm) { this.maxDistanceKm = maxDistanceKm; }

    public int getMaxBudgetPerDay() { return maxBudgetPerDay; }
    public void setMaxBudgetPerDay(int maxBudgetPerDay) { this.maxBudgetPerDay = maxBudgetPerDay; }

    public int getGroupSize() { return groupSize; }
    public void setGroupSize(int groupSize) { this.groupSize = groupSize; }

    public int getTripDays() { return tripDays; }
    public void setTripDays(int tripDays) { this.tripDays = tripDays; }

    public boolean isAvoidCrowds() { return avoidCrowds; }
    public void setAvoidCrowds(boolean avoidCrowds) { this.avoidCrowds = avoidCrowds; }

    public boolean isEcoFriendly() { return ecoFriendly; }
    public void setEcoFriendly(boolean ecoFriendly) { this.ecoFriendly = ecoFriendly; }

    public String getPreferredState() { return preferredState; }
    public void setPreferredState(String preferredState) { this.preferredState = preferredState; }

    /**
     * Check if a specific trip type is selected.
     */
    public boolean hasType(String type) {
        return tripTypes != null && tripTypes.contains(type);
    }
}
