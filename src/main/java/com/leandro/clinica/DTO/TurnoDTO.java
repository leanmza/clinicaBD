package com.leandro.clinica.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TurnoDTO {

        private long id;
        private LocalDate fecha;
        private LocalTime hora;
        private boolean ocupado;
        private PacienteDTO paciente;
        private DoctorDTO doctor;
        private String mensajeError;
}
