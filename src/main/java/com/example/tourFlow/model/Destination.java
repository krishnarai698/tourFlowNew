package com.example.tourFlow.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "destinations")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String state;

    private String district;

    @Column(nullable = false)
    private String type; // hill_station, valley, village, trek, heritage, eco_tourism

    private double latitude;
    private double longitude;
    private int altitude; // meters

    private int maxCapacity; // max tourists per day

    @Column(length = 2000)
    private String description;

    private String imageUrl;

    // Scores
    private int discoveryScore; // 0-100, how "hidden" this gem is
    private boolean hiddenGem;

    // Preference tags
    private boolean adventureFriendly;
    private boolean natureFriendly;
    private boolean familyFriendly;
    private boolean peacefulFriendly;
    private boolean budgetFriendly;
    private boolean photographyFriendly;
    private boolean snowFriendly;
    private boolean trekkingFriendly;

    // Current real-time status (updated periodically)
    private int currentCrowdPercentage;
    private int currentTrafficPercentage;
    private int currentParkingPercentage;
    private int hotelAvailabilityPercentage; // available rooms %
    private int tourismPressure; // 0-100 composite score
    private int environmentalLoad; // 0-100

    // Weather (cached from API)
    private String weatherCondition;
    private double temperature;
    private int humidity;

    // Nearby info
    private String nearbyAttractions;
    private String localFood;
    private String transportOptions;

    // Status flags
    private boolean active;
    private boolean alertActive;
    private String alertMessage;

    // Constructors
    public Destination() {
        this.active = true;
    }

    public Destination(String name, String state, String type, double latitude, double longitude) {
        this.name = name;
        this.state = state;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.active = true;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public int getAltitude() { return altitude; }
    public void setAltitude(int altitude) { this.altitude = altitude; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getDiscoveryScore() { return discoveryScore; }
    public void setDiscoveryScore(int discoveryScore) { this.discoveryScore = discoveryScore; }

    public boolean isHiddenGem() { return hiddenGem; }
    public void setHiddenGem(boolean hiddenGem) { this.hiddenGem = hiddenGem; }

    public boolean isAdventureFriendly() { return adventureFriendly; }
    public void setAdventureFriendly(boolean adventureFriendly) { this.adventureFriendly = adventureFriendly; }

    public boolean isNatureFriendly() { return natureFriendly; }
    public void setNatureFriendly(boolean natureFriendly) { this.natureFriendly = natureFriendly; }

    public boolean isFamilyFriendly() { return familyFriendly; }
    public void setFamilyFriendly(boolean familyFriendly) { this.familyFriendly = familyFriendly; }

    public boolean isPeacefulFriendly() { return peacefulFriendly; }
    public void setPeacefulFriendly(boolean peacefulFriendly) { this.peacefulFriendly = peacefulFriendly; }

    public boolean isBudgetFriendly() { return budgetFriendly; }
    public void setBudgetFriendly(boolean budgetFriendly) { this.budgetFriendly = budgetFriendly; }

    public boolean isPhotographyFriendly() { return photographyFriendly; }
    public void setPhotographyFriendly(boolean photographyFriendly) { this.photographyFriendly = photographyFriendly; }

    public boolean isSnowFriendly() { return snowFriendly; }
    public void setSnowFriendly(boolean snowFriendly) { this.snowFriendly = snowFriendly; }

    public boolean isTrekkingFriendly() { return trekkingFriendly; }
    public void setTrekkingFriendly(boolean trekkingFriendly) { this.trekkingFriendly = trekkingFriendly; }

    public int getCurrentCrowdPercentage() { return currentCrowdPercentage; }
    public void setCurrentCrowdPercentage(int currentCrowdPercentage) { this.currentCrowdPercentage = currentCrowdPercentage; }

    public int getCurrentTrafficPercentage() { return currentTrafficPercentage; }
    public void setCurrentTrafficPercentage(int currentTrafficPercentage) { this.currentTrafficPercentage = currentTrafficPercentage; }

    public int getCurrentParkingPercentage() { return currentParkingPercentage; }
    public void setCurrentParkingPercentage(int currentParkingPercentage) { this.currentParkingPercentage = currentParkingPercentage; }

    public int getHotelAvailabilityPercentage() { return hotelAvailabilityPercentage; }
    public void setHotelAvailabilityPercentage(int hotelAvailabilityPercentage) { this.hotelAvailabilityPercentage = hotelAvailabilityPercentage; }

    public int getTourismPressure() { return tourismPressure; }
    public void setTourismPressure(int tourismPressure) { this.tourismPressure = tourismPressure; }

    public int getEnvironmentalLoad() { return environmentalLoad; }
    public void setEnvironmentalLoad(int environmentalLoad) { this.environmentalLoad = environmentalLoad; }

    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public String getNearbyAttractions() { return nearbyAttractions; }
    public void setNearbyAttractions(String nearbyAttractions) { this.nearbyAttractions = nearbyAttractions; }

    public String getLocalFood() { return localFood; }
    public void setLocalFood(String localFood) { this.localFood = localFood; }

    public String getTransportOptions() { return transportOptions; }
    public void setTransportOptions(String transportOptions) { this.transportOptions = transportOptions; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isAlertActive() { return alertActive; }
    public void setAlertActive(boolean alertActive) { this.alertActive = alertActive; }

    public String getAlertMessage() { return alertMessage; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }

    // Helper methods
    public String getCrowdLevel() {
        if (currentCrowdPercentage >= 90) return "Critical";
        if (currentCrowdPercentage >= 75) return "High";
        if (currentCrowdPercentage >= 50) return "Moderate";
        if (currentCrowdPercentage >= 25) return "Low";
        return "Very Low";
    }

    public String getCrowdColor() {
        if (currentCrowdPercentage >= 75) return "red";
        if (currentCrowdPercentage >= 50) return "yellow";
        return "green";
    }

    public String getTrafficColor() {
        if (currentTrafficPercentage >= 75) return "red";
        if (currentTrafficPercentage >= 50) return "yellow";
        return "green";
    }

    public String getPressureColor() {
        if (tourismPressure >= 75) return "red";
        if (tourismPressure >= 50) return "yellow";
        return "green";
    }

    /**
     * Calculate distance (in km) to another destination using Haversine formula.
     */
    public double distanceTo(Destination other) {
        final int R = 6371; // Earth's radius in km
        double latDiff = Math.toRadians(other.latitude - this.latitude);
        double lonDiff = Math.toRadians(other.longitude - this.longitude);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude))
                * Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
