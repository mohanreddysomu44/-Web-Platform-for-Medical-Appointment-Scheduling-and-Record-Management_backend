package com.medvalt.medvalt.service;

import com.medvalt.medvalt.entity.Patient;
import com.medvalt.medvalt.entity.User;
import com.medvalt.medvalt.repository.PatientRepository;
import com.medvalt.medvalt.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientService(PatientRepository patientRepository,
                          UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    // ✅ Get all patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // ✅ Get patient by ID
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    // ✅ Get patient by User ID (NEW)
    public Patient getPatientByUserId(Long userId) {
        return patientRepository.findByUserId(userId).orElse(null);
    }

    // ✅ Save patient
    public Patient savePatient(Patient patient) {

        // Attach real User entity
        if (patient.getUser() != null && patient.getUser().getId() != null) {
            User user = userRepository.findById(patient.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            patient.setUser(user);
        }

        return patientRepository.save(patient);
    }

    // ✅ Delete patient
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}