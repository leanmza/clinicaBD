package com.leandro.clinica.service;

import com.leandro.clinica.DTO.DoctorDTO;
import com.leandro.clinica.model.Doctor;

import java.util.List;

public interface IDoctorService {
    List<DoctorDTO> getDoctores();

    DoctorDTO getDoctorById(long id);

    void createDoctor(Doctor doctor);

    void deleteDoctor(long id);

    void updateDoctor(long id, Doctor doctor);
}
