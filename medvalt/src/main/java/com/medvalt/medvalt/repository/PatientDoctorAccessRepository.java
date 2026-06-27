package com.medvalt.medvalt.repository;

import com.medvalt.medvalt.entity.PatientDoctorAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientDoctorAccessRepository
        extends JpaRepository<PatientDoctorAccess, Long> {

    Optional<PatientDoctorAccess> findByPatientIdAndDoctorId(Long patientId, Long doctorId);

    List<PatientDoctorAccess> findByPatientId(Long patientId);

    List<PatientDoctorAccess> findByDoctorId(Long doctorId);
}

