package com.medvalt.medvalt.service;

import com.medvalt.medvalt.entity.Doctor;
import com.medvalt.medvalt.entity.DoctorVerification;
import com.medvalt.medvalt.entity.DoctorVerification.VerificationStatus;
import com.medvalt.medvalt.repository.DoctorRepository;
import com.medvalt.medvalt.repository.DoctorVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class DoctorVerificationService {

    @Autowired
    private DoctorVerificationRepository verificationRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Value("${file.upload-dir:uploads/verification}")
    private String uploadDir;

    // ── Get by doctor ID ──────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public Optional<DoctorVerification> getByDoctorId(Long doctorId) {
        return verificationRepository.findByDoctorId(doctorId);
    }

    // ── Save / update all text fields (upsert) ────────────────────────────────
    @Transactional
    public DoctorVerification saveVerification(Long doctorId, DoctorVerification data) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));

        DoctorVerification dv = verificationRepository.findByDoctorId(doctorId)
                .orElse(new DoctorVerification());

        dv.setDoctor(doctor);

        // Personal identity
        dv.setFullLegalName(data.getFullLegalName());
        dv.setDateOfBirth(data.getDateOfBirth());
        dv.setGender(data.getGender());
        dv.setNationality(data.getNationality());

        // Aadhaar
        dv.setAadhaarNumber(data.getAadhaarNumber());
        dv.setAadhaarMobile(data.getAadhaarMobile());

        // NMC Registration
        dv.setMedicalRegistrationNumber(data.getMedicalRegistrationNumber());
        dv.setMedicalCouncil(data.getMedicalCouncil());
        dv.setRegistrationDate(data.getRegistrationDate());
        dv.setRegistrationExpiry(data.getRegistrationExpiry());
        dv.setRegistrationType(data.getRegistrationType());

        // Secondary ID
        dv.setIdProofType(data.getIdProofType());
        dv.setIdProofNumber(data.getIdProofNumber());

        // Education
        dv.setDegreeName(data.getDegreeName());
        dv.setDegreeInstitution(data.getDegreeInstitution());
        dv.setDegreeUniversity(data.getDegreeUniversity());
        dv.setDegreePassingYear(data.getDegreePassingYear());
        dv.setInternshipYear(data.getInternshipYear());
        dv.setPgDegree(data.getPgDegree());
        dv.setPgInstitution(data.getPgInstitution());
        dv.setPgPassingYear(data.getPgPassingYear());

        // Specialization
        dv.setSuperSpecialization(data.getSuperSpecialization());
        dv.setAreasOfExpertise(data.getAreasOfExpertise());
        dv.setTotalExperienceYears(data.getTotalExperienceYears());

        // Practice
        dv.setCurrentHospitalName(data.getCurrentHospitalName());
        dv.setCurrentHospitalAddress(data.getCurrentHospitalAddress());
        dv.setClinicName(data.getClinicName());
        dv.setClinicAddress(data.getClinicAddress());
        dv.setClinicCity(data.getClinicCity());
        dv.setClinicState(data.getClinicState());
        dv.setClinicPincode(data.getClinicPincode());

        // Availability
        dv.setLanguagesSpoken(data.getLanguagesSpoken());
        dv.setConsultationDays(data.getConsultationDays());
        dv.setConsultationTimings(data.getConsultationTimings());

        // About
        dv.setAboutMe(data.getAboutMe());

        // Only initialise status if brand new (don't downgrade PENDING/APPROVED)
        if (dv.getStatus() == null) {
            dv.setStatus(VerificationStatus.INCOMPLETE);
        }

        return verificationRepository.save(dv);
    }

    // ── Doctor submits for admin review ───────────────────────────────────────
    @Transactional
    public DoctorVerification submitForReview(Long doctorId) {
        DoctorVerification dv = verificationRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new RuntimeException("No verification record for doctor " + doctorId));
        dv.setStatus(VerificationStatus.PENDING);
        dv.setSubmittedAt(LocalDate.now());
        return verificationRepository.save(dv);
    }

    // ── Admin approves ────────────────────────────────────────────────────────
    @Transactional
    public DoctorVerification approve(Long doctorId, String remarks) {
        DoctorVerification dv = verificationRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new RuntimeException("Verification not found for doctor " + doctorId));
        dv.setStatus(VerificationStatus.APPROVED);
        dv.setAdminRemarks(remarks);
        dv.setReviewedAt(LocalDate.now());
        return verificationRepository.save(dv);
    }

    // ── Admin rejects ─────────────────────────────────────────────────────────
    @Transactional
    public DoctorVerification reject(Long doctorId, String remarks) {
        DoctorVerification dv = verificationRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new RuntimeException("Verification not found for doctor " + doctorId));
        dv.setStatus(VerificationStatus.REJECTED);
        dv.setAdminRemarks(remarks);
        dv.setReviewedAt(LocalDate.now());
        return verificationRepository.save(dv);
    }

    // ── Upload a document file, returns saved filename ────────────────────────
    public String uploadDocument(MultipartFile file) throws IOException {
        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);
        String ext = "";
        String orig = file.getOriginalFilename();
        if (orig != null && orig.contains(".")) {
            ext = orig.substring(orig.lastIndexOf('.'));
        }
        String filename = UUID.randomUUID().toString() + ext;
        Files.copy(file.getInputStream(), dir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }

    // ── Save uploaded filename to the correct field ───────────────────────────
    @Transactional
    public DoctorVerification saveDocumentField(Long doctorId, String field, String filename) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));

        DoctorVerification dv = verificationRepository.findByDoctorId(doctorId)
                .orElse(new DoctorVerification());
        dv.setDoctor(doctor);
        if (dv.getStatus() == null) {
            dv.setStatus(VerificationStatus.INCOMPLETE);
        }

        switch (field) {
            case "aadhaarFile"           -> dv.setAadhaarFile(filename);
            case "medicalCertificateFile"-> dv.setMedicalCertificateFile(filename);
            case "degreeFile"            -> dv.setDegreeFile(filename);
            case "pgDegreeFile"          -> dv.setPgDegreeFile(filename);
            case "idProofFile"           -> dv.setIdProofFile(filename);
            case "photoFile"             -> dv.setPhotoFile(filename);
            default -> throw new RuntimeException("Unknown document field: " + field);
        }

        return verificationRepository.save(dv);
    }
}