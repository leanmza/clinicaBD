package com.leandro.clinica.service;

import com.leandro.clinica.DTO.DoctorDTO;
import com.leandro.clinica.DTO.TurnoDTO;
import com.leandro.clinica.model.Doctor;
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
    public List<TurnoDTO> getTurnosByNombreDoctor(String nombre, String apellido) {
        List<TurnoDTO> listaTurnos = turnoRepo.findTurnoByNombreDoctor(nombre, apellido).stream().map(this::mapearDTO).toList();
        if (listaTurnos.isEmpty()) {
            return List.of(llenarMensajeError("No hay turnos pendientes para el doctor" + nombre + " " + apellido));
        }
        return listaTurnos;
    }

    @Override
    public List<TurnoDTO> getTurnosByNombrePaciente(String nombre, String apellido) {
        List<TurnoDTO> listaTurnos = turnoRepo.findTurnoByNombrePaciente(nombre, apellido).stream().map(this::mapearDTO).toList();

        if (listaTurnos.isEmpty()){
            return List.of(llenarMensajeError("No hay turnos pendientes para el paciente" + nombre + " " + apellido));
        }
        return listaTurnos;
    }

    @Override
    public List<TurnoDTO> getTurnosPendientes() {
        List<TurnoDTO> listaTurnos = turnoRepo.findTurnosDesdeFecha(LocalDateTime.now()).stream().map(this::mapearDTO).toList();

        if (listaTurnos.isEmpty()){
            return List.of(llenarMensajeError("No hay turnos pendientes en la clínica"))  ;
        }
        return listaTurnos;
    }

    //Asigna automáticamente los turnos, uno detrás de otro, excepto que haya un turno previo cancelado
    @Override
    public TurnoDTO asignarTurno(Turno turno) {
        // Obtengo la fecha y hora actual
        LocalDateTime fechaActual = LocalDateTime.now();

        //Primero busco el primer turno sin ocupar del doctor, puede ser cancelado
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

            // Busco la fecha y hora del último turno del doctor y los horarios de inicio y fin
            LocalDateTime ultimaFecha = turnoRepo.findUltimaFechaTurnoByDoctor(turno.getDoctor());

            LocalTime horaInicio = doctorService.getHorarioInicio(turno.getDoctor());
            LocalTime horaFin = doctorService.getHorarioFin(turno.getDoctor());

                // Si ultimaFecha es null, busco el primer horario libre del día siguiente;
            if (ultimaFecha == null) {
                ultimaFecha = primerTurnoDelProximoDía(horaInicio, horaFin);
            }

            // Busco el proximo horario disponible
            LocalDateTime siguiente = siguienteTurno(ultimaFecha, horaInicio, horaFin);

            turno.setFechaHora(siguiente);
            turno.setOcupado(true);
            turnoRepo.save(turno);
            return mapearDTO(turno);
        }
    }

    @Override
    public List<TurnoDTO> getTurnos() {
        List<TurnoDTO> listaTurnos =  turnoRepo.findAllOrdenadosPorFecha().stream().map(this::mapearDTO).toList();
        if (listaTurnos.isEmpty()){
            return List.of(llenarMensajeError("No hay turnos cargados"))  ;
        }
        return listaTurnos;
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
        List<TurnoDTO> listaTurnos =  turnoRepo.findTurnosCanceladosDesdeFecha(LocalDateTime.now()).stream().map(this::mapearDTO).toList();
        if (listaTurnos.isEmpty()){
            return List.of(llenarMensajeError("No hay turnos cancelados"))  ;
        }
        return listaTurnos;
    }

    @Override
    public TurnoDTO reservarTurno(Turno turno) {
        Optional<Turno> estaDisponible = turnoRepo.findTurnoDisponiblePorDoctoryFecha(turno.getDoctor(), turno.getFechaHora());

        // Si estaDisponible es null, seteo ocupado true al turno que viene por parámetro y lo guardo
        if (estaDisponible.isEmpty()) {

            turno.setOcupado(true);
            turnoRepo.save(turno);
            return mapearDTO(turno);
        }

        // Si encuentra un turno que ya existe con el doctor y fecha pasados, reviso si ocupado es esta false,
        // si lo está, le asigno el id del turno existente y cambio ocupado a true
        if (estaDisponible.isPresent() && !estaDisponible.get().isOcupado()) {
            turno.setId(estaDisponible.get().getId());
            turno.setOcupado(true);
            turnoRepo.save(turno);
            return mapearDTO(turno);
        } else {
            return llenarMensajeError("La fecha y hora elegidas no están disponibles");
        }
    }

    private LocalDateTime primerTurnoDelProximoDía(LocalTime horaInicio, LocalTime horaFin) {
        LocalDate hoy = LocalDate.now();
        //Establezco el día posterior a hoy, si hoy es viernes le sumo 3 para que pase al lunes siguiente
        LocalDate proximoDía;
        if (hoy.getDayOfWeek() == DayOfWeek.FRIDAY) {
            proximoDía = hoy.plusDays(3);

        } else {
            proximoDía = hoy.plusDays(1);
        }
        LocalTime horaPrimerTurno;

        if(horaInicio.isBefore(horaFin)){
            horaPrimerTurno = horaInicio;
        } else {
            horaPrimerTurno =horaFin;
        }

        return LocalDateTime.of(proximoDía, horaPrimerTurno);
    }

    private LocalDateTime siguienteTurno(LocalDateTime ultimoTurno, LocalTime horaInicio, LocalTime horaFin) {
        //Sumo 30 minutos al la hora del último turno
        LocalDateTime siguienteFecha = ultimoTurno.plusMinutes(30);

        // Si la hora se pasa de las 18:00, pasa al siguiente día siguiente a las 8:00
        if (siguienteFecha.toLocalTime().isAfter(horaFin)) {
            siguienteFecha = LocalDateTime.of(siguienteFecha.toLocalDate().plusDays(1), horaInicio);
        }

        // Si el día siguiente cae sábado o domingo, pasa para el lunes al lunes
        DayOfWeek dia = siguienteFecha.getDayOfWeek();
        if (dia == DayOfWeek.SATURDAY) {
            siguienteFecha = LocalDateTime.of(siguienteFecha.toLocalDate().plusDays(2), horaInicio);
        } else if (dia == DayOfWeek.SUNDAY) {
            siguienteFecha = LocalDateTime.of(siguienteFecha.toLocalDate().plusDays(1), horaFin);
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

    private TurnoDTO llenarMensajeError(String mensajeError) {
        TurnoDTO errorDTO = new TurnoDTO();
        errorDTO.setMensajeError(mensajeError);
        return errorDTO;
    }
}


