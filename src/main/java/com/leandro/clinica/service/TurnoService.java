package com.leandro.clinica.service;


import com.leandro.clinica.DTO.TurnoResponseDTO;
import com.leandro.clinica.model.Doctor;
import com.leandro.clinica.model.Turno;
import com.leandro.clinica.repository.ITurnoRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.*;
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
    public List<TurnoResponseDTO> getTurnosByNombreDoctor(String nombre, String apellido) {
        List<TurnoResponseDTO> listaTurnos = turnoRepo.findTurnoByNombreDoctor(nombre, apellido).stream().map(this::mapearDTO).toList();
        if (listaTurnos.isEmpty()) {
            return List.of(llenarMensajeError("No hay turnos pendientes para el doctor" + nombre + " " + apellido));
        }
        return listaTurnos;
    }

    @Override
    public List<TurnoResponseDTO> getTurnosByNombrePaciente(String nombre, String apellido) {
        List<TurnoResponseDTO> listaTurnos = turnoRepo.findTurnoByNombrePaciente(nombre, apellido).stream().map(this::mapearDTO).toList();

        if (listaTurnos.isEmpty()) {
            return List.of(llenarMensajeError("No hay turnos pendientes para el paciente" + nombre + " " + apellido));
        }
        return listaTurnos;
    }

    @Override
    public List<TurnoResponseDTO> getTurnosPendientes() {
        List<TurnoResponseDTO> listaTurnos = turnoRepo.findTurnosDesdeFecha().stream().map(this::mapearDTO).toList();

        if (listaTurnos.isEmpty()) {
            return List.of(llenarMensajeError("No hay turnos pendientes en la clínica"));
        }
        return listaTurnos;
    }

    //Asigna automáticamente los turnos, uno detrás de otro, excepto que haya un turno previo cancelado
    @Override
    public TurnoResponseDTO asignarTurno(Turno turno) {
        // Obtengo la fecha y hora actual
        LocalTime horaInicio = doctorService.getHorarioInicio(turno.getDoctor());
        LocalTime horaFin = doctorService.getHorarioFin(turno.getDoctor());

        //Primero busco el primer turno sin ocupar del doctor, puede ser cancelado
        List<Turno> turnosCancelados = turnoRepo.findTurnosCanceladosPorDoctorDesdeFecha(turno.getDoctor());

        //Si hay turnos cancelados, le asigno a turno, que viene por parámetro con el doctor y el paciente,  el id y fecha/hora
        // del primer turno cancelado de la lista de turnos cancelados
        if (!turnosCancelados.isEmpty()) {
            turno.setId(turnosCancelados.get(0).getId());
            turno.setOcupado(true);
            turno.setFechaHora(turnosCancelados.get(0).getFechaHora());
            turnoRepo.save(turno);
            return mapearDTO(turno);
        }
        //Si la validacion anterior es falsa, reviso si hay huecos de horas y fechas en los turnos del doctor,
        // como puede pasar si se usa la función reservarTurno que permite elegir día y hora
        LocalDateTime fechaDisponible = buscarTurnosLibresPrevios(turno.getDoctor(), horaInicio, horaFin);
        if (fechaDisponible != null) {
            turno.setFechaHora(fechaDisponible);
            turno.setOcupado(true);
            turnoRepo.save(turno);
            return mapearDTO(turno);
        } else {

            // Busco la fecha y hora del último turno del doctor y los horarios de inicio y fin
            LocalDateTime ultimaFecha = turnoRepo.findUltimaFechaTurnoByDoctor(turno.getDoctor());


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
    public List<TurnoResponseDTO> getTurnos() {
        List<TurnoResponseDTO> listaTurnos = turnoRepo.findAllOrdenadosPorFecha().stream().map(this::mapearDTO).toList();
        if (listaTurnos.isEmpty()) {
            return List.of(llenarMensajeError("No hay turnos cargados"));
        }
        return listaTurnos;
    }

    @Override
    public TurnoResponseDTO getTurnoById(long id) {
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
    public List<TurnoResponseDTO> getTurnosCancelados() {
        List<TurnoResponseDTO> listaTurnos = turnoRepo.findTurnosCanceladosDesdeFecha().stream().map(this::mapearDTO).toList();
        if (listaTurnos.isEmpty()) {
            return List.of(llenarMensajeError("No hay turnos cancelados"));
        }
        return listaTurnos;
    }

    @Override
    public TurnoResponseDTO reservarTurno(Turno turno) {
        Optional<Turno> estaDisponible = turnoRepo.findTurnoDisponiblePorDoctoryFecha(turno.getDoctor(), turno.getFechaHora());

        // Si estaDisponible es null, seteo ocupado true al turno que viene por parámetro y lo guardo
        if (estaDisponible.isEmpty()) {
            turno.setOcupado(true);
            turnoRepo.save(turno);
            return mapearDTO(turno);
        }

        // Veo si el horario elegido está dentro del horario del doctor
        LocalTime horaInicio = doctorService.getHorarioInicio(turno.getDoctor());
        LocalTime horaFin = doctorService.getHorarioFin(turno.getDoctor());
        if (!validarHorario(turno.getFechaHora(), horaInicio, horaFin)) {
            return llenarMensajeError("El profesional no atiende en el horario elegido");
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

    private LocalDateTime buscarTurnosLibresPrevios(Doctor doctor, LocalTime horaInicio, LocalTime horaFin) {
        //Obtengo la lista de turnos del doctor elegido con fecha mayor igual a la actual
        List<Turno> listaTurnos = turnoRepo.findTurnosPorDoctor(doctor);

        //Saco la lista de fecha y hora de la lista de turnos
        List<LocalDateTime> listaFechas = listaTurnos.stream().map(Turno::getFechaHora).toList();
        LocalDateTime fechaCandidata;


        //Reviso con un for la diferencia de fecha y hora entre un turno asignado y el siguiente
        for (int i = 0; i < listaFechas.size() - 1; i++) {
            LocalDateTime fechaPresente = listaTurnos.get(i).getFechaHora();
            LocalDateTime fechaSiguiente = listaTurnos.get(i + 1).getFechaHora();
            Duration diferencia = Duration.between(fechaPresente, fechaSiguiente);

            //Reviso si la diferencia entre las horas es mayor a 30 minutos y si el horario 30 minutos más tarde de fechaPresente es válido
            if (diferencia.toMinutes() > 30 && validarHorario(fechaPresente.plusMinutes(30), horaInicio, horaFin)) {
                fechaCandidata = siguienteTurno(fechaPresente, horaInicio, horaFin);
                //revisa si la fecha candidata no existe en la lista de fechas y horas
                if (!listaFechas.contains(fechaCandidata)) {
                    return fechaCandidata;
                }
            }
            //Reviso si la diferecia entre horas es mayor a 30 minutos y si hay diferencia mayor a 1 día
            if (diferencia.toMinutes() > 30 && diferencia.toDays() > 1) {
                fechaCandidata = siguienteTurno(fechaPresente, horaInicio, horaFin);
                if (!listaFechas.contains(fechaCandidata)) {

                    return fechaCandidata;
                }
            }
        }
        return null;
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

        if (horaInicio.isBefore(horaFin)) {
            horaPrimerTurno = horaInicio;
        } else {
            horaPrimerTurno = horaFin;
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

        // Si el día siguiente cae sábado o domingo, pasa para el lunes
        DayOfWeek dia = siguienteFecha.getDayOfWeek();
        if (dia == DayOfWeek.SATURDAY) {
            siguienteFecha = LocalDateTime.of(siguienteFecha.toLocalDate().plusDays(2), horaInicio);
        } else if (dia == DayOfWeek.SUNDAY) {
            siguienteFecha = LocalDateTime.of(siguienteFecha.toLocalDate().plusDays(1), horaFin);
        }
        return siguienteFecha;
    }

    private TurnoResponseDTO mapearDTO(Turno turno) {
        TurnoResponseDTO turnoDTO = new TurnoResponseDTO();

        turnoDTO.setId(turno.getId());
        turnoDTO.setPaciente(pacienteService.getPacienteById(turno.getPaciente().getId()));
        turnoDTO.setDoctor(doctorService.getDoctorById(turno.getDoctor().getId()));
        turnoDTO.setFecha(turno.getFechaHora().toLocalDate());
        turnoDTO.setHora(turno.getFechaHora().toLocalTime());
        turnoDTO.setOcupado(turno.isOcupado());
        return turnoDTO;
    }

    private TurnoResponseDTO llenarMensajeError(String mensajeError) {
        TurnoResponseDTO errorDTO = new TurnoResponseDTO();
        errorDTO.setId(null);
        errorDTO.setOcupado(null);
        errorDTO.setMensajeError(mensajeError);
        return errorDTO;
    }

    private boolean validarHorario(LocalDateTime fechaHoraElegida, LocalTime horaInicio, LocalTime horaFin) {
        LocalTime horaElegida = fechaHoraElegida.toLocalTime();
        return !horaElegida.isBefore(horaInicio) && !horaElegida.isAfter(horaFin);
    }
}


