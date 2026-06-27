//package com.medvalt.medvalt.service;
//
//import com.medvalt.medvalt.entity.*;
//import com.medvalt.medvalt.repository.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.*;
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//public class AppointmentService {
//
//    private final AppointmentRepository appointmentRepository;
//    private final PatientRepository patientRepository;
//    private final DoctorRepository doctorRepository;
//
//    public AppointmentService(AppointmentRepository appointmentRepository,
//                              PatientRepository patientRepository,
//                              DoctorRepository doctorRepository) {
//        this.appointmentRepository = appointmentRepository;
//        this.patientRepository = patientRepository;
//        this.doctorRepository = doctorRepository;
//    }
//
//    // ✅ Booking with description & report
//    public Appointment bookWithReport(Long patientId, Long doctorId,
//                                      String description, String date,
//                                      String timeSlot, MultipartFile report) throws IOException {
//
//        Patient patient = patientRepository.findById(patientId).orElseThrow();
//        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
//
//        Appointment appointment = new Appointment();
//        appointment.setPatient(patient);
//        appointment.setDoctor(doctor);
//        appointment.setDescription(description);
//        appointment.setDate(LocalDate.parse(date));
//        appointment.setTimeSlot(timeSlot);
//        appointment.setStatus("PENDING");
//
//        // ✅ Save file
//        if (report != null && !report.isEmpty()) {
//            String fileName = System.currentTimeMillis() + "_" + report.getOriginalFilename();
//            Path path = Paths.get("uploads/" + fileName);
//            Files.createDirectories(path.getParent());
//            Files.write(path, report.getBytes());
//
//            appointment.setReportFilePath(path.toString());
//        }
//
//        return appointmentRepository.save(appointment);
//    }
//
//    public Appointment approve(Long id) {
//        Appointment a = appointmentRepository.findById(id).orElseThrow();
//        a.setStatus("APPROVED");
//        return appointmentRepository.save(a);
//    }
//
//    public Appointment reject(Long id) {
//        Appointment a = appointmentRepository.findById(id).orElseThrow();
//        a.setStatus("REJECTED");
//        return appointmentRepository.save(a);
//    }
//
//    public List<Appointment> getDoctorAppointments(Long doctorId) {
//        return appointmentRepository.findByDoctorId(doctorId);
//    }
//
//    public List<Appointment> getPatientAppointments(Long patientId) {
//        return appointmentRepository.findByPatientId(patientId);
//    }
//}


package com.medvalt.medvalt.service;

import com.medvalt.medvalt.entity.*;
import com.medvalt.medvalt.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    // ── Patient books appointment — NO document upload at booking time ────────
    public Appointment bookAppointment(Long patientId, Long doctorId,
                                       String description, String date, String timeSlot) {
        Patient patient = patientRepository.findById(patientId).orElseThrow();
        Doctor doctor   = doctorRepository.findById(doctorId).orElseThrow();

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDescription(description);
        appointment.setDate(LocalDate.parse(date));
        appointment.setTimeSlot(timeSlot);
        appointment.setStatus("PENDING");
        appointment.setDocRequestStatus("DOC_REQUEST_NONE");

        return appointmentRepository.save(appointment);
    }

    // ── Keep old method for backward compat (ignores report param now) ────────
    public Appointment bookWithReport(Long patientId, Long doctorId,
                                      String description, String date,
                                      String timeSlot, MultipartFile report) throws IOException {
        // Report upload at booking is disabled — just book without it
        return bookAppointment(patientId, doctorId, description, date, timeSlot);
    }

    // ── Doctor approves ───────────────────────────────────────────────────────
    public Appointment approve(Long id) {
        Appointment a = appointmentRepository.findById(id).orElseThrow();
        a.setStatus("APPROVED");
        return appointmentRepository.save(a);
    }

    // ── Doctor rejects ────────────────────────────────────────────────────────
    public Appointment reject(Long id) {
        Appointment a = appointmentRepository.findById(id).orElseThrow();
        a.setStatus("REJECTED");
        return appointmentRepository.save(a);
    }

    // ── Doctor requests documents from patient ────────────────────────────────
    public Appointment requestDocuments(Long appointmentId, String message) {
        Appointment a = appointmentRepository.findById(appointmentId).orElseThrow();
        a.setDocRequestStatus("DOC_REQUEST_PENDING");
        a.setDocRequestMessage(message);
        return appointmentRepository.save(a);
    }

    // ── Patient uploads document after doctor requests ────────────────────────
    public Appointment uploadRequestedDocument(Long appointmentId, MultipartFile file) throws IOException {
        Appointment a = appointmentRepository.findById(appointmentId).orElseThrow();

        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get("appointment-docs/" + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            a.setDocFileName(fileName);
        }

        a.setDocRequestStatus("DOC_REQUEST_DONE");
        return appointmentRepository.save(a);
    }

    // ── Queries ───────────────────────────────────────────────────────────────
    public List<Appointment> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
}