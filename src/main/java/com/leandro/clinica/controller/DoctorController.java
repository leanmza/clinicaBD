package com.leandro.clinica.controller;

import com.leandro.clinica.DTO.DoctorDTO;
import com.leandro.clinica.model.Doctor;
import com.leandro.clinica.service.IDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    @Autowired
    private IDoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getDoctores(){
        return ResponseEntity.ok(doctorService.getDoctores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable long id){
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @PostMapping
    public ResponseEntity<String> createDoctor(@RequestBody Doctor doctor){
        doctorService.createDoctor(doctor);
        return ResponseEntity.ok("Doctor creado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable long id){
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok("Doctor borrado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable long id, @RequestBody Doctor doctor){
        doctorService.updateDoctor(id, doctor);
        return ResponseEntity.ok(doctor);
    }
}
