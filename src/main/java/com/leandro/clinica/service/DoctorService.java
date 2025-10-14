package com.leandro.clinica.service;

import com.leandro.clinica.DTO.DoctorDTO;
import com.leandro.clinica.model.Doctor;
import com.leandro.clinica.repository.IDoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService implements IDoctorService{

    @Autowired
    private IDoctorRepository doctorRepo;

    @Override
    public List<DoctorDTO> getDoctores() {
        return doctorRepo.findAll().stream().map(this::mapearDTO).toList();
    }

    @Override
    public DoctorDTO getDoctorById(long id) {
        return doctorRepo.findById(id).map(this::mapearDTO).orElse(null);
    }

    @Override
    public void createDoctor(Doctor doctor) {
        doctorRepo.save(doctor);
    }

    @Override
    public void deleteDoctor(long id) {
        doctorRepo.deleteById(id);
    }

    @Override
    public void updateDoctor(long id, Doctor doctor) {
        doctor.setId(id);
        doctorRepo.save(doctor);
    }

    private DoctorDTO mapearDTO(Doctor doctor){
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(doctor.getId());
        doctorDTO.setNombre(doctor.getNombre());
        doctorDTO.setApellido(doctor.getApellido());
        doctorDTO.setEmail(doctor.getEmail());
        doctorDTO.setCelular(doctor.getCelular());
        doctorDTO.setEspecialidad(doctor.getEspecialidad());

        return doctorDTO;
    }
}
