package com.leandro.clinica.model;

import jakarta.persistence.*;
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

    private String nombre;
    private String apellido;
    private String email;
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
