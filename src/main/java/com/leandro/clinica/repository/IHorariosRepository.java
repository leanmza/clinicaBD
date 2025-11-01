package com.leandro.clinica.repository;

import com.leandro.clinica.model.Horarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface IHorariosRepository extends JpaRepository<Horarios, Long> {
    @Query("SELECT h " +
            "FROM Horarios h " +
            "WHERE h.horaInicio = :horaInicio " +
            "AND h.horaFin = :horaFin")
    Horarios getHorarioByRange(@Param("horaInicio") LocalTime horaInicio,
                               @Param("horaFin") LocalTime horaFin);
}
