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
    public Object getTurnosByNombreDoctor(String nombre, String apellido) {
        return turnoRepo.findTurnoByNombreDoctor(nombre, apellido).stream().map(this::mapearDTO).toList();
    }

    @Override
    public Object getTurnosByNombrePaciente(String nombre, String apellido) {
        return turnoRepo.findTurnoByNombrePaciente(nombre, apellido).stream().map(this::mapearDTO).toList();
    }

    @Override
    public List<TurnoDTO> getTurnosPendientes() {
        return turnoRepo.findTurnosDesdeFecha(LocalDateTime.now()).stream().map(this::mapearDTO).toList();
    }

    @Override
    public Turno asignarTurno(Turno turno) {
        Doctor doctor = turno.getDoctor();

        // Buscar la fecha del último turno del doctor
        LocalDateTime ultimaFecha = turnoRepo.findUltimaFechaTurnoByDoctor(doctor);

        // Si no hay turnos, empezar desde la próxima semana
        if (ultimaFecha == null) {
            ultimaFecha = primerTurnoDeLaProximaSemana();
        }

        // Calcular siguiente horario disponible
        LocalDateTime siguiente = siguienteTurno(ultimaFecha);

        Turno nuevo = new Turno();
        nuevo.setDoctor(doctor);
        nuevo.setPaciente(turno.getPaciente());
        nuevo.setFechaHora(siguiente);
        nuevo.setOcupado(true);

        return turnoRepo.save(nuevo);
    }

    private LocalDateTime primerTurnoDeLaProximaSemana() {
        LocalDate hoy = LocalDate.now();
        // Lunes de la próxima semana
        LocalDate proximoLunes = hoy.with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.MONDAY));
        return LocalDateTime.of(proximoLunes, LocalTime.of(8, 0));
    }

    private LocalDateTime siguienteTurno(LocalDateTime ultimoTurno) {
        LocalDateTime siguiente = ultimoTurno.plusMinutes(30);

        // Si se pasa de las 18:00, pasa al siguiente día hábil a las 8:00
        if (siguiente.toLocalTime().isAfter(LocalTime.of(18, 0))) {
            siguiente = LocalDateTime.of(siguiente.toLocalDate().plusDays(1), LocalTime.of(8, 0));
        }

        // Si cae sábado o domingo, saltar al lunes
        DayOfWeek dia = siguiente.getDayOfWeek();
        if (dia == DayOfWeek.SATURDAY) {
            siguiente = LocalDateTime.of(siguiente.toLocalDate().plusDays(2), LocalTime.of(8, 0));
        } else if (dia == DayOfWeek.SUNDAY) {
            siguiente = LocalDateTime.of(siguiente.toLocalDate().plusDays(1), LocalTime.of(8, 0));
        }

        return siguiente;
    }

    @Override
    public List<TurnoDTO> getTurnos() {
        return turnoRepo.findAll().stream().map(this::mapearDTO).toList();
    }

    @Override
    public TurnoDTO getTurnoById(long id) {

        return turnoRepo.findById(id).map(this::mapearDTO).orElseGet(null);
    }



    private TurnoDTO mapearDTO(Turno turno){
        TurnoDTO turnoDTO = new TurnoDTO();

        turnoDTO.setId(turno.getId());
        turnoDTO.setPaciente(pacienteService.getPacienteById(turno.getPaciente().getId()));
        turnoDTO.setDoctor(doctorService.getDoctorById(turno.getDoctor().getId()));
        turnoDTO.setFechaHora(turno.getFechaHora());
        return turnoDTO;


    }
}


