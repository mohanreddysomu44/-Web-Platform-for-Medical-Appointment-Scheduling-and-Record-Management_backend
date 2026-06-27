package com.medvalt.medvalt.service;

import com.medvalt.medvalt.entity.MedicalRecord;
import com.medvalt.medvalt.entity.Patient;
import com.medvalt.medvalt.repository.MedicalRecordRepository;
import com.medvalt.medvalt.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository,
                                PatientRepository patientRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
    }

    // ✅ Get all records for a patient (newest first)
    public List<MedicalRecord> getRecordsForPatient(Long patientId) {
        return medicalRecordRepository.findByPatientIdOrderByRecordDateDesc(patientId);
    }

    // ✅ Get records by type for a patient
    public List<MedicalRecord> getRecordsByType(Long patientId, String recordType) {
        return medicalRecordRepository.findByPatientIdAndRecordTypeOrderByRecordDateDesc(patientId, recordType);
    }

    // ✅ Upload a new medical record (with optional file attachment)
    public MedicalRecord uploadRecord(Long patientId,
                                      String title,
                                      String recordType,
                                      String description,
                                      String recordDate,
                                      String issuedBy,
                                      MultipartFile file) throws IOException {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + patientId));

        MedicalRecord record = new MedicalRecord();
        record.setPatient(patient);
        record.setTitle(title);
        record.setRecordType(recordType);
        record.setDescription(description);
        record.setIssuedBy(issuedBy);
        record.setRecordDate(recordDate != null && !recordDate.isBlank()
                ? LocalDate.parse(recordDate)
                : LocalDate.now());

        // ✅ Save file to disk (same pattern as AppointmentService)
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get("medical-records/" + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            record.setFilePath(path.toString());
        }

        return medicalRecordRepository.save(record);
    }

    // ✅ Delete a record
    public void deleteRecord(Long recordId) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        // Also delete the file from disk
        if (record.getFilePath() != null) {
            try { Files.deleteIfExists(Paths.get(record.getFilePath())); }
            catch (IOException ignored) {}
        }

        medicalRecordRepository.deleteById(recordId);
    }
}