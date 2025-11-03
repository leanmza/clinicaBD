package com.leandro.clinica.service;


import com.leandro.clinica.DTO.EspecialidadDTO;
import com.leandro.clinica.model.Especialidad;
import com.leandro.clinica.repository.IEspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadService implements IEspecialidadService {

    @Autowired
    private IEspecialidadRepository especialidadRepo;

    @Override
    public Especialidad getEspecialidadByName(String nombre) {

        Especialidad especialidad = especialidadRepo.findEspecialidadByNombre(nombre);

        return especialidad;

    }

    @Override
    public List<EspecialidadDTO> getEspecialidades() {
        return especialidadRepo.findAll().stream().map(this::mapearDTO).toList();
    }

    @Override
    public EspecialidadDTO getEspecialidadById(long id) {
        return especialidadRepo.findById(id).map(this::mapearDTO).orElseGet(null);
    }

    @Override
    public void createEspecialidad(Especialidad especialidad) {
        especialidadRepo.save(especialidad);
    }

    private EspecialidadDTO mapearDTO(Especialidad especialidad) {
        EspecialidadDTO especialidadDTO = new EspecialidadDTO();
        especialidadDTO.setEspecialidad(especialidad.getNombre());
        return especialidadDTO;
    }
}
