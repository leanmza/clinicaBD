/*
package com.leandro.clinica.DTO;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacienteRequestDTO {

    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "Campo obligatorio")
    private String nombre;

    @NotNull(message = "El apellido no puede ser nulo")
    @NotBlank(message = "Campo obligatorio")
    @Size(min = 3)
    private String apellido;

    @NotNull(message = "El celular no puede ser nulo")
    @NotBlank(message = "Campo obligatorio")
    @Size(min = 10, max = 11)
    private String celular;

    @NotNull(message = "El email no puede ser nulo")
    @NotBlank(message = "Campo obligatorio")
    private String email;
}
*/
