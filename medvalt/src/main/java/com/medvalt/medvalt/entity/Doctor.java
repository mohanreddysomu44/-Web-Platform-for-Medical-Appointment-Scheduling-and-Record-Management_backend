//package com.medvalt.medvalt.entity;
//
//import jakarta.persistence.*;
//import com.fasterxml.jackson.annotation.JsonBackReference;
//
//@Entity
//@Table(name = "doctors")
//public class Doctor {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    // ✅ Proper relationship
//    @OneToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    @JsonBackReference
//    private User user;
//
//    private String name;
//    private String specialization;
//    private String contact;
//    private String hospital;
//
//    // --- Getters & Setters ---
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public User getUser() { return user; }
//    public void setUser(User user) { this.user = user; }
//
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//
//    public String getSpecialization() { return specialization; }
//    public void setSpecialization(String specialization) { this.specialization = specialization; }
//
//    public String getContact() { return contact; }
//    public void setContact(String contact) { this.contact = contact; }
//
//    public String getHospital() { return hospital; }
//    public void setHospital(String hospital) { this.hospital = hospital; }
//}


package com.medvalt.medvalt.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    // ── Original fields ──────────────────────────────────────────────────────
    private String name;
    private String specialization;
    private String contact;
    private String hospital;

    // ── NEW fields ────────────────────────────────────────────────────────────
    private String email;
    private String gender;
    private String qualification;   // e.g. MBBS, MD, MS
    private int    experience;      // years of experience
    private double consultationFee; // in INR

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getHospital() { return hospital; }
    public void setHospital(String hospital) { this.hospital = hospital; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }
}


//package com.medvalt.medvalt.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "doctors")
//public class Doctor {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    @JsonIgnoreProperties({"doctor", "patient", "hibernateLazyInitializer", "handler"})
//    private User user;  // ✅ replaced @JsonBackReference with this
//
//    private String name;
//    private String specialization;
//    private String contact;
//    private String hospital;
//    private String email;
//    private String gender;
//    private String qualification;
//    private int    experience;
//    private double consultationFee;
//
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//    public User getUser() { return user; }
//    public void setUser(User user) { this.user = user; }
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//    public String getSpecialization() { return specialization; }
//    public void setSpecialization(String specialization) { this.specialization = specialization; }
//    public String getContact() { return contact; }
//    public void setContact(String contact) { this.contact = contact; }
//    public String getHospital() { return hospital; }
//    public void setHospital(String hospital) { this.hospital = hospital; }
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//    public String getGender() { return gender; }
//    public void setGender(String gender) { this.gender = gender; }
//    public String getQualification() { return qualification; }
//    public void setQualification(String qualification) { this.qualification = qualification; }
//    public int getExperience() { return experience; }
//    public void setExperience(int experience) { this.experience = experience; }
//    public double getConsultationFee() { return consultationFee; }
//    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }
//}