package com.medvalt.medvalt.repository;

import com.medvalt.medvalt.entity.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    Optional<PaymentRecord> findByAppointmentId(Long appointmentId);
    List<PaymentRecord> findByAppointmentPatientId(Long patientId);
}