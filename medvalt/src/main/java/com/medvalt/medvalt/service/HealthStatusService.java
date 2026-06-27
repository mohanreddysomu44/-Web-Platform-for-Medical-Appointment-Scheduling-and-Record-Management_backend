package com.medvalt.medvalt.service;

import com.medvalt.medvalt.entity.HealthStatus;
import com.medvalt.medvalt.entity.Patient;
import com.medvalt.medvalt.repository.HealthStatusRepository;
import com.medvalt.medvalt.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HealthStatusService {

    private final HealthStatusRepository healthStatusRepository;
    private final PatientRepository patientRepository;

    public HealthStatusService(HealthStatusRepository healthStatusRepository,
                               PatientRepository patientRepository) {
        this.healthStatusRepository = healthStatusRepository;
        this.patientRepository = patientRepository;
    }

    // Get all records for a patient (history)
    public List<HealthStatus> getHistory(Long patientId) {
        return healthStatusRepository.findByPatientIdOrderByRecordedAtDesc(patientId);
    }

    // Get latest record
    public Optional<HealthStatus> getLatest(Long patientId) {
        return healthStatusRepository.findTopByPatientIdOrderByRecordedAtDesc(patientId);
    }

    // Save new entry
    public HealthStatus save(Long patientId, HealthStatus incoming) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + patientId));
        incoming.setPatient(patient);
        return healthStatusRepository.save(incoming);
    }

    // Update existing entry
    public HealthStatus update(Long id, HealthStatus incoming) {
        HealthStatus existing = healthStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HealthStatus not found: " + id));

        existing.setBloodSugarLevel(incoming.getBloodSugarLevel());
        existing.setSugarType(incoming.getSugarType());
        existing.setBodyTemperature(incoming.getBodyTemperature());
        existing.setBloodPressure(incoming.getBloodPressure());
        existing.setHeartRate(incoming.getHeartRate());
        existing.setOxygenSaturation(incoming.getOxygenSaturation());
        existing.setRespiratoryRate(incoming.getRespiratoryRate());
        existing.setHeight(incoming.getHeight());
        existing.setWeight(incoming.getWeight());

        return healthStatusRepository.save(existing);
    }

    // Delete
    public void delete(Long id) {
        healthStatusRepository.deleteById(id);
    }
}