package com.medvalt.medvalt.controller;

import com.medvalt.medvalt.entity.Prescription;
import com.medvalt.medvalt.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService service;

    public PrescriptionController(PrescriptionService service) {
        this.service = service;
    }

    @PostMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> savePrescription(
            @PathVariable Long appointmentId,
            @RequestBody Prescription prescription) {
        try {
            Prescription saved = service.savePrescription(appointmentId, prescription);
            // ✅ Return only safe scalar fields — no nested entities, no recursion risk
            return ResponseEntity.ok(Map.of(
                    "id",            saved.getId(),
                    "diagnosis",     saved.getDiagnosis() != null ? saved.getDiagnosis() : "",
                    "medicines",     saved.getMedicines() != null ? saved.getMedicines() : "",
                    "instructions",  saved.getInstructions() != null ? saved.getInstructions() : "",
                    "tests",         saved.getTests() != null ? saved.getTests() : "",
                    "followUpDate",  saved.getFollowUpDate() != null ? saved.getFollowUpDate() : "",
                    "issuedDate",    saved.getIssuedDate() != null ? saved.getIssuedDate().toString() : ""
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<Prescription> getByAppointment(@PathVariable Long appointmentId) {
        return service.getByAppointmentId(appointmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public List<Prescription> getForPatient(@PathVariable Long patientId) {
        return service.getForPatient(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Prescription> getForDoctor(@PathVariable Long doctorId) {
        return service.getForDoctor(doctorId);
    }
}
