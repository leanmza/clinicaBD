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
    public ResponseEntity<List<PacienteDTO>> getPacientes() {
        return ResponseEntity.ok(pacienteService.getPacientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> getPacienteById(@PathVariable long id) {
        return ResponseEntity.ok(pacienteService.getPacienteById(id));
    }

    @PostMapping
    public ResponseEntity<String> createPaciente(@RequestBody Paciente paciente) {
        pacienteService.createPaciente(paciente);
        return ResponseEntity.ok("Paciente creado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePaciente(@PathVariable long id) {
        pacienteService.deletePaciente(id);
        return ResponseEntity.ok("Paciente eliminado");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> updatePaciente(@PathVariable long id, @RequestBody Paciente paciente) {
        pacienteService.updatePaciente(id, paciente);
        return ResponseEntity.ok(paciente);
    }

}
