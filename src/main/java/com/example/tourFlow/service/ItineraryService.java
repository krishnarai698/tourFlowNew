package com.example.tourFlow.service;

import com.example.tourFlow.model.Destination;
import com.example.tourFlow.repository.DestinationRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItineraryService {

    private final DestinationRepository destinationRepo;

    public ItineraryService(DestinationRepository destinationRepo) {
        this.destinationRepo = destinationRepo;
    }

    /**
     * Generate an AI-optimized itinerary for a destination.
     */
    public Map<String, Object> generateItinerary(Long destinationId, int days) {
        Map<String, Object> itinerary = new HashMap<>();

        Optional<Destination> optDest = destinationRepo.findById(destinationId);
        if (optDest.isEmpty()) {
            itinerary.put("error", "Destination not found");
            return itinerary;
        }

        Destination dest = optDest.get();
        days = Math.min(days, 5); // Max 5-day itinerary

        itinerary.put("destination", dest.getName());
        itinerary.put("days", days);
        itinerary.put("type", dest.getType());

        List<Map<String, Object>> dayPlans = new ArrayList<>();

        // Generate day-by-day plan based on destination
        String[] attractions = dest.getNearbyAttractions() != null ?
                dest.getNearbyAttractions().split(",\\s*") : new String[]{"Local sightseeing"};

        for (int day = 1; day <= days; day++) {
            Map<String, Object> dayPlan = new HashMap<>();
            dayPlan.put("day", day);
            dayPlan.put("title", "Day " + day);

            List<Map<String, String>> activities = new ArrayList<>();

            // Morning activity
            Map<String, String> morning = new HashMap<>();
            morning.put("time", "6:00 AM - 9:00 AM");
            morning.put("activity", day == 1 ? "Arrival & Check-in at accommodation" :
                    "Early morning " + (dest.isTrekkingFriendly() ? "trek to " : "visit to ") +
                            getAttraction(attractions, day * 2 - 2));
            morning.put("icon", "🌅");
            morning.put("tip", "Start early to avoid crowds and enjoy the best weather.");
            activities.add(morning);

            // Mid-morning
            Map<String, String> midMorning = new HashMap<>();
            midMorning.put("time", "9:30 AM - 12:00 PM");
            midMorning.put("activity", "Explore " + getAttraction(attractions, day * 2 - 1));
            midMorning.put("icon", "🏔️");
            midMorning.put("tip", "This is before peak tourist hours — enjoy the relative quiet.");
            activities.add(midMorning);

            // Lunch
            Map<String, String> lunch = new HashMap<>();
            lunch.put("time", "12:30 PM - 1:30 PM");
            lunch.put("activity", "Lunch at local eatery — try " +
                    (dest.getLocalFood() != null ? dest.getLocalFood().split(",")[0].trim() : "local cuisine"));
            lunch.put("icon", "🍲");
            lunch.put("tip", "Support local restaurants and try traditional dishes!");
            activities.add(lunch);

            // Afternoon (rest/avoid peak hours)
            Map<String, String> afternoon = new HashMap<>();
            afternoon.put("time", "2:00 PM - 4:00 PM");
            afternoon.put("activity", day % 2 == 0 ? "Rest & relax at accommodation" :
                    "Visit local market and handicraft shops");
            afternoon.put("icon", day % 2 == 0 ? "😴" : "🛍️");
            afternoon.put("tip", "Peak tourist hours — better to rest or explore nearby quiet spots.");
            activities.add(afternoon);

            // Evening
            Map<String, String> evening = new HashMap<>();
            evening.put("time", "4:30 PM - 6:30 PM");
            evening.put("activity", dest.isPhotographyFriendly() ? "Golden hour photography at viewpoint" :
                    "Evening walk and exploration");
            evening.put("icon", "📸");
            evening.put("tip", "Sunset views are spectacular in the mountains!");
            activities.add(evening);

            dayPlan.put("activities", activities);
            dayPlans.add(dayPlan);
        }

        itinerary.put("dayPlans", dayPlans);

        // Travel tips
        List<String> tips = new ArrayList<>();
        tips.add("🕐 Start your day early (5-6 AM) to avoid peak crowds");
        tips.add("🚗 Check traffic conditions before departing using TourFlow");
        if (dest.isBudgetFriendly()) tips.add("💰 This is a budget-friendly destination — homestays available from ₹500/night");
        tips.add("🌿 Carry reusable water bottles and avoid single-use plastics");
        tips.add("📱 Download offline maps — network coverage may be limited");
        tips.add("🏠 Consider homestays for an authentic local experience");
        itinerary.put("tips", tips);

        return itinerary;
    }

    private String getAttraction(String[] attractions, int index) {
        if (attractions.length == 0) return "Local sightseeing";
        return attractions[index % attractions.length].trim();
    }
}
