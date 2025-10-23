package com.leandro.clinica.DTO;

import com.leandro.clinica.model.Doctor;
import com.leandro.clinica.model.Paciente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class TurnoDTO {


        private long id;

        private LocalDate fecha;
        private LocalTime hora;
        private boolean ocupado;
        private PacienteDTO paciente;
        private DoctorDTO doctor;
}
