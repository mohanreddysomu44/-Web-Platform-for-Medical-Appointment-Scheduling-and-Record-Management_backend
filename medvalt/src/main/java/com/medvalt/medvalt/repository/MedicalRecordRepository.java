package com.medvalt.medvalt.repository;

import com.medvalt.medvalt.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    // ✅ Get all records for a specific patient (used by patient dashboard)
    List<MedicalRecord> findByPatientIdOrderByRecordDateDesc(Long patientId);

    // ✅ Filter by type (e.g. LAB_REPORT, PRESCRIPTION)
    List<MedicalRecord> findByPatientIdAndRecordTypeOrderByRecordDateDesc(Long patientId, String recordType);
}