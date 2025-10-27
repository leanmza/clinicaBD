package com.leandro.clinica.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoctorDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String celular;
    private String email;
    private String especialidad;
    private String mensajeError;
}
