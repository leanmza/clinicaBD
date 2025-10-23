package com.leandro.clinica.service;

import com.leandro.clinica.DTO.TurnoDTO;
import com.leandro.clinica.model.Doctor;
import com.leandro.clinica.model.Paciente;
import com.leandro.clinica.model.Turno;
import com.leandro.clinica.repository.ITurnoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TurnoService implements ITurnoService {


    @Autowired
    private ITurnoRepository turnoRepo;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private DoctorService doctorService;


    @Override
    public List<TurnoDTO> getTurnosByNombreDoctor(String nombre, String apellido) {
        return turnoRepo.findTurnoByNombreDoctor(nombre, apellido).stream().map(this::mapearDTO).toList();
    }

    @Override
    public List<TurnoDTO> getTurnosByNombrePaciente(String nombre, String apellido) {
        return turnoRepo.findTurnoByNombrePaciente(nombre, apellido).stream().map(this::mapearDTO).toList();
    }

    @Override
    public List<TurnoDTO> getTurnosPendientes() {
        return turnoRepo.findTurnosDesdeFecha(LocalDateTime.now()).stream().map(this::mapearDTO).toList();
    }

    @Override
    public TurnoDTO asignarTurno(Turno turno) {
        Doctor doctor = turno.getDoctor();

        LocalDateTime fechaActual = LocalDateTime.now();

        //Primero reviso si hay turnos cancelados para el doctor
        List<Turno> turnosCancelados = turnoRepo.findTurnosCanceladosPorDoctorDesdeFecha(turno.getDoctor(), fechaActual);

        //Si hay turnos cancelados, le asigno a turno, que viene por parámetro con el doctor y el paciente,  el id y fecha/hora
        // del primer turno cancelado de la lista de turnos cancelados
        if (!turnosCancelados.isEmpty()) {
            turno.setId(turnosCancelados.get(0).getId());
            turno.setOcupado(true);
            turno.setFechaHora(turnosCancelados.get(0).getFechaHora());
            turnoRepo.save(turno);
            return mapearDTO(turno);
        } else {

            // Busco la fecha del último turno del doctor
            LocalDateTime ultimaFecha = turnoRepo.findUltimaFechaTurnoByDoctor(doctor);

            // Si ultimaFecha es null, busco el primer horario libre del día siguiente;
            if (ultimaFecha == null) {
                ultimaFecha = primerTurnoDelProximoDía();
            }

            // Busco el proximo horario disponible
            LocalDateTime siguiente = siguienteTurno(ultimaFecha);

            Turno turnoNuevo = new Turno();
            turnoNuevo.setDoctor(doctor);
            turnoNuevo.setPaciente(turno.getPaciente());
            turnoNuevo.setFechaHora(siguiente);
            turnoNuevo.setOcupado(true);
            turnoRepo.save(turnoNuevo);
            return mapearDTO(turnoNuevo);
        }
    }

    @Override
    public List<TurnoDTO> getTurnos() {
        return turnoRepo.findAllOrdenadosPorFecha().stream().map(this::mapearDTO).toList();
    }

    @Override
    public TurnoDTO getTurnoById(long id) {
        return turnoRepo.findById(id).map(this::mapearDTO).orElseGet(null);
    }

    @Override
    public void deleteTurno(long id) {
        Turno turno = turnoRepo.findById(id).orElseGet(null);
        //No borro de la BD el turno, solo le cambio el valor a ocupado a false
        turno.setOcupado(false);
        turnoRepo.save(turno);
    }

    @Override
    public List<TurnoDTO> getTurnosCancelados() {
        return turnoRepo.findTurnosCanceladosDesdeFecha(LocalDateTime.now()).stream().map(this::mapearDTO).toList();
    }

    private LocalDateTime primerTurnoDelProximoDía() {
        LocalDate hoy = LocalDate.now();
        //Establezco el día posterior a hoy, si hoy es viernes le sumo 3 para que pase al lunes siguiente
        LocalDate proximoDía;
        if (hoy.getDayOfWeek() == DayOfWeek.FRIDAY) {
            proximoDía = hoy.plusDays(3);

        } else {
            proximoDía = hoy.plusDays(1);
        }

        return LocalDateTime.of(proximoDía, LocalTime.of(8, 0));
    }

    private LocalDateTime siguienteTurno(LocalDateTime ultimoTurno) {
        //Sumo 30 minutos al la hora del último turno
        LocalDateTime siguienteFecha = ultimoTurno.plusMinutes(30);

        // Si la hora se pasa de las 18:00, pasa al siguiente día siguiente a las 8:00
        if (siguienteFecha.toLocalTime().isAfter(LocalTime.of(18, 0))) {
            siguienteFecha = LocalDateTime.of(siguienteFecha.toLocalDate().plusDays(1), LocalTime.of(8, 0));
        }

        // Si el día siguiente cae sábado o domingo, pasa para el lunes al lunes
        DayOfWeek dia = siguienteFecha.getDayOfWeek();
        if (dia == DayOfWeek.SATURDAY) {
            siguienteFecha = LocalDateTime.of(siguienteFecha.toLocalDate().plusDays(2), LocalTime.of(8, 0));
        } else if (dia == DayOfWeek.SUNDAY) {
            siguienteFecha = LocalDateTime.of(siguienteFecha.toLocalDate().plusDays(1), LocalTime.of(8, 0));
        }

        return siguienteFecha;
    }

    private TurnoDTO mapearDTO(Turno turno) {
        TurnoDTO turnoDTO = new TurnoDTO();

        turnoDTO.setId(turno.getId());
        turnoDTO.setPaciente(pacienteService.getPacienteById(turno.getPaciente().getId()));
        turnoDTO.setDoctor(doctorService.getDoctorById(turno.getDoctor().getId()));
        turnoDTO.setFecha(turno.getFechaHora().toLocalDate());
        turnoDTO.setHora(turno.getFechaHora().toLocalTime());
        turnoDTO.setOcupado(turno.isOcupado());
        return turnoDTO;


    }
}


