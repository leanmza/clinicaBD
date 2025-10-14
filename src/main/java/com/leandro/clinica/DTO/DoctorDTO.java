package com.leandro.clinica.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private long id;
    private String nombre;
    private String apellido;
    private String celular;
    private String email;
    private String especialidad;
}
