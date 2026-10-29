package com.example.tourFlow.repository;

import com.example.tourFlow.model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

    List<Destination> findByActiveTrue();

    List<Destination> findByState(String state);

    List<Destination> findByType(String type);

    List<Destination> findByHiddenGemTrue();

    @Query("SELECT d FROM Destination d WHERE d.active = true AND d.currentCrowdPercentage < :maxCrowd ORDER BY d.currentCrowdPercentage ASC")
    List<Destination> findLessCrowdedDestinations(int maxCrowd);

    @Query("SELECT d FROM Destination d WHERE d.active = true ORDER BY d.tourismPressure DESC")
    List<Destination> findAllOrderByPressureDesc();

    @Query("SELECT d FROM Destination d WHERE d.active = true ORDER BY d.discoveryScore DESC")
    List<Destination> findTopHiddenGems();

    @Query("SELECT d FROM Destination d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(d.state) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(d.type) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Destination> searchDestinations(String query);

    @Query("SELECT d FROM Destination d WHERE d.active = true AND d.id != :excludeId AND d.currentCrowdPercentage < :maxCrowd ORDER BY d.currentCrowdPercentage ASC")
    List<Destination> findAlternatives(Long excludeId, int maxCrowd);
}
