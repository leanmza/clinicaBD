package com.leandro.clinica.service;

import com.leandro.clinica.DTO.TurnoDTO;
import com.leandro.clinica.model.Turno;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ITurnoService {
    Turno asignarTurno(Turno turno);

    List<TurnoDTO> getTurnos();

    TurnoDTO getTurnoById(long id);

    List<TurnoDTO> getTurnosPendientes();
}
