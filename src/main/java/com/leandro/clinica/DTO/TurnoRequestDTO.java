package com.leandro.clinica.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurnoRequestDTO {
    
    @NotNull(message = "La fecha y hora son obligatorias")
    @Future(message = "La fecha debe ser futura")
    private LocalDateTime fechaHora;
    
    @NotNull(message = "El ID del paciente es obligatorio")
    private Long pacienteId;
    
    @NotNull(message = "El ID del doctor es obligatorio")
    private Long doctorId;
}

