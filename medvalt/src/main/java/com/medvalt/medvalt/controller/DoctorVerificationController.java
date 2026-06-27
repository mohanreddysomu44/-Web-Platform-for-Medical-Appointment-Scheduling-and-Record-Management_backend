package com.medvalt.medvalt.controller;

import com.medvalt.medvalt.entity.DoctorVerification;
import com.medvalt.medvalt.service.DoctorVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Map;

@RestController
@RequestMapping("/doctor-verification")
public class DoctorVerificationController {

    @Autowired
    private DoctorVerificationService service;

    @Value("${file.upload-dir:uploads/verification}")
    private String uploadDir;

    // ── GET verification record for a doctor ─────────────────────────────────
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getByDoctor(@PathVariable Long doctorId) {
        return service.getByDoctorId(doctorId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── POST save / update text fields ────────────────────────────────────────
    @PostMapping("/doctor/{doctorId}")
    public ResponseEntity<DoctorVerification> save(
            @PathVariable Long doctorId,
            @RequestBody DoctorVerification data) {
        return ResponseEntity.ok(service.saveVerification(doctorId, data));
    }

    // ── POST submit for admin review ──────────────────────────────────────────
    @PostMapping("/doctor/{doctorId}/submit")
    public ResponseEntity<DoctorVerification> submit(@PathVariable Long doctorId) {
        return ResponseEntity.ok(service.submitForReview(doctorId));
    }

    // ── PUT admin approves ────────────────────────────────────────────────────
    @PutMapping("/doctor/{doctorId}/approve")
    public ResponseEntity<DoctorVerification> approve(
            @PathVariable Long doctorId,
            @RequestBody(required = false) Map<String, String> body) {
        String remarks = (body != null) ? body.getOrDefault("remarks", "") : "";
        return ResponseEntity.ok(service.approve(doctorId, remarks));
    }

    // ── PUT admin rejects ─────────────────────────────────────────────────────
    @PutMapping("/doctor/{doctorId}/reject")
    public ResponseEntity<DoctorVerification> reject(
            @PathVariable Long doctorId,
            @RequestBody(required = false) Map<String, String> body) {
        String remarks = (body != null) ? body.getOrDefault("remarks", "") : "";
        return ResponseEntity.ok(service.reject(doctorId, remarks));
    }

    // ── POST upload a document file ───────────────────────────────────────────
    // field values: medicalCertificateFile | degreeFile | pgDegreeFile | idProofFile | photoFile
    @PostMapping("/doctor/{doctorId}/upload")
    public ResponseEntity<?> uploadDoc(
            @PathVariable Long doctorId,
            @RequestParam("field") String field,
            @RequestParam("file")  MultipartFile file) {
        try {
            String filename = service.uploadDocument(file);
            DoctorVerification dv = service.saveDocumentField(doctorId, field, filename);
            return ResponseEntity.ok(Map.of(
                    "filename", filename,
                    "field",    field,
                    "status",   dv.getStatus().name()
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

    // ── GET serve a verification document file ────────────────────────────────
    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path path = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            String lower = filename.toLowerCase();
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            if (lower.endsWith(".pdf"))                                   mediaType = MediaType.APPLICATION_PDF;
            else if (lower.endsWith(".png"))                              mediaType = MediaType.IMAGE_PNG;
            else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg"))   mediaType = MediaType.IMAGE_JPEG;

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}