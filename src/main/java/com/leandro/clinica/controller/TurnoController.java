package com.leandro.clinica.controller;

import com.leandro.clinica.DTO.TurnoResponseDTO;
import com.leandro.clinica.model.Turno;
import com.leandro.clinica.service.ITurnoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turno")
public class TurnoController {

    @Autowired
    private ITurnoService turnoService;

    @GetMapping
    public List<TurnoResponseDTO> getTurnos() {
        return turnoService.getTurnos();
    }

    @GetMapping("{id}")
    public TurnoResponseDTO getTurnoById(@PathVariable long id) {
        return turnoService.getTurnoById(id);
    }

    @GetMapping("/pendientes")
    public List<TurnoResponseDTO> getTurnosPendientes() {
        return turnoService.getTurnosPendientes();
    }

    @GetMapping("/cancelados")
    public List<TurnoResponseDTO> getTurnosCancelados() {
        return turnoService.getTurnosCancelados();

    }

    @GetMapping("/doctor")
    public List<TurnoResponseDTO> getTurnosByNombreYApellidoDoctor(@RequestParam @Valid String nombre,
                                                           @RequestParam @Valid String apellido) {
        return turnoService.getTurnosByNombreDoctor(nombre, apellido);
    }

    @GetMapping("/paciente")
    public List<TurnoResponseDTO> getTurnosByNombreYApellidoPaciente(@RequestParam @Valid String nombre,
                                                             @RequestParam @Valid String apellido) {
        return turnoService.getTurnosByNombrePaciente(nombre, apellido);
    }

    @PostMapping("/asignar")
    public TurnoResponseDTO asignarTurno(@RequestBody Turno turno) {
        TurnoResponseDTO turnoAsignado = turnoService.asignarTurno(turno);
        return turnoAsignado;
    }

    @PostMapping("/reservar")
    public TurnoResponseDTO reservarTurno(@RequestBody Turno turno) {
        return turnoService.reservarTurno(turno);
    }


    @DeleteMapping("/{id}")
    public String deleteTurno(@PathVariable long id) {
        turnoService.deleteTurno(id);
        return "Turno cancelado";
    }
}
