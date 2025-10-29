package com.leandro.clinica.repository;

import com.leandro.clinica.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEspecialidadRepository extends JpaRepository<Especialidad, Long> {

    Especialidad findEspecialidadByNombre(String nombre);
}
