package com.leandro.clinica.controller;


import com.leandro.clinica.DTO.PacienteDTO;
import com.leandro.clinica.model.Paciente;
import com.leandro.clinica.service.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paciente")
public class PacienteController {

    @Autowired
    private IPacienteService pacienteService;

    @GetMapping
    public List<PacienteDTO> getPacientes() {
        return pacienteService.getPacientes();
    }

    @GetMapping("/{id}")
    public PacienteDTO getPacienteById(@PathVariable long id) {
        return pacienteService.getPacienteById(id);
    }

    @PostMapping
    public String createPaciente(@RequestBody Paciente paciente) {
        pacienteService.createPaciente(paciente);
        return "Paciente creado correctamente";
    }

    @DeleteMapping("/{id}")
    public String deletePaciente(@PathVariable long id) {
        pacienteService.deletePaciente(id);
        return "Paciente eliminado";
    }

    @PutMapping("/{id}")
    public Paciente updatePaciente(@PathVariable long id, @RequestBody Paciente paciente) {
        pacienteService.updatePaciente(id, paciente);
        return paciente;
    }

}
