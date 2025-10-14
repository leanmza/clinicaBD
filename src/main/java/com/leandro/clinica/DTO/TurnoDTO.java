package com.leandro.clinica.DTO;

import com.leandro.clinica.model.Doctor;
import com.leandro.clinica.model.Paciente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class TurnoDTO {


        private long id;

        private LocalDateTime fechaHora;
        private boolean ocupado;
        private PacienteDTO paciente;
        private DoctorDTO doctor;
}
