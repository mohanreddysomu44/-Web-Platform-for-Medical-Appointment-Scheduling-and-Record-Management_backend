package com.medvalt.medvalt.service;

import com.medvalt.medvalt.entity.Appointment;
import com.medvalt.medvalt.entity.PaymentRecord;
import com.medvalt.medvalt.repository.AppointmentRepository;
import com.medvalt.medvalt.repository.PaymentRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRecordRepository paymentRecordRepository;
    private final AppointmentRepository appointmentRepository;

    public PaymentService(PaymentRecordRepository paymentRecordRepository,
                          AppointmentRepository appointmentRepository) {
        this.paymentRecordRepository = paymentRecordRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // Create a dummy Razorpay order (no real SDK needed — returns a fake orderId)
    public PaymentRecord createOrder(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + appointmentId));

        double fee = appointment.getDoctor() != null
                ? appointment.getDoctor().getConsultationFee()
                : 0.0;

        // Check if already exists
        Optional<PaymentRecord> existing = paymentRecordRepository.findByAppointmentId(appointmentId);
        if (existing.isPresent()) return existing.get();

        PaymentRecord record = new PaymentRecord();
        record.setAppointment(appointment);
        record.setAmount(fee);
        record.setStatus("PENDING");
        // Dummy Razorpay order ID (simulates what real SDK returns)
        record.setRazorpayOrderId("order_" + UUID.randomUUID().toString().replace("-","").substring(0,16));
        return paymentRecordRepository.save(record);
    }

    // Called after frontend Razorpay success callback
    public PaymentRecord confirmPayment(Long appointmentId, String razorpayPaymentId) {
        PaymentRecord record = paymentRecordRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Payment record not found"));
        record.setStatus("SUCCESS");
        record.setRazorpayPaymentId(razorpayPaymentId);
        record.setPaidAt(LocalDateTime.now());
        return paymentRecordRepository.save(record);
    }

    public Optional<PaymentRecord> getByAppointmentId(Long appointmentId) {
        return paymentRecordRepository.findByAppointmentId(appointmentId);
    }

    public List<PaymentRecord> getForPatient(Long patientId) {
        return paymentRecordRepository.findByAppointmentPatientId(patientId);
    }
}