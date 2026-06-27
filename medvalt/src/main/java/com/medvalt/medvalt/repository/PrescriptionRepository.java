package com.medvalt.medvalt.repository;

import com.medvalt.medvalt.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    Optional<Prescription> findByAppointmentId(Long appointmentId);
    List<Prescription> findByAppointmentPatientId(Long patientId);
    List<Prescription> findByAppointmentDoctorId(Long doctorId);
}