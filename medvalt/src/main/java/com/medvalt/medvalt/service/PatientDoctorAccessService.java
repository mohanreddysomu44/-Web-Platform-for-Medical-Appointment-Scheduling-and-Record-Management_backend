package com.medvalt.medvalt.service;

import com.medvalt.medvalt.entity.Doctor;
import com.medvalt.medvalt.entity.Patient;
import com.medvalt.medvalt.entity.PatientDoctorAccess;
import com.medvalt.medvalt.repository.DoctorRepository;
import com.medvalt.medvalt.repository.PatientDoctorAccessRepository;
import com.medvalt.medvalt.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientDoctorAccessService {

    private final PatientDoctorAccessRepository accessRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public PatientDoctorAccessService(
            PatientDoctorAccessRepository accessRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {
        this.accessRepository = accessRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    // ✅ Grant access
    public void grantAccess(Long patientId, Long doctorId) {
        Optional<PatientDoctorAccess> existing =
                accessRepository.findByPatientIdAndDoctorId(patientId, doctorId);

        if (existing.isPresent()) {
            PatientDoctorAccess access = existing.get();
            access.setGranted(true);
            accessRepository.save(access);
            return;
        }

        Patient patient = patientRepository.findById(patientId).orElseThrow();
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();

        PatientDoctorAccess access = new PatientDoctorAccess();
        access.setPatient(patient);
        access.setDoctor(doctor);
        access.setGranted(true);

        accessRepository.save(access);
    }

    // ✅ Revoke access
    public void revokeAccess(Long patientId, Long doctorId) {
        accessRepository.findByPatientIdAndDoctorId(patientId, doctorId)
                .ifPresent(accessRepository::delete);
    }

    // ✅ Patients list for doctor
    public List<Patient> getPatientsForDoctor(Long doctorId) {
        return accessRepository.findByDoctorId(doctorId)
                .stream()
                .filter(PatientDoctorAccess::isGranted)
                .map(PatientDoctorAccess::getPatient)
                .collect(Collectors.toList());
    }

    // ✅ Access list for patient
    public List<PatientDoctorAccess> getAccessListForPatient(Long patientId) {
        return accessRepository.findByPatientId(patientId);
    }

    // ✅ Access list for doctor
    public List<PatientDoctorAccess> getAccessListForDoctor(Long doctorId) {
        return accessRepository.findByDoctorId(doctorId);
    }
}