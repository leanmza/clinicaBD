package com.leandro.clinica.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
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

        private Long id;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        private LocalDate fecha;
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "hh:mm")
        private LocalTime hora;
        private Boolean ocupado;
        private PacienteDTO paciente;
        private DoctorDTO doctor;
        private String mensajeError;
}
