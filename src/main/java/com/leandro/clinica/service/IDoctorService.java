package com.leandro.clinica.service;

import com.leandro.clinica.DTO.DoctorResponseDTO;
import com.leandro.clinica.model.Doctor;

import java.util.List;

public interface IDoctorService {
    List<DoctorResponseDTO> getDoctores();

    DoctorResponseDTO getDoctorById(long id);

    void createDoctor(Doctor doctor);

    void deleteDoctor(long id);

    void updateDoctor(long id, Doctor doctor);
}
