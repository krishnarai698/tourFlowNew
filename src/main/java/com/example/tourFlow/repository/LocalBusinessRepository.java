package com.example.tourFlow.repository;

import com.example.tourFlow.model.LocalBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalBusinessRepository extends JpaRepository<LocalBusiness, Long> {

    List<LocalBusiness> findByDestinationId(Long destinationId);

    List<LocalBusiness> findByDestinationIdAndType(Long destinationId, LocalBusiness.BusinessType type);

    List<LocalBusiness> findByPromotedTrue();

    List<LocalBusiness> findByDestinationIdAndVerifiedTrue(Long destinationId);
}
