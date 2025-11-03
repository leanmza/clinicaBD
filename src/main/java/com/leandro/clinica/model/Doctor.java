package com.leandro.clinica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id", unique = true)
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "especialidad_id",nullable = false)
    private Especialidad especialidad;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Turno> listaTurnos;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @JoinColumn(name = "horarios_id", nullable = false)
    private Horarios horarios;

}
