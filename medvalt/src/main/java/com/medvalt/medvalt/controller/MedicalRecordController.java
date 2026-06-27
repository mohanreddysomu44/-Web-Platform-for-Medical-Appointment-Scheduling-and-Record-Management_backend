package com.medvalt.medvalt.controller;

import com.medvalt.medvalt.entity.MedicalRecord;
import com.medvalt.medvalt.service.MedicalRecordService;
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

@RestController
@RequestMapping("/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService service;

    public MedicalRecordController(MedicalRecordService service) {
        this.service = service;
    }

    // ✅ GET all records for a patient
    // Frontend: GET /medical-records/patient/{patientId}
    @GetMapping("/patient/{patientId}")
    public List<MedicalRecord> getRecords(@PathVariable Long patientId) {
        return service.getRecordsForPatient(patientId);
    }

    // ✅ GET records filtered by type
    // Frontend: GET /medical-records/patient/{patientId}?type=LAB_REPORT
    @GetMapping("/patient/{patientId}/filter")
    public List<MedicalRecord> getRecordsByType(@PathVariable Long patientId,
                                                @RequestParam String type) {
        return service.getRecordsByType(patientId, type);
    }

    // ✅ POST upload a new medical record (multipart: fields + optional file)
    // Frontend FormData: patientId, title, recordType, description, recordDate, issuedBy, file(optional)
    @PostMapping("/upload")
    public MedicalRecord uploadRecord(
            @RequestParam Long patientId,
            @RequestParam String title,
            @RequestParam String recordType,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String recordDate,
            @RequestParam(required = false) String issuedBy,
            @RequestParam(required = false) MultipartFile file
    ) throws IOException {
        return service.uploadRecord(patientId, title, recordType, description, recordDate, issuedBy, file);
    }

    // ✅ DELETE a specific record
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecord(@PathVariable Long id) {
        service.deleteRecord(id);
        return ResponseEntity.ok("Record deleted");
    }

    // ✅ Serve the file (PDF/image) — same pattern as AppointmentController
    // Frontend: GET /medical-records/file/{filename}
    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("medical-records").resolve(filename).normalize();
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

    // ── helpers ──────────────────────────────────────────────────────────────

    private String detectContentType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".pdf"))                            return "application/pdf";
        if (lower.endsWith(".png"))                            return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif"))                            return "image/gif";
        if (lower.endsWith(".webp"))                           return "image/webp";
        if (lower.endsWith(".doc"))                            return "application/msword";
        if (lower.endsWith(".docx"))                           return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        return "application/octet-stream";
    }

    private boolean isInlineType(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".pdf") || lower.endsWith(".png")
                || lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".gif") || lower.endsWith(".webp");
    }
}