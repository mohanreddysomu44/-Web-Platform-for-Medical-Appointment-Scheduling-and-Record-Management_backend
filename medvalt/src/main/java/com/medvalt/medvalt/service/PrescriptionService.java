package com.medvalt.medvalt.service;

import com.medvalt.medvalt.entity.Appointment;
import com.medvalt.medvalt.entity.Prescription;
import com.medvalt.medvalt.repository.AppointmentRepository;
import com.medvalt.medvalt.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                               AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Prescription savePrescription(Long appointmentId, Prescription incoming) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + appointmentId));

        // ✅ Find existing or create new — prevents duplicate key crash
        Prescription prescription = prescriptionRepository
                .findByAppointmentId(appointmentId)
                .orElse(new Prescription());

        prescription.setAppointment(appointment);
        prescription.setDiagnosis(incoming.getDiagnosis());
        prescription.setMedicines(incoming.getMedicines());
        prescription.setInstructions(incoming.getInstructions());
        prescription.setTests(incoming.getTests());
        prescription.setFollowUpDate(incoming.getFollowUpDate());

        if (prescription.getIssuedDate() == null) {
            prescription.setIssuedDate(LocalDate.now());
        }

        return prescriptionRepository.save(prescription);
    }

    public Optional<Prescription> getByAppointmentId(Long appointmentId) {
        return prescriptionRepository.findByAppointmentId(appointmentId);
    }

    public List<Prescription> getForPatient(Long patientId) {
        return prescriptionRepository.findByAppointmentPatientId(patientId);
    }

    public List<Prescription> getForDoctor(Long doctorId) {
        return prescriptionRepository.findByAppointmentDoctorId(doctorId);
    }
}