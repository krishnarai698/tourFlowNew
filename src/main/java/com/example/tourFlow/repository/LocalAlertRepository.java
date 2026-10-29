package com.example.tourFlow.repository;

import com.example.tourFlow.model.LocalAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalAlertRepository extends JpaRepository<LocalAlert, Long> {

    List<LocalAlert> findByActiveTrue();

    List<LocalAlert> findByDestinationIdAndActiveTrue(Long destinationId);

    List<LocalAlert> findByActiveTrueOrderByReportedAtDesc();

    List<LocalAlert> findByVerifiedFalseAndActiveTrue();

    List<LocalAlert> findBySeverityAndActiveTrue(LocalAlert.AlertSeverity severity);

    long countByDestinationIdAndActiveTrue(Long destinationId);
}
