package com.leandro.clinica.repository;

import com.leandro.clinica.model.Doctor;
import com.leandro.clinica.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ITurnoRepository extends JpaRepository<Turno, Long> {

    @Query("SELECT MAX(t.fechaHora) FROM Turno t WHERE t.doctor = :doctor")
    LocalDateTime findUltimaFechaTurnoByDoctor(@Param("doctor") Doctor doctor);

    @Query("SELECT t FROM Turno t WHERE t.fechaHora >= :fechaActual")
    List<Turno> findTurnosDesdeFecha(@Param("fechaActual") LocalDateTime fechaActual);
}
