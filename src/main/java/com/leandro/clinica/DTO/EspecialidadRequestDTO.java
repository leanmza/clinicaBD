package com.leandro.clinica.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EspecialidadRequestDTO {
    
    @NotBlank(message = "El nombre de la especialidad es obligatorio")
    private String nombreEspecialidad;
}




