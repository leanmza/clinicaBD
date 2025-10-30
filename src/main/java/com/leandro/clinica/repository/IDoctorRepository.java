package com.leandro.clinica.repository;

import com.leandro.clinica.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface IDoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("SELECT d.horarios.horaInicio FROM Doctor d WHERE d = :doctor")
    LocalTime getHorarioInicio(@Param("doctor") Doctor doctor);

    @Query("SELECT d.horarios.horaFin FROM Doctor d WHERE d = :doctor")
    LocalTime getHorarioFin(@Param("doctor") Doctor doctor);

}
