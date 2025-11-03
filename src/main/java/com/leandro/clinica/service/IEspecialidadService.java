package com.leandro.clinica.service;

import com.leandro.clinica.DTO.EspecialidadDTO;
import com.leandro.clinica.model.Especialidad;

import java.util.List;

public interface IEspecialidadService {
    EspecialidadDTO getEspecialidadById(long id);

    Especialidad getEspecialidadByName(String nombre);

    void createEspecialidad(Especialidad especialidad);

    List<EspecialidadDTO> getEspecialidades();
}
