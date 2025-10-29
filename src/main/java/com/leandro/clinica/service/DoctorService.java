package com.leandro.clinica.service;

import com.leandro.clinica.DTO.DoctorResponseDTO;
import com.leandro.clinica.model.Doctor;
import com.leandro.clinica.model.Especialidad;
import com.leandro.clinica.repository.IDoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService implements IDoctorService {

    @Autowired
    private IDoctorRepository doctorRepo;

    @Autowired
    private EspecialidadService especialidadService;

    @Override
    public List<DoctorResponseDTO> getDoctores() {
        List<DoctorResponseDTO> listaDoctores = doctorRepo.findAll().stream().map(this::mapearDTO).toList();

        if(listaDoctores.isEmpty()){
            return List.of(llenarMensajeError("No hay doctores registrados"));
        }
        return listaDoctores;
    }

    @Override
    public DoctorResponseDTO getDoctorById(long id) {
        DoctorResponseDTO doctorDTO = doctorRepo.findById(id).map(this::mapearDTO).orElse(null);

        if (doctorDTO == null){
            return llenarMensajeError("El doctor no existe");
        }
        return doctorDTO;
    }

    @Override
    public void createDoctor(Doctor doctor) {
        Especialidad especialidad = especialidadService.getEspecialidadByName(doctor.getEspecialidad().getNombre());
        if (especialidad != null) {
            doctor.setEspecialidad(especialidad);
        }
        doctorRepo.save(doctor);
    }

    @Override
    public void deleteDoctor(long id) {
        doctorRepo.deleteById(id);
    }

    @Override
    public void updateDoctor(long id, Doctor doctor) {
        doctor.setId(id);

        Especialidad especialidad = especialidadService.getEspecialidadByName(doctor.getEspecialidad().getNombre());
        if (especialidad != null) {
            doctor.setEspecialidad(especialidad);
        }

        doctorRepo.save(doctor);
    }

    private DoctorResponseDTO mapearDTO(Doctor doctor) {
        DoctorResponseDTO doctorDTO = new DoctorResponseDTO();
        doctorDTO.setId(doctor.getId());
        doctorDTO.setNombre(doctor.getNombre());
        doctorDTO.setApellido(doctor.getApellido());
        doctorDTO.setEmail(doctor.getEmail());
        doctorDTO.setCelular(doctor.getCelular());
        doctorDTO.setEspecialidad(doctor.getEspecialidad().getNombre());

        return doctorDTO;
    }

    private DoctorResponseDTO llenarMensajeError(String mensajeError) {
        DoctorResponseDTO errorDTO = new DoctorResponseDTO();
        errorDTO.setMensajeError(mensajeError);
        return errorDTO;
    }
}
