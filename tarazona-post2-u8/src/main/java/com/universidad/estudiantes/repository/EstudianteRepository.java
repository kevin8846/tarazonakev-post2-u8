package com.universidad.estudiantes.repository;

import com.universidad.estudiantes.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    /**
     * Consulta derivada: buscar estudiantes por carrera (sin distinción de mayúsculas)
     */
    List<Estudiante> findByCarreraIgnoreCase(String carrera);

    /**
     * Consulta derivada: búsqueda parcial por nombre o apellido
     */
    List<Estudiante> findByNombreContainingOrApellidoContaining(
            String nombre, String apellido);

    /**
     * Verificar si ya existe un correo registrado (para validación de duplicados)
     */
    Optional<Estudiante> findByCorreo(String correo);

    /**
     * Verificar si existe un correo en uso por otro estudiante distinto al actual
     */
    boolean existsByCorreoAndIdNot(String correo, Long id);
}
