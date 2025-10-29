package com.leandro.clinica.service;

import com.leandro.clinica.DTO.TurnoResponseDTO;
import com.leandro.clinica.model.Turno;

import java.util.List;

public interface ITurnoService {
    TurnoResponseDTO asignarTurno(Turno turno);

    List<TurnoResponseDTO> getTurnos();

    TurnoResponseDTO getTurnoById(long id);

    List<TurnoResponseDTO> getTurnosPendientes();

    List<TurnoResponseDTO> getTurnosByNombreDoctor(String nombre, String apellido);

    List<TurnoResponseDTO> getTurnosByNombrePaciente(String nombre, String apellido);

    void deleteTurno(long id);

    List<TurnoResponseDTO> getTurnosCancelados();

    TurnoResponseDTO reservarTurno(Turno turno);
}
