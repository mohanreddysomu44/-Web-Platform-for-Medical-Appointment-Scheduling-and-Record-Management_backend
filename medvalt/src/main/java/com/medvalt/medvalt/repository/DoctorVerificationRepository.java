package com.medvalt.medvalt.repository;

import com.medvalt.medvalt.entity.DoctorVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DoctorVerificationRepository extends JpaRepository<DoctorVerification, Long> {

    @Query("SELECT dv FROM DoctorVerification dv JOIN FETCH dv.doctor WHERE dv.doctor.id = :doctorId")
    Optional<DoctorVerification> findByDoctorId(@Param("doctorId") Long doctorId);

    boolean existsByDoctorId(Long doctorId);
}