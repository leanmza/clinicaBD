package com.leandro.clinica.service;

import com.leandro.clinica.DTO.TurnoDTO;
import com.leandro.clinica.model.Turno;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ITurnoService {
    TurnoDTO asignarTurno(Turno turno);

    List<TurnoDTO> getTurnos();

    TurnoDTO getTurnoById(long id);

    List<TurnoDTO> getTurnosPendientes();

    List<TurnoDTO> getTurnosByNombreDoctor(String nombre, String apellido);

    List<TurnoDTO> getTurnosByNombrePaciente(String nombre, String apellido);

    void deleteTurno(long id);

    List<TurnoDTO> getTurnosCancelados();

    TurnoDTO reservarTurno(Turno turno);
}
