package com.leandro.clinica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paciente_id", unique = true)
    private long id;

    @Size(max = 50)
    @NotNull
    private String nombre;
    @Size(max = 50)
    @NotNull
    private String apellido;
    @Size(max = 50)
    @NotNull
    private String email;
    @Size(max = 15)
    @NotNull
    private String celular;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Turno> listaTurnos;
}
