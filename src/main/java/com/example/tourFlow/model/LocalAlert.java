package com.example.tourFlow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "local_alerts")
public class LocalAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long destinationId;

    private String destinationName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertSeverity severity;

    @Column(length = 1000)
    private String description;

    private String reportedBy;
    private String contactInfo;

    private boolean verified;
    private boolean active;

    private LocalDateTime reportedAt;
    private LocalDateTime verifiedAt;
    private LocalDateTime resolvedAt;

    private int impactScore; // 0-100, how much this affects tourism

    public enum AlertType {
        ROAD_BLOCKAGE("🚧", "Road Blockage"),
        WATER_SHORTAGE("💧", "Water Shortage"),
        WASTE_PROBLEM("🗑️", "Waste Problem"),
        OVERCROWDING("🚨", "Overcrowding"),
        STRIKE_PROTEST("📢", "Strike/Protest"),
        INFRASTRUCTURE("🏥", "Infrastructure Pressure"),
        TRANSPORT("🚌", "Transport Disruption"),
        WEATHER_WARNING("⛈️", "Weather Warning"),
        SAFETY_CONCERN("⚠️", "Safety Concern");

        private final String emoji;
        private final String label;

        AlertType(String emoji, String label) {
            this.emoji = emoji;
            this.label = label;
        }

        public String getEmoji() { return emoji; }
        public String getLabel() { return label; }
    }

    public enum AlertSeverity {
        LOW("green", "Low"),
        MEDIUM("yellow", "Medium"),
        HIGH("orange", "High"),
        CRITICAL("red", "Critical");

        private final String color;
        private final String label;

        AlertSeverity(String color, String label) {
            this.color = color;
            this.label = label;
        }

        public String getColor() { return color; }
        public String getLabel() { return label; }
    }

    // Constructors
    public LocalAlert() {
        this.reportedAt = LocalDateTime.now();
        this.active = true;
        this.verified = false;
    }

    public LocalAlert(Long destinationId, String destinationName, AlertType type,
                       AlertSeverity severity, String description) {
        this();
        this.destinationId = destinationId;
        this.destinationName = destinationName;
        this.type = type;
        this.severity = severity;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDestinationId() { return destinationId; }
    public void setDestinationId(Long destinationId) { this.destinationId = destinationId; }

    public String getDestinationName() { return destinationName; }
    public void setDestinationName(String destinationName) { this.destinationName = destinationName; }

    public AlertType getType() { return type; }
    public void setType(AlertType type) { this.type = type; }

    public AlertSeverity getSeverity() { return severity; }
    public void setSeverity(AlertSeverity severity) { this.severity = severity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }

    public LocalDateTime getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(LocalDateTime verifiedAt) { this.verifiedAt = verifiedAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public int getImpactScore() { return impactScore; }
    public void setImpactScore(int impactScore) { this.impactScore = impactScore; }
}
