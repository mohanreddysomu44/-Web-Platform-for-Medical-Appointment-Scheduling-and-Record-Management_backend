package com.medvalt.medvalt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_status")
public class HealthStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"healthStatuses", "user", "appointments", "hibernateLazyInitializer", "handler"})
    private Patient patient;

    // Blood Sugar
    private Double bloodSugarLevel;
    private String sugarType; // fasting, post-meal, random

    // Vitals
    private Double bodyTemperature;
    private String bloodPressure; // e.g. "120/80"
    private Integer heartRate;
    private Double oxygenSaturation;
    private Integer respiratoryRate;

    // Body Metrics
    private Double height; // cm
    private Double weight; // kg
    private Double bmi;

    private LocalDateTime recordedAt;

    @PrePersist
    public void prePersist() {
        this.recordedAt = LocalDateTime.now();
        calculateBMI();
    }

    @PreUpdate
    public void preUpdate() {
        calculateBMI();
    }

    private void calculateBMI() {
        if (height != null && weight != null && height > 0) {
            double heightInMeters = height / 100.0;
            this.bmi = Math.round((weight / (heightInMeters * heightInMeters)) * 10.0) / 10.0;
        }
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Double getBloodSugarLevel() { return bloodSugarLevel; }
    public void setBloodSugarLevel(Double bloodSugarLevel) { this.bloodSugarLevel = bloodSugarLevel; }
    public String getSugarType() { return sugarType; }
    public void setSugarType(String sugarType) { this.sugarType = sugarType; }
    public Double getBodyTemperature() { return bodyTemperature; }
    public void setBodyTemperature(Double bodyTemperature) { this.bodyTemperature = bodyTemperature; }
    public String getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }
    public Integer getHeartRate() { return heartRate; }
    public void setHeartRate(Integer heartRate) { this.heartRate = heartRate; }
    public Double getOxygenSaturation() { return oxygenSaturation; }
    public void setOxygenSaturation(Double oxygenSaturation) { this.oxygenSaturation = oxygenSaturation; }
    public Integer getRespiratoryRate() { return respiratoryRate; }
    public void setRespiratoryRate(Integer respiratoryRate) { this.respiratoryRate = respiratoryRate; }
    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public Double getBmi() { return bmi; }
    public void setBmi(Double bmi) { this.bmi = bmi; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}