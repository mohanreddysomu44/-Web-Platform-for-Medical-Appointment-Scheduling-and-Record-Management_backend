package com.medvalt.medvalt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "doctor_verifications")
public class DoctorVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user"})
    private Doctor doctor;

    // ── Personal Identity ──────────────────────────────────────────────────────
    private String fullLegalName;
    private LocalDate dateOfBirth;
    private String gender;
    private String nationality;

    // ── Aadhaar (mandatory in India) ───────────────────────────────────────────
    private String aadhaarNumber;
    private String aadhaarMobile;      // last 4 digits of registered mobile
    private String aadhaarFile;        // uploaded file name

    // ── NMC / State Medical Council Registration ───────────────────────────────
    private String medicalRegistrationNumber;
    private String medicalCouncil;
    private LocalDate registrationDate;
    private LocalDate registrationExpiry;
    private String registrationType;   // Allopathy, Ayurveda, etc.

    // ── Secondary Government ID ────────────────────────────────────────────────
    private String idProofType;
    private String idProofNumber;

    // ── Education ─────────────────────────────────────────────────────────────
    private String degreeName;
    private String degreeInstitution;
    private String degreeUniversity;
    private Integer degreePassingYear;
    private Integer internshipYear;
    private String pgDegree;
    private String pgInstitution;
    private Integer pgPassingYear;

    // ── Specialization & Experience ────────────────────────────────────────────
    private String superSpecialization;
    private String areasOfExpertise;
    private Integer totalExperienceYears;

    // ── Current Practice ───────────────────────────────────────────────────────
    private String currentHospitalName;
    private String currentHospitalAddress;
    private String clinicName;
    private String clinicAddress;
    private String clinicCity;
    private String clinicState;
    private String clinicPincode;

    // ── Availability ───────────────────────────────────────────────────────────
    private String languagesSpoken;
    private String consultationDays;
    private String consultationTimings;

    // ── About ─────────────────────────────────────────────────────────────────
    @Column(columnDefinition = "TEXT")
    private String aboutMe;

    // ── Uploaded Document Filenames ────────────────────────────────────────────
    private String medicalCertificateFile;
    private String degreeFile;
    private String pgDegreeFile;
    private String idProofFile;
    private String photoFile;

    // ── Verification Status ────────────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus status = VerificationStatus.INCOMPLETE;

    private String adminRemarks;
    private LocalDate submittedAt;
    private LocalDate reviewedAt;

    public enum VerificationStatus {
        INCOMPLETE, PENDING, APPROVED, REJECTED
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public String getFullLegalName() { return fullLegalName; }
    public void setFullLegalName(String v) { this.fullLegalName = v; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate v) { this.dateOfBirth = v; }

    public String getGender() { return gender; }
    public void setGender(String v) { this.gender = v; }

    public String getNationality() { return nationality; }
    public void setNationality(String v) { this.nationality = v; }

    public String getAadhaarNumber() { return aadhaarNumber; }
    public void setAadhaarNumber(String v) { this.aadhaarNumber = v; }

    public String getAadhaarMobile() { return aadhaarMobile; }
    public void setAadhaarMobile(String v) { this.aadhaarMobile = v; }

    public String getAadhaarFile() { return aadhaarFile; }
    public void setAadhaarFile(String v) { this.aadhaarFile = v; }

    public String getMedicalRegistrationNumber() { return medicalRegistrationNumber; }
    public void setMedicalRegistrationNumber(String v) { this.medicalRegistrationNumber = v; }

    public String getMedicalCouncil() { return medicalCouncil; }
    public void setMedicalCouncil(String v) { this.medicalCouncil = v; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate v) { this.registrationDate = v; }

    public LocalDate getRegistrationExpiry() { return registrationExpiry; }
    public void setRegistrationExpiry(LocalDate v) { this.registrationExpiry = v; }

    public String getRegistrationType() { return registrationType; }
    public void setRegistrationType(String v) { this.registrationType = v; }

    public String getIdProofType() { return idProofType; }
    public void setIdProofType(String v) { this.idProofType = v; }

    public String getIdProofNumber() { return idProofNumber; }
    public void setIdProofNumber(String v) { this.idProofNumber = v; }

    public String getDegreeName() { return degreeName; }
    public void setDegreeName(String v) { this.degreeName = v; }

    public String getDegreeInstitution() { return degreeInstitution; }
    public void setDegreeInstitution(String v) { this.degreeInstitution = v; }

    public String getDegreeUniversity() { return degreeUniversity; }
    public void setDegreeUniversity(String v) { this.degreeUniversity = v; }

    public Integer getDegreePassingYear() { return degreePassingYear; }
    public void setDegreePassingYear(Integer v) { this.degreePassingYear = v; }

    public Integer getInternshipYear() { return internshipYear; }
    public void setInternshipYear(Integer v) { this.internshipYear = v; }

    public String getPgDegree() { return pgDegree; }
    public void setPgDegree(String v) { this.pgDegree = v; }

    public String getPgInstitution() { return pgInstitution; }
    public void setPgInstitution(String v) { this.pgInstitution = v; }

    public Integer getPgPassingYear() { return pgPassingYear; }
    public void setPgPassingYear(Integer v) { this.pgPassingYear = v; }

    public String getSuperSpecialization() { return superSpecialization; }
    public void setSuperSpecialization(String v) { this.superSpecialization = v; }

    public String getAreasOfExpertise() { return areasOfExpertise; }
    public void setAreasOfExpertise(String v) { this.areasOfExpertise = v; }

    public Integer getTotalExperienceYears() { return totalExperienceYears; }
    public void setTotalExperienceYears(Integer v) { this.totalExperienceYears = v; }

    public String getCurrentHospitalName() { return currentHospitalName; }
    public void setCurrentHospitalName(String v) { this.currentHospitalName = v; }

    public String getCurrentHospitalAddress() { return currentHospitalAddress; }
    public void setCurrentHospitalAddress(String v) { this.currentHospitalAddress = v; }

    public String getClinicName() { return clinicName; }
    public void setClinicName(String v) { this.clinicName = v; }

    public String getClinicAddress() { return clinicAddress; }
    public void setClinicAddress(String v) { this.clinicAddress = v; }

    public String getClinicCity() { return clinicCity; }
    public void setClinicCity(String v) { this.clinicCity = v; }

    public String getClinicState() { return clinicState; }
    public void setClinicState(String v) { this.clinicState = v; }

    public String getClinicPincode() { return clinicPincode; }
    public void setClinicPincode(String v) { this.clinicPincode = v; }

    public String getLanguagesSpoken() { return languagesSpoken; }
    public void setLanguagesSpoken(String v) { this.languagesSpoken = v; }

    public String getConsultationDays() { return consultationDays; }
    public void setConsultationDays(String v) { this.consultationDays = v; }

    public String getConsultationTimings() { return consultationTimings; }
    public void setConsultationTimings(String v) { this.consultationTimings = v; }

    public String getAboutMe() { return aboutMe; }
    public void setAboutMe(String v) { this.aboutMe = v; }

    public String getMedicalCertificateFile() { return medicalCertificateFile; }
    public void setMedicalCertificateFile(String v) { this.medicalCertificateFile = v; }

    public String getDegreeFile() { return degreeFile; }
    public void setDegreeFile(String v) { this.degreeFile = v; }

    public String getPgDegreeFile() { return pgDegreeFile; }
    public void setPgDegreeFile(String v) { this.pgDegreeFile = v; }

    public String getIdProofFile() { return idProofFile; }
    public void setIdProofFile(String v) { this.idProofFile = v; }

    public String getPhotoFile() { return photoFile; }
    public void setPhotoFile(String v) { this.photoFile = v; }

    public VerificationStatus getStatus() { return status; }
    public void setStatus(VerificationStatus v) { this.status = v; }

    public String getAdminRemarks() { return adminRemarks; }
    public void setAdminRemarks(String v) { this.adminRemarks = v; }

    public LocalDate getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDate v) { this.submittedAt = v; }

    public LocalDate getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDate v) { this.reviewedAt = v; }
}