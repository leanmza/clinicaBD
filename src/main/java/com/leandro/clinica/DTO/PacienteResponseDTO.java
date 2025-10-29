package com.leandro.clinica.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PacienteResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String celular;
    private String email;
    private String mensajeError;
}

