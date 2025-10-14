package com.leandro.clinica.service;


import com.leandro.clinica.DTO.PacienteDTO;
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
    public List<PacienteDTO> getPacientes() {
        return pacienteRepo.findAll().stream().map(this::mapearDTO).toList();
    }

    @Override
    public PacienteDTO getPacienteById(long id) {
        return pacienteRepo.findById(id).map(this::mapearDTO).orElse(null);
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

    private PacienteDTO mapearDTO(Paciente paciente) {
        PacienteDTO pacienteDTO = new PacienteDTO();

        pacienteDTO.setId(paciente.getId());
        pacienteDTO.setNombre(paciente.getNombre());
        pacienteDTO.setApellido(paciente.getApellido());
        pacienteDTO.setEmail(paciente.getEmail());
        pacienteDTO.setCelular(paciente.getCelular());

        return pacienteDTO;
    }

}
