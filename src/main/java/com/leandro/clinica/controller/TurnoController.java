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
    public List<TurnoDTO> getTurnos(){
        return turnoService.getTurnos();
    }

    @GetMapping("{id}")
    public TurnoDTO getTurnoById(@PathVariable long id){
        return turnoService.getTurnoById(id);
    }

    @GetMapping("/pendientes")
    public List<TurnoDTO> getTurnosPendientes(){
        return turnoService.getTurnosPendientes();
    }

    @GetMapping("/cancelados")
    public List<TurnoDTO> getTurnosCancelados(){
        return turnoService.getTurnosCancelados();

    }

    @GetMapping("/doctor")
    public List<TurnoDTO> getTurnosByNombreYApellidoDoctor(@RequestParam @Valid String nombre,
                                                              @RequestParam @Valid String apellido) {
        return turnoService.getTurnosByNombreDoctor(nombre, apellido);
    }

    @GetMapping("/paciente")
    public List<TurnoDTO> getTurnosByNombreYApellidoPaciente(@RequestParam @Valid String nombre,
                                                              @RequestParam @Valid String apellido) {
        return turnoService.getTurnosByNombrePaciente(nombre, apellido);
    }

    @PostMapping("/asignar")
    public TurnoDTO asignarTurno(@RequestBody Turno turno) {
         TurnoDTO turnoAsignado = turnoService.asignarTurno(turno);
        return turnoAsignado;
    }

    @DeleteMapping("/{id}")
    public String deleteTurno(@PathVariable long id){
        turnoService.deleteTurno(id);
        return "Turno cancelado";
    }
}
