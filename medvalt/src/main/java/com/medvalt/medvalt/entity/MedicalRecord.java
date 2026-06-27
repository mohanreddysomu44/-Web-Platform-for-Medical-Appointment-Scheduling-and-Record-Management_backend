package com.medvalt.medvalt.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Link to patient
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // ✅ Record metadata
    private String title;          // e.g. "Blood Test Report", "X-Ray"
    private String recordType;     // e.g. "LAB_REPORT", "PRESCRIPTION", "SCAN", "OTHER"
    private String description;    // optional notes

    private LocalDate recordDate;  // date of the medical event

    // ✅ Uploaded file (stored on disk, similar to appointment reports)
    private String filePath;       // e.g. "medical-records/1234567_report.pdf"

    // ✅ Doctor who issued (optional — can be null if patient uploads themselves)
    private String issuedBy;       // free-text doctor name

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getIssuedBy() { return issuedBy; }
    public void setIssuedBy(String issuedBy) { this.issuedBy = issuedBy; }

    // ✅ Expose just the filename to the frontend (same pattern as Appointment.java)
    @Transient
    public String getFileName() {
        if (filePath == null || filePath.isBlank()) return null;
        return java.nio.file.Paths.get(filePath).getFileName().toString();
    }
}