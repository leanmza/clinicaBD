package com.leandro.clinica.DTO;

import lombok.*;

import java.time.LocalDate;
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
