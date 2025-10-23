package com.leandro.clinica.repository;

import com.leandro.clinica.model.Doctor;
import com.leandro.clinica.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ITurnoRepository extends JpaRepository<Turno, Long> {

    @Query("SELECT MAX(t.fechaHora) " +
            "FROM Turno t " +
            "WHERE t.doctor = :doctor")
    LocalDateTime findUltimaFechaTurnoByDoctor(@Param("doctor") Doctor doctor);

    @Query("SELECT t " +
            "FROM Turno t " +
            "WHERE t.fechaHora >= :fechaActual " +
            "ORDER BY t.fechaHora ASC")
    List<Turno> findTurnosDesdeFecha(@Param("fechaActual") LocalDateTime fechaActual);

    @Query("SELECT t " +
            "FROM Turno t " +
            "WHERE t.fechaHora >= :fechaActual " +
            "AND t.ocupado = :ocupado " +
            "ORDER BY t.fechaHora ASC")
    List<Turno> findTurnosCanceladosDesdeFecha(@Param("fechaActual") LocalDateTime fechaActual,
                                               @Param("ocupado") boolean ocupado);

    @Query("SELECT t " +
            "FROM Turno t " +
            "LEFT JOIN  t.doctor d " +
            "WHERE d.nombre = :nombreDoctor " +
            "AND d.apellido = :apellidoDoctor")
    List<Turno> findTurnoByNombreDoctor(@Param("nombreDoctor") String nombreDoctor,
                                        @Param("apellidoDoctor") String apellidoDoctor);

    @Query("SELECT t " +
            "FROM Turno t " +
            "LEFT JOIN  t.paciente p " +
            "WHERE p.nombre = :nombrePaciente " +
            "AND p.apellido = :apellidoPaciente")
    List<Turno> findTurnoByNombrePaciente(@Param("nombrePaciente") String nombrePaciente,
                                        @Param("apellidoPaciente") String apellidoPaciente);
}
