////package com.medvalt.medvalt.entity;
////
////import jakarta.persistence.*;
////import com.fasterxml.jackson.annotation.JsonBackReference;
////import java.util.List;
////import com.fasterxml.jackson.annotation.JsonManagedReference;
////import com.fasterxml.jackson.annotation.JsonIgnore;
////
////@Entity
////@Table(name = "patients")
////public class Patient {
////    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
////
////    private String name;
////    private int age;
////    private String gender;
////    private String contact;
////
////    @OneToOne
////    @JoinColumn(name = "user_id")
////    @JsonBackReference
////    private User user;
////
////    // ✅ Add this field here
////    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
////    private List<PatientDoctorAccess> accessList;
////
////    // --- Getters and Setters ---
////    public Long getId() { return id; }
////    public void setId(Long id) { this.id = id; }
////
////    public String getName() { return name; }
////    public void setName(String name) { this.name = name; }
////
////    public int getAge() { return age; }
////    public void setAge(int age) { this.age = age; }
////
////    public String getGender() { return gender; }
////    public void setGender(String gender) { this.gender = gender; }
////
////    public String getContact() { return contact; }
////    public void setContact(String contact) { this.contact = contact; }
////
////    public User getUser() { return user; }
////    public void setUser(User user) { this.user = user; }
////}
//
//package com.medvalt.medvalt.entity;
//
//import jakarta.persistence.*;
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import java.util.List;
//
//@Entity
//@Table(name = "patients")
//public class Patient {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//    private int age;
//    private String gender;
//    private String contact;
//    private String dob;  // ✅ date of birth (used in profile / PatientProfileForm)
//
//    // ── Relationships ──────────────────────────────────────────────────────────
//
//    @OneToOne
//    @JoinColumn(name = "user_id")
//    @JsonBackReference
//    private User user;
//
//    // ✅ Access list (existing)
//    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<PatientDoctorAccess> accessList;
//
//    // ✅ NEW: medical records relationship
//    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore  // avoid circular JSON; access via /medical-records/patient/{id}
//    private List<MedicalRecord> medicalRecords;
//
//    // ── Getters & Setters ──────────────────────────────────────────────────────
//
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//
//    public int getAge() { return age; }
//    public void setAge(int age) { this.age = age; }
//
//    public String getGender() { return gender; }
//    public void setGender(String gender) { this.gender = gender; }
//
//    public String getContact() { return contact; }
//    public void setContact(String contact) { this.contact = contact; }
//
//    public String getDob() { return dob; }
//    public void setDob(String dob) { this.dob = dob; }
//
//    public User getUser() { return user; }
//    public void setUser(User user) { this.user = user; }
//
//    public List<PatientDoctorAccess> getAccessList() { return accessList; }
//    public void setAccessList(List<PatientDoctorAccess> accessList) { this.accessList = accessList; }
//
//    public List<MedicalRecord> getMedicalRecords() { return medicalRecords; }
//    public void setMedicalRecords(List<MedicalRecord> medicalRecords) { this.medicalRecords = medicalRecords; }
//}

package com.medvalt.medvalt.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String gender;
    private String contact;
    private String dob;
    private String email;   // ✅ NEW

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PatientDoctorAccess> accessList;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MedicalRecord> medicalRecords;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<PatientDoctorAccess> getAccessList() { return accessList; }
    public void setAccessList(List<PatientDoctorAccess> accessList) { this.accessList = accessList; }

    public List<MedicalRecord> getMedicalRecords() { return medicalRecords; }
    public void setMedicalRecords(List<MedicalRecord> medicalRecords) { this.medicalRecords = medicalRecords; }
}