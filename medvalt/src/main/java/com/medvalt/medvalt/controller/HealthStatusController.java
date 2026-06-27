package com.medvalt.medvalt.controller;

import com.medvalt.medvalt.entity.HealthStatus;
import com.medvalt.medvalt.service.HealthStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/health-status")
public class HealthStatusController {

    private final HealthStatusService service;

    public HealthStatusController(HealthStatusService service) {
        this.service = service;
    }

    // GET all history for patient
    @GetMapping("/patient/{patientId}")
    public List<HealthStatus> getHistory(@PathVariable Long patientId) {
        return service.getHistory(patientId);
    }

    // GET latest record
    @GetMapping("/patient/{patientId}/latest")
    public ResponseEntity<HealthStatus> getLatest(@PathVariable Long patientId) {
        return service.getLatest(patientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST new entry
    @PostMapping("/patient/{patientId}")
    public HealthStatus save(@PathVariable Long patientId,
                             @RequestBody HealthStatus healthStatus) {
        return service.save(patientId, healthStatus);
    }

    // PUT update entry
    @PutMapping("/{id}")
    public HealthStatus update(@PathVariable Long id,
                               @RequestBody HealthStatus healthStatus) {
        return service.update(id, healthStatus);
    }

    // DELETE entry
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}