package com.leandro.clinica.controller;

import com.leandro.clinica.DTO.TurnoDTO;
import com.leandro.clinica.model.Turno;
import com.leandro.clinica.service.ITurnoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turno")
public class TurnoController {

    @Autowired
    private ITurnoService turnoService;

    @GetMapping
    public ResponseEntity<List<TurnoDTO>> getTurnos(){
        return ResponseEntity.ok(turnoService.getTurnos());
    }

    @GetMapping("{id}")
    public ResponseEntity<TurnoDTO> getTurnoById(@PathVariable long id){
        return ResponseEntity.ok(turnoService.getTurnoById(id));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<TurnoDTO>> getTurnosPendientes(){
        return ResponseEntity.ok(turnoService.getTurnosPendientes());
    }

    @GetMapping("/doctor")
    public ResponseEntity<?> getTurnosByNombreYApellidoDoctor(@RequestParam @Valid String nombre,
                                                              @RequestParam @Valid String apellido) {
        return ResponseEntity.ok(turnoService.getTurnosByNombreDoctor(nombre, apellido));
    }

    @GetMapping("/paciente")
    public ResponseEntity<?> getTurnosByNombreYApellidoPaciente(@RequestParam @Valid String nombre,
                                                              @RequestParam @Valid String apellido) {
        return ResponseEntity.ok(turnoService.getTurnosByNombrePaciente(nombre, apellido));
    }

    @PostMapping("/asignar")
    public ResponseEntity<Turno> asignarTurno(@RequestBody Turno turno) {
         Turno turnoAsignado = turnoService.asignarTurno(turno);
        return ResponseEntity.ok(turnoAsignado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTurno(@PathVariable long id){
        turnoService.deleteTurno(id);
        return ResponseEntity.ok("Turno cancelado");
    }
}
