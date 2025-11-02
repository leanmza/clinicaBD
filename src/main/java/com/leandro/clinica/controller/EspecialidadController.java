package com.leandro.clinica.controller;

import com.leandro.clinica.DTO.EspecialidadResponseDTO;
import com.leandro.clinica.service.IEspecialidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/especialidades")
public class EspecialidadController {

    @Autowired
    private IEspecialidadService especialidadService;
    @GetMapping()
    public List<EspecialidadResponseDTO> getEspecialidades(){
        return especialidadService.getEspecialidades();
    }
}
