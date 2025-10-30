package com.leandro.clinica.service;

import com.leandro.clinica.DTO.DoctorDTO;
import com.leandro.clinica.model.Doctor;
import com.leandro.clinica.model.Especialidad;
import com.leandro.clinica.repository.IDoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class DoctorService implements IDoctorService {

    @Autowired
    private IDoctorRepository doctorRepo;

    @Autowired
    private EspecialidadService especialidadService;

    @Override
    public List<DoctorDTO> getDoctores() {
        List<DoctorDTO> listaDoctores = doctorRepo.findAll().stream().map(this::mapearDTO).toList();

        if(listaDoctores.isEmpty()){
            return List.of(llenarMensajeError("No hay doctores registrados"));
        }
        return listaDoctores;
    }

    @Override
    public DoctorDTO getDoctorById(long id) {
        DoctorDTO doctorDTO = doctorRepo.findById(id).map(this::mapearDTO).orElse(null);

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

    public LocalTime getHorarioInicio(Doctor doctor){
        return doctorRepo.getHorarioInicio(doctor);
    }

    public LocalTime getHorarioFin(Doctor doctor){
        return doctorRepo.getHorarioFin(doctor);
    }


    private DoctorDTO mapearDTO(Doctor doctor) {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(doctor.getId());
        doctorDTO.setNombre(doctor.getNombre());
        doctorDTO.setApellido(doctor.getApellido());
        doctorDTO.setEmail(doctor.getEmail());
        doctorDTO.setCelular(doctor.getCelular());
        doctorDTO.setEspecialidad(doctor.getEspecialidad().getNombre());
        doctorDTO.setHoraInicio(String.valueOf(doctor.getHorarios().getHoraInicio()));
        doctorDTO.setHoraFin(String.valueOf(doctor.getHorarios().getHoraFin()));

        return doctorDTO;
    }

    private DoctorDTO llenarMensajeError(String mensajeError) {
        DoctorDTO errorDTO = new DoctorDTO();
        errorDTO.setMensajeError(mensajeError);
        return errorDTO;
    }
}
