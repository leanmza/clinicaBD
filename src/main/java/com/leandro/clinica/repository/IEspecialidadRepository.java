package com.leandro.clinica.repository;

import com.leandro.clinica.DTO.EspecialidadDTO;
import com.leandro.clinica.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEspecialidadRepository extends JpaRepository<Especialidad, Long> {

    @Query("SELECT e " +
            "FROM Especialidad e " +
            "WHERE e.nombre = :nombre")
    Especialidad findEspecialidadByNombre(@Param("nombre") String nombre);
}
