package com.medvalt.medvalt.repository;

import com.medvalt.medvalt.entity.HealthStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HealthStatusRepository extends JpaRepository<HealthStatus, Long> {
    List<HealthStatus> findByPatientIdOrderByRecordedAtDesc(Long patientId);
    Optional<HealthStatus> findTopByPatientIdOrderByRecordedAtDesc(Long patientId);
}