package com.leandro.clinica.service;

import com.leandro.clinica.model.Horarios;
import com.leandro.clinica.repository.IHorariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class HorariosService implements  IHorariosService{

    @Autowired
    private IHorariosRepository horariosRepo;

    @Override
    public Horarios getHorariosPorRango(LocalTime horaInicio, LocalTime horaFin) {
        Horarios horario = horariosRepo.getHorarioByRange(horaInicio, horaFin);
        return horario;
    }

    @Override
    public void createHorario(Horarios horario) {
        horariosRepo.save(horario);

    }
}
