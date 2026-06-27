package com.medvalt.medvalt.controller;

import com.medvalt.medvalt.entity.Doctor;
import com.medvalt.medvalt.entity.Patient;
import com.medvalt.medvalt.entity.PatientDoctorAccess;
import com.medvalt.medvalt.service.DoctorService;
import com.medvalt.medvalt.service.PatientService;
import com.medvalt.medvalt.service.PatientDoctorAccessService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final PatientDoctorAccessService accessService;

    public DoctorController(DoctorService doctorService,
                            PatientService patientService,
                            PatientDoctorAccessService accessService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.accessService = accessService;
    }

    // 🔹 Get all doctors
    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    // 🔹 Get doctor by ID
    @GetMapping("/{id}")
    public Doctor getDoctor(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    // 🔹 Create doctor profile
    @PostMapping
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        return doctorService.saveDoctor(doctor);
    }

    // 🔹 Update doctor
    @PutMapping("/{id}")
    public Doctor updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        doctor.setId(id);
        return doctorService.saveDoctor(doctor);
    }

    // 🔹 Delete doctor
    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

    // 🔹 Clear all doctors (testing)
    @DeleteMapping("/clear")
    public String clearDoctors() {
        doctorService.getAllDoctors()
                .forEach(d -> doctorService.deleteDoctor(d.getId()));
        return "All doctors deleted!";
    }

    // 🔹 Get doctor profile by userId (for dashboard)
    @GetMapping("/user/{userId}")
    public ResponseEntity<Doctor> getDoctorByUserId(@PathVariable Long userId) {
        Doctor doctor = doctorService.getDoctorByUserId(userId);
        return doctor != null
                ? ResponseEntity.ok(doctor)
                : ResponseEntity.notFound().build();
    }

    // 🔹 Patients who granted access
    @GetMapping("/{doctorId}/patients")
    public List<Patient> getPatientsForDoctor(@PathVariable Long doctorId) {
        return accessService.getPatientsForDoctor(doctorId);
    }

    // 🔹 Access list for doctor
    @GetMapping("/{doctorId}/accessList")
    public List<PatientDoctorAccess> getDoctorAccessList(@PathVariable Long doctorId) {
        return accessService.getAccessListForDoctor(doctorId);
    }

    // 🔹 Get patient records with permission check
    @GetMapping("/{doctorId}/patient/{patientId}")
    public Object getPatientRecords(@PathVariable Long doctorId,
                                    @PathVariable Long patientId) {

        boolean hasAccess = accessService.getAccessListForDoctor(doctorId)
                .stream()
                .anyMatch(a -> a.getPatient().getId().equals(patientId)
                        && a.isGranted());

        if (!hasAccess) {
            return "Access denied: Patient has not granted permission.";
        }

        return patientService.getPatientById(patientId);
    }
}