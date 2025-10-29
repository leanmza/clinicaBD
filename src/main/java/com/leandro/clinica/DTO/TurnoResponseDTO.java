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
public class TurnoResponseDTO {

        private long id;
        private LocalDate fecha;
        private LocalTime hora;
        private boolean ocupado;
        private PacienteResponseDTO paciente;
        private DoctorResponseDTO doctor;
        private String mensajeError;
}

