package com.medvalt.medvalt.controller;

import com.medvalt.medvalt.entity.Prescription;
import com.medvalt.medvalt.repository.PrescriptionRepository;
import com.medvalt.medvalt.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionEmailController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private EmailService emailService;

    /**
     * POST /prescriptions/appointment/{appointmentId}/notify-patient
     * Called by the frontend after a prescription is saved.
     * Sends an email to the patient with prescription details.
     */
    @PostMapping("/appointment/{appointmentId}/notify-patient")
    public ResponseEntity<?> notifyPatient(@PathVariable Long appointmentId) {
        try {
            Prescription rx = prescriptionRepository
                    .findByAppointmentId(appointmentId)
                    .orElse(null);

            if (rx == null) {
                return ResponseEntity.notFound().build();
            }

            String patientEmail = rx.getAppointment().getPatient().getEmail();
            String patientName  = rx.getAppointment().getPatient().getName();
            String doctorName   = rx.getAppointment().getDoctor().getName();

            emailService.sendPrescriptionEmail(
                    patientEmail,
                    patientName,
                    doctorName,
                    rx.getDiagnosis(),
                    rx.getMedicines(),
                    rx.getInstructions(),
                    rx.getFollowUpDate()
            );

            return ResponseEntity.ok("Email sent to " + patientEmail);
        } catch (Exception e) {
            // Don't fail the request if email fails
            return ResponseEntity.ok("Email skipped: " + e.getMessage());
        }
    }
}