package com.leandro.clinica.controller;

import com.leandro.clinica.DTO.DoctorResponseDTO;
import com.leandro.clinica.model.Doctor;
import com.leandro.clinica.service.IDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    @Autowired
    private IDoctorService doctorService;

    @GetMapping
    public List<DoctorResponseDTO> getDoctores(){
        return doctorService.getDoctores();
    }

    @GetMapping("/{id}")
    public DoctorResponseDTO getDoctorById(@PathVariable long id){
        return doctorService.getDoctorById(id);
    }

    @PostMapping
    public String createDoctor(@RequestBody Doctor doctor){
        doctorService.createDoctor(doctor);
        return "Doctor creado correctamente";
    }

    @DeleteMapping("/{id}")
    public String deleteDoctor(@PathVariable long id){
        doctorService.deleteDoctor(id);
        return "Doctor borrado correctamente";
    }

    @PutMapping("/{id}")
    public Doctor updateDoctor(@PathVariable long id, @RequestBody Doctor doctor){
        doctorService.updateDoctor(id, doctor);
        return doctor;
    }
}
