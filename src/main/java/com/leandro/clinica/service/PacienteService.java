package com.leandro.clinica.service;


import com.leandro.clinica.DTO.PacienteResponseDTO;
import com.leandro.clinica.model.Paciente;
import com.leandro.clinica.repository.IPacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService implements IPacienteService {

    @Autowired
    private IPacienteRepository pacienteRepo;

    @Override
    public List<PacienteResponseDTO> getPacientes() {

        List<PacienteResponseDTO> listaPacientes = pacienteRepo.findAll().stream().map(this::mapearDTO).toList();

        if (listaPacientes.isEmpty()) {
            return List.of(llenarMensajeError("No hay pacientes registrados"));
        }

        return listaPacientes;
    }


    @Override
    public PacienteResponseDTO getPacienteById(long id) {
        PacienteResponseDTO pacienteDTO = pacienteRepo.findById(id).map(this::mapearDTO).orElse(null);

        if (pacienteDTO == null) {

            return llenarMensajeError("El paciente no existe");
        }
        return pacienteDTO;
    }

    @Override
    public void createPaciente(Paciente paciente) {
        pacienteRepo.save(paciente);
    }

    @Override
    public void deletePaciente(long id) {
        pacienteRepo.deleteById(id);
    }

    @Override
    public void updatePaciente(long id, Paciente paciente) {
        paciente.setId(id);
        pacienteRepo.save(paciente);
    }

    private PacienteResponseDTO mapearDTO(Paciente paciente) {
        PacienteResponseDTO pacienteDTO = new PacienteResponseDTO();

        pacienteDTO.setId(paciente.getId());
        pacienteDTO.setNombre(paciente.getNombre());
        pacienteDTO.setApellido(paciente.getApellido());
        pacienteDTO.setEmail(paciente.getEmail());
        pacienteDTO.setCelular(paciente.getCelular());

        return pacienteDTO;
    }

    private PacienteResponseDTO llenarMensajeError(String mensajeError) {
        PacienteResponseDTO errorDTO = new PacienteResponseDTO();
        errorDTO.setMensajeError(mensajeError);
        return errorDTO;
    }

}
