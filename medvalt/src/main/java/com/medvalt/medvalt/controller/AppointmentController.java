////package com.medvalt.medvalt.controller;
////
////import com.medvalt.medvalt.entity.Appointment;
////import com.medvalt.medvalt.service.AppointmentService;
////import org.springframework.web.bind.annotation.*;
////import org.springframework.web.multipart.MultipartFile;
////
////import java.io.IOException;
////import java.util.List;
////
////@RestController
////@RequestMapping("/appointments")
////public class AppointmentController {
////
////    private final AppointmentService service;
////
////    public AppointmentController(AppointmentService service) {
////        this.service = service;
////    }
////
////    // 🧑 Patient books appointment with description & report
////    @PostMapping("/book")
////    public Appointment bookAppointment(
////            @RequestParam Long patientId,
////            @RequestParam Long doctorId,
////            @RequestParam String description,
////            @RequestParam String date,
////            @RequestParam String timeSlot,
////            @RequestParam(required = false) MultipartFile report
////    ) throws IOException {
////        return service.bookWithReport(patientId, doctorId, description, date, timeSlot, report);
////    }
////
////    // 👨‍⚕️ Doctor approves
////    @PutMapping("/{id}/approve")
////    public Appointment approve(@PathVariable Long id) {
////        return service.approve(id);
////    }
////
////    // 👨‍⚕️ Doctor rejects
////    @PutMapping("/{id}/reject")
////    public Appointment reject(@PathVariable Long id) {
////        return service.reject(id);
////    }
////
////    // Doctor view appointments
////    @GetMapping("/doctor/{doctorId}")
////    public List<Appointment> doctorAppointments(@PathVariable Long doctorId) {
////        return service.getDoctorAppointments(doctorId);
////    }
////
////    // Patient view appointments
////    @GetMapping("/patient/{patientId}")
////    public List<Appointment> patientAppointments(@PathVariable Long patientId) {
////        return service.getPatientAppointments(patientId);
////    }
////}
//
//package com.medvalt.medvalt.controller;
//
//import com.medvalt.medvalt.entity.Appointment;
//import com.medvalt.medvalt.service.AppointmentService;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
//@RestController
//@RequestMapping("/appointments")
//public class AppointmentController {
//
//    private final AppointmentService service;
//
//    public AppointmentController(AppointmentService service) {
//        this.service = service;
//    }
//
//    // 🧑 Patient books appointment with description & report (UNCHANGED)
//    @PostMapping("/book")
//    public Appointment bookAppointment(
//            @RequestParam Long patientId,
//            @RequestParam Long doctorId,
//            @RequestParam String description,
//            @RequestParam String date,
//            @RequestParam String timeSlot,
//            @RequestParam(required = false) MultipartFile report
//    ) throws IOException {
//        return service.bookWithReport(patientId, doctorId, description, date, timeSlot, report);
//    }
//
//    // 👨‍⚕️ Doctor approves (UNCHANGED)
//    @PutMapping("/{id}/approve")
//    public Appointment approve(@PathVariable Long id) {
//        return service.approve(id);
//    }
//
//    // 👨‍⚕️ Doctor rejects (UNCHANGED)
//    @PutMapping("/{id}/reject")
//    public Appointment reject(@PathVariable Long id) {
//        return service.reject(id);
//    }
//
//    // Doctor view appointments (UNCHANGED)
//    @GetMapping("/doctor/{doctorId}")
//    public List<Appointment> doctorAppointments(@PathVariable Long doctorId) {
//        return service.getDoctorAppointments(doctorId);
//    }
//
//    // Patient view appointments (UNCHANGED)
//    @GetMapping("/patient/{patientId}")
//    public List<Appointment> patientAppointments(@PathVariable Long patientId) {
//        return service.getPatientAppointments(patientId);
//    }
//
//    /**
//     * ✅ NEW — Serve a report file by filename.
//     *
//     * Frontend calls:  GET http://localhost:8080/appointments/report/1234567890_myfile.pdf
//     * This looks up the file in the "uploads/" folder and streams it back.
//     *
//     * Supports inline preview for PDF and images (Content-Disposition: inline)
//     * and forces download for other file types.
//     */
//    @GetMapping("/report/{filename:.+}")
//    public ResponseEntity<Resource> serveReport(@PathVariable String filename) {
//        try {
//            // Resolve file from the "uploads" directory (same folder AppointmentService writes to)
//            Path filePath = Paths.get("uploads").resolve(filename).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//
//            if (!resource.exists() || !resource.isReadable()) {
//                return ResponseEntity.notFound().build();
//            }
//
//            // Detect MIME type from extension for correct browser rendering
//            String contentType = detectContentType(filename);
//
//            // Use "inline" so PDF/images open in the browser; other types trigger download
//            String disposition = isInlineType(filename) ? "inline" : "attachment";
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .header(HttpHeaders.CONTENT_DISPOSITION,
//                            disposition + "; filename=\"" + resource.getFilename() + "\"")
//                    // Allow React dev server to fetch this (CORS for localhost:5173)
//                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
//                    .body(resource);
//
//        } catch (MalformedURLException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    // ── helpers ──────────────────────────────────────────────────────────────
//
//    private String detectContentType(String filename) {
//        String lower = filename.toLowerCase();
//        if (lower.endsWith(".pdf"))                       return "application/pdf";
//        if (lower.endsWith(".png"))                       return "image/png";
//        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
//        if (lower.endsWith(".gif"))                       return "image/gif";
//        if (lower.endsWith(".webp"))                      return "image/webp";
//        if (lower.endsWith(".doc"))                       return "application/msword";
//        if (lower.endsWith(".docx"))                      return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
//        return "application/octet-stream";               // force download for unknown types
//    }
//
//    private boolean isInlineType(String filename) {
//        String lower = filename.toLowerCase();
//        return lower.endsWith(".pdf")
//                || lower.endsWith(".png")
//                || lower.endsWith(".jpg")
//                || lower.endsWith(".jpeg")
//                || lower.endsWith(".gif")
//                || lower.endsWith(".webp");
//    }
//}


package com.medvalt.medvalt.controller;

import com.medvalt.medvalt.entity.Appointment;
import com.medvalt.medvalt.service.AppointmentService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    // ── Patient books appointment (NO file upload at booking) ─────────────────
    // POST /appointments/book
    // Params: patientId, doctorId, description, date, timeSlot
    @PostMapping("/book")
    public Appointment bookAppointment(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam String description,
            @RequestParam String date,
            @RequestParam String timeSlot
    ) {
        return service.bookAppointment(patientId, doctorId, description, date, timeSlot);
    }

    // ── Doctor approves ───────────────────────────────────────────────────────
    @PutMapping("/{id}/approve")
    public Appointment approve(@PathVariable Long id) {
        return service.approve(id);
    }

    // ── Doctor rejects ────────────────────────────────────────────────────────
    @PutMapping("/{id}/reject")
    public Appointment reject(@PathVariable Long id) {
        return service.reject(id);
    }

    // ── Doctor requests patient to upload health documents ────────────────────
    // PUT /appointments/{id}/request-docs
    // Body: { "message": "Please upload your latest blood report" }
    @PutMapping("/{id}/request-docs")
    public Appointment requestDocs(@PathVariable Long id,
                                   @RequestBody(required = false) Map<String, String> body) {
        String message = body != null ? body.getOrDefault("message", "") : "";
        return service.requestDocuments(id, message);
    }

    // ── Patient uploads document after doctor requests ────────────────────────
    // POST /appointments/{id}/upload-doc
    // Params: file (multipart)
    @PostMapping("/{id}/upload-doc")
    public Appointment uploadDoc(@PathVariable Long id,
                                 @RequestParam("file") MultipartFile file) throws IOException {
        return service.uploadRequestedDocument(id, file);
    }

    // ── List appointments ─────────────────────────────────────────────────────
    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> doctorAppointments(@PathVariable Long doctorId) {
        return service.getDoctorAppointments(doctorId);
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> patientAppointments(@PathVariable Long patientId) {
        return service.getPatientAppointments(patientId);
    }

    // ── Serve uploaded appointment docs (doctor-requested) ────────────────────
    // GET /appointments/doc/{filename}
    @GetMapping("/doc/{filename:.+}")
    public ResponseEntity<Resource> serveDoc(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("appointment-docs").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }
            String contentType = detectContentType(filename);
            String disposition = isInlineType(filename) ? "inline" : "attachment";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            disposition + "; filename=\"" + resource.getFilename() + "\"")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ── Serve old appointment reports (backward compat) ───────────────────────
    @GetMapping("/report/{filename:.+}")
    public ResponseEntity<Resource> serveReport(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }
            String contentType = detectContentType(filename);
            String disposition = isInlineType(filename) ? "inline" : "attachment";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            disposition + "; filename=\"" + resource.getFilename() + "\"")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private String detectContentType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".pdf"))  return "application/pdf";
        if (lower.endsWith(".png"))  return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif"))  return "image/gif";
        if (lower.endsWith(".webp")) return "image/webp";
        if (lower.endsWith(".doc"))  return "application/msword";
        if (lower.endsWith(".docx")) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        return "application/octet-stream";
    }

    private boolean isInlineType(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".pdf") || lower.endsWith(".png")
                || lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".gif") || lower.endsWith(".webp");
    }
}