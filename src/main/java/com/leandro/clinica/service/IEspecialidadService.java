package com.leandro.clinica.service;

import com.leandro.clinica.DTO.EspecialidadResponseDTO;
import com.leandro.clinica.model.Especialidad;

import java.util.List;

public interface IEspecialidadService {
    EspecialidadResponseDTO getEspecialidadById(long id);

    Especialidad getEspecialidadByName(String nombre);

    void createEspecialidad(Especialidad especialidad);

    List<EspecialidadResponseDTO> getEspecialidades();
}
