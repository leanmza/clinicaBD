package com.leandro.clinica.controller;

import com.leandro.clinica.DTO.TurnoDTO;
import com.leandro.clinica.model.Turno;
import com.leandro.clinica.service.ITurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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

    @PostMapping("/asignar")
    public ResponseEntity<Turno> asignarTurno(@RequestBody Turno turno) {
         Turno turnoAsignado = turnoService.asignarTurno(turno);
        return ResponseEntity.ok(turnoAsignado);
    }
}
