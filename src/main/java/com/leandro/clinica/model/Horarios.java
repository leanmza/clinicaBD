package com.leandro.clinica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Horarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horarios_id", unique = true)
    private long id;

    @NotNull
    private LocalTime horaInicio;
    @NotNull
    private LocalTime horaFin;

}
