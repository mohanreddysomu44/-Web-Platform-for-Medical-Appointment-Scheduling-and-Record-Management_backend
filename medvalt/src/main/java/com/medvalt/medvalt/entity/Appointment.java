//////package com.medvalt.medvalt.entity;
//////
//////import jakarta.persistence.*;
//////import java.time.LocalDate;
//////
//////@Entity
//////public class Appointment {
//////
//////    @Id
//////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//////    private Long id;
//////
//////    @ManyToOne
//////    @JoinColumn(name = "patient_id")
//////    private Patient patient;
//////
//////    @ManyToOne
//////    @JoinColumn(name = "doctor_id")
//////    private Doctor doctor;
//////
//////    private LocalDate date;
//////    private String timeSlot;
//////
//////    private String status; // PENDING, APPROVED, REJECTED
//////
//////    // ✅ NEW FIELDS
//////    @Column(length = 1000)
//////    private String description;
//////
//////    private String reportFilePath; // store file path
//////
//////    // =============================
//////    // Getters and Setters
//////    // =============================
//////
//////    public Long getId() { return id; }
//////    public void setId(Long id) { this.id = id; }
//////
//////    public Patient getPatient() { return patient; }
//////    public void setPatient(Patient patient) { this.patient = patient; }
//////
//////    public Doctor getDoctor() { return doctor; }
//////    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
//////
//////    public LocalDate getDate() { return date; }
//////    public void setDate(LocalDate date) { this.date = date; }
//////
//////    public String getTimeSlot() { return timeSlot; }
//////    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
//////
//////    public String getStatus() { return status; }
//////    public void setStatus(String status) { this.status = status; }
//////
//////    public String getDescription() { return description; }
//////    public void setDescription(String description) { this.description = description; }
//////
//////    public String getReportFilePath() { return reportFilePath; }
//////    public void setReportFilePath(String reportFilePath) { this.reportFilePath = reportFilePath; }
//////}
////
////
////package com.medvalt.medvalt.entity;
////
////import com.fasterxml.jackson.annotation.JsonProperty;
////import jakarta.persistence.*;
////import java.nio.file.Paths;
////import java.time.LocalDate;
////
////@Entity
////public class Appointment {
////
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
////
////    @ManyToOne
////    @JoinColumn(name = "patient_id")
////    private Patient patient;
////
////    @ManyToOne
////    @JoinColumn(name = "doctor_id")
////    private Doctor doctor;
////
////    private LocalDate date;
////    private String timeSlot;
////
////    private String status; // PENDING, APPROVED, REJECTED
////
////    @Column(length = 1000)
////    private String description;
////
////    private String reportFilePath; // stored as e.g. "uploads/1234567890_report.pdf"
////
////    // =============================
////    // Getters and Setters
////    // =============================
////
////    public Long getId() { return id; }
////    public void setId(Long id) { this.id = id; }
////
////    public Patient getPatient() { return patient; }
////    public void setPatient(Patient patient) { this.patient = patient; }
////
////    public Doctor getDoctor() { return doctor; }
////    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
////
////    public LocalDate getDate() { return date; }
////    public void setDate(LocalDate date) { this.date = date; }
////
////    public String getTimeSlot() { return timeSlot; }
////    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
////
////    public String getStatus() { return status; }
////    public void setStatus(String status) { this.status = status; }
////
////    public String getDescription() { return description; }
////    public void setDescription(String description) { this.description = description; }
////
////    public String getReportFilePath() { return reportFilePath; }
////    public void setReportFilePath(String reportFilePath) { this.reportFilePath = reportFilePath; }
////
////    /**
////     * ✅ This is what the frontend receives as "report".
////     * It exposes ONLY the filename (e.g. "1234567890_myfile.pdf")
////     * so the React code can build: GET /appointments/report/1234567890_myfile.pdf
////     *
////     * If no report was uploaded, returns null → frontend shows "No report attached"
////     */
////    @JsonProperty("report")
////    public String getReportFilename() {
////        if (reportFilePath == null || reportFilePath.isBlank()) {
////            return null;
////        }
////        // Strip the "uploads/" or "uploads\" prefix, return only filename
////        return Paths.get(reportFilePath).getFileName().toString();
////    }
////}
////
////package com.medvalt.medvalt.entity;
////
////import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
////import com.fasterxml.jackson.annotation.JsonProperty;
////import jakarta.persistence.*;
////import java.nio.file.Paths;
////import java.time.LocalDate;
////
////@Entity
////public class Appointment {
////
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
////
////    @ManyToOne
////    @JoinColumn(name = "patient_id")
////    @JsonIgnoreProperties({"appointments", "user", "medicalRecords", "hibernateLazyInitializer", "handler"})
////    private Patient patient;
////
////    @ManyToOne
////    @JoinColumn(name = "doctor_id")
////    @JsonIgnoreProperties({"appointments", "user", "hibernateLazyInitializer", "handler"})
////    private Doctor doctor;
////
////    private LocalDate date;
////    private String timeSlot;
////    private String status;
////
////    @Column(length = 1000)
////    private String description;
////
////    private String reportFilePath;
////
////    public Long getId() { return id; }
////    public void setId(Long id) { this.id = id; }
////
////    public Patient getPatient() { return patient; }
////    public void setPatient(Patient patient) { this.patient = patient; }
////
////    public Doctor getDoctor() { return doctor; }
////    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
////
////    public LocalDate getDate() { return date; }
////    public void setDate(LocalDate date) { this.date = date; }
////
////    public String getTimeSlot() { return timeSlot; }
////    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
////
////    public String getStatus() { return status; }
////    public void setStatus(String status) { this.status = status; }
////
////    public String getDescription() { return description; }
////    public void setDescription(String description) { this.description = description; }
////
////    public String getReportFilePath() { return reportFilePath; }
////    public void setReportFilePath(String reportFilePath) { this.reportFilePath = reportFilePath; }
////
////    @JsonProperty("report")
////    public String getReportFilename() {
////        if (reportFilePath == null || reportFilePath.isBlank()) {
////            return null;
////        }
////        return Paths.get(reportFilePath).getFileName().toString();
////    }
////}
//
//
//
//package com.medvalt.medvalt.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import jakarta.persistence.*;
//import java.nio.file.Paths;
//import java.time.LocalDate;
//
//@Entity
//public class Appointment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "patient_id")
//    @JsonIgnoreProperties({"appointments", "user", "medicalRecords", "hibernateLazyInitializer", "handler"})
//    private Patient patient;
//
//    @ManyToOne
//    @JoinColumn(name = "doctor_id")
//    @JsonIgnoreProperties({"appointments", "user", "hibernateLazyInitializer", "handler"})
//    private Doctor doctor;
//
//    private LocalDate date;
//    private String timeSlot;
//    private String status;
//
//    @Column(length = 1000)
//    private String description;
//
//    private String reportFilePath;
//
//    // ✅ ADD THESE 3 LINES — breaks the Prescription ↔ Appointment infinite loop
//    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonIgnoreProperties({"appointment", "hibernateLazyInitializer", "handler"})
//    private Prescription prescription;
//
//    // ── all your existing getters/setters unchanged ──
//
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public Patient getPatient() { return patient; }
//    public void setPatient(Patient patient) { this.patient = patient; }
//
//    public Doctor getDoctor() { return doctor; }
//    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
//
//    public LocalDate getDate() { return date; }
//    public void setDate(LocalDate date) { this.date = date; }
//
//    public String getTimeSlot() { return timeSlot; }
//    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
//
//    public String getStatus() { return status; }
//    public void setStatus(String status) { this.status = status; }
//
//    public String getDescription() { return description; }
//    public void setDescription(String description) { this.description = description; }
//
//    public String getReportFilePath() { return reportFilePath; }
//    public void setReportFilePath(String reportFilePath) { this.reportFilePath = reportFilePath; }
//
//    // ✅ ADD getter/setter for prescription too
//    public Prescription getPrescription() { return prescription; }
//    public void setPrescription(Prescription prescription) { this.prescription = prescription; }
//
//    @JsonProperty("report")
//    public String getReportFilename() {
//        if (reportFilePath == null || reportFilePath.isBlank()) return null;
//        return Paths.get(reportFilePath).getFileName().toString();
//    }
//}


