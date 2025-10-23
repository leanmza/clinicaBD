package com.leandro.clinica.repository;

import com.leandro.clinica.model.Doctor;
import com.leandro.clinica.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ITurnoRepository extends JpaRepository<Turno, Long> {

    //Devuelve todos los turnos ordenados por fecha ascedente
    @Query("SELECT t " +
            "FROM Turno t " +
            "ORDER BY t.fechaHora ASC")
    List<Turno> findAllOrdenadosPorFecha();

    //Devuelve la última fecha y hora ocupada de un doctor en particular
    @Query("SELECT MAX(t.fechaHora) " +
            "FROM Turno t " +
            "WHERE t.doctor = :doctor")
    LocalDateTime findUltimaFechaTurnoByDoctor(@Param("doctor") Doctor doctor);

    //Devuelve todos los turnos pendientes con fecha y hora >= a la actual
    @Query("SELECT t " +
            "FROM Turno t " +
            "WHERE t.fechaHora >= :fechaActual " +
            "ORDER BY t.fechaHora ASC")
    List<Turno> findTurnosDesdeFecha(@Param("fechaActual") LocalDateTime fechaActual);

    //Devuelve todos los turnos cancelandos con fecha y hora >= a la actual
    @Query("SELECT t " +
            "FROM Turno t " +
            "WHERE t.fechaHora >= :fechaActual " +
            "AND t.ocupado = false " +
            "ORDER BY t.fechaHora ASC")
    List<Turno> findTurnosCanceladosDesdeFecha(@Param("fechaActual") LocalDateTime fechaActual);

    //Devuelve todos los turnos asignados a un doctor en particular, filtrando por nombre y apellido del doctor
    @Query("SELECT t " +
            "FROM Turno t " +
            "LEFT JOIN  t.doctor d " +
            "WHERE d.nombre = :nombreDoctor " +
            "AND d.apellido = :apellidoDoctor")
    List<Turno> findTurnoByNombreDoctor(@Param("nombreDoctor") String nombreDoctor,
                                        @Param("apellidoDoctor") String apellidoDoctor);

    //Devuelve todos los turnos sacados por un paciente en particular, filtrando por nombre y apellido del paciente
    @Query("SELECT t " +
            "FROM Turno t " +
            "LEFT JOIN  t.paciente p " +
            "WHERE p.nombre = :nombrePaciente " +
            "AND p.apellido = :apellidoPaciente")
    List<Turno> findTurnoByNombrePaciente(@Param("nombrePaciente") String nombrePaciente,
                                        @Param("apellidoPaciente") String apellidoPaciente);

    //Devuelve los turnos cancelandos de un doctor en particular con fecha y hora >= a la actual
    @Query("SELECT t " +
            "FROM Turno t " +
            "WHERE t.doctor = :doctor " +
            "AND t.fechaHora >= :fechaActual " +
            "AND t.ocupado = false " +
            "ORDER BY t.fechaHora ASC")
    List<Turno> findTurnosCanceladosPorDoctorDesdeFecha(@Param("doctor") Doctor doctor,
                                                        @Param("fechaActual") LocalDateTime fechaActual);

}
