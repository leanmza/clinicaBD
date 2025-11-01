package com.leandro.clinica.service;

import com.leandro.clinica.model.Horarios;

import java.time.LocalTime;

public interface IHorariosService {

    Horarios getHorariosPorRango(LocalTime horaInicio, LocalTime horaFin);

    void createHorario(Horarios horario);


}
