package com.medvalt.medvalt.controller;

import com.medvalt.medvalt.entity.PaymentRecord;
import com.medvalt.medvalt.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")

public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    // Create payment order (called when patient clicks Pay)
    // POST /payments/create-order/{appointmentId}
//    @PostMapping("/create-order/{appointmentId}")
//    public PaymentRecord createOrder(@PathVariable Long appointmentId) {
//        return service.createOrder(appointmentId);
//    }

    @PostMapping("/create-order/{appointmentId}")
    public ResponseEntity<?> createOrder(@PathVariable Long appointmentId) {
        try {
            return ResponseEntity.ok(service.createOrder(appointmentId));
        } catch (Exception e) {
            e.printStackTrace(); // VERY IMPORTANT
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // Confirm payment success (called after Razorpay success)
    // POST /payments/confirm/{appointmentId}
    // Body: { "razorpayPaymentId": "pay_xxxx" }
    @PostMapping("/confirm/{appointmentId}")
    public PaymentRecord confirmPayment(@PathVariable Long appointmentId,
                                        @RequestBody Map<String, String> body) {
        String paymentId = body.getOrDefault("razorpayPaymentId", "pay_dummy_" + System.currentTimeMillis());
        return service.confirmPayment(appointmentId, paymentId);
    }

    // Get payment status for an appointment
    // GET /payments/appointment/{appointmentId}
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PaymentRecord> getByAppointment(@PathVariable Long appointmentId) {
        return service.getByAppointmentId(appointmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all payment records for a patient
    // GET /payments/patient/{patientId}
    @GetMapping("/patient/{patientId}")
    public List<PaymentRecord> getForPatient(@PathVariable Long patientId) {
        return service.getForPatient(patientId);
    }
}