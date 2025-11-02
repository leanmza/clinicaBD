package com.leandro.clinica.service;


import com.leandro.clinica.DTO.PacienteResponseDTO;
import com.leandro.clinica.model.Paciente;

import java.util.List;

public interface IPacienteService {

    List<PacienteResponseDTO> getPacientes();

    PacienteResponseDTO getPacienteById(long id);

    void createPaciente(Paciente paciente);

    void deletePaciente(long id);

    void updatePaciente(long id, Paciente paciente);







}

