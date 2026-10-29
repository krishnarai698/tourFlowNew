package com.example.tourFlow.model;

import jakarta.persistence.*;

@Entity
@Table(name = "local_businesses")
public class LocalBusiness {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long destinationId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusinessType type;

    @Column(length = 1000)
    private String description;

    private String contactNumber;
    private String address;

    private double rating; // 1-5
    private int priceRange; // 1-3 (budget, mid, premium)

    private boolean verified;
    private boolean promoted; // highlighted when tourists diverted here

    private String imageUrl;
    private String tags; // comma-separated: "organic,traditional,eco-friendly"

    public enum BusinessType {
        HOMESTAY("🏠", "Homestay"),
        RESTAURANT("🍲", "Restaurant"),
        SHOP("🛍️", "Local Shop"),
        HANDICRAFT("🎨", "Handicraft"),
        GUIDE("🧑‍🌾", "Local Guide"),
        EXPERIENCE("🚶", "Local Experience"),
        TRANSPORT("🚌", "Transport"),
        ADVENTURE("🏔️", "Adventure Activity");

        private final String emoji;
        private final String label;

        BusinessType(String emoji, String label) {
            this.emoji = emoji;
            this.label = label;
        }

        public String getEmoji() { return emoji; }
        public String getLabel() { return label; }
    }

    // Constructors
    public LocalBusiness() {
        this.verified = true;
    }

    public LocalBusiness(Long destinationId, String name, BusinessType type, String description) {
        this();
        this.destinationId = destinationId;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDestinationId() { return destinationId; }
    public void setDestinationId(Long destinationId) { this.destinationId = destinationId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BusinessType getType() { return type; }
    public void setType(BusinessType type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getPriceRange() { return priceRange; }
    public void setPriceRange(int priceRange) { this.priceRange = priceRange; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public boolean isPromoted() { return promoted; }
    public void setPromoted(boolean promoted) { this.promoted = promoted; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getPriceLabel() {
        return switch (priceRange) {
            case 1 -> "₹ Budget";
            case 2 -> "₹₹ Mid-Range";
            case 3 -> "₹₹₹ Premium";
            default -> "₹ Budget";
        };
    }
}