package com.medvalt.medvalt.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private String description;
    private LocalDate date;
    private String timeSlot;

    // PENDING → APPROVED → REJECTED
    private String status = "PENDING";

    // Old field — kept for backward compat (appointment reports)
    private String reportFilePath;

    // ── NEW fields for doctor-requested document sharing ──────────────────────

    // DOC_REQUEST_NONE   = doctor hasn't asked yet
    // DOC_REQUEST_PENDING = doctor requested, patient hasn't uploaded yet
    // DOC_REQUEST_DONE   = patient uploaded the documents
    @Column(nullable = false)
    private String docRequestStatus = "DOC_REQUEST_NONE";

    // Optional message from doctor when requesting documents
    private String docRequestMessage;

    // Uploaded document filename (patient uploads after doctor requests)
    private String docFileName;

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReportFilePath() { return reportFilePath; }
    public void setReportFilePath(String reportFilePath) { this.reportFilePath = reportFilePath; }

    // convenience getter used in frontend (returns just the filename part)
    public String getReport() {
        if (reportFilePath == null) return null;
        return java.nio.file.Paths.get(reportFilePath).getFileName().toString();
    }

    public String getDocRequestStatus() { return docRequestStatus; }
    public void setDocRequestStatus(String docRequestStatus) { this.docRequestStatus = docRequestStatus; }

    public String getDocRequestMessage() { return docRequestMessage; }
    public void setDocRequestMessage(String docRequestMessage) { this.docRequestMessage = docRequestMessage; }

    public String getDocFileName() { return docFileName; }
    public void setDocFileName(String docFileName) { this.docFileName = docFileName; }
}