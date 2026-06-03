package com.universidad.estudiantes.repository;

import com.universidad.estudiantes.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    /**
     * Trae todos los cursos con sus estudiantes en UNA sola consulta (evita N+1).
     * LEFT JOIN FETCH garantiza que cursos sin estudiantes también aparecen.
     */
    @Query("SELECT c FROM Curso c LEFT JOIN FETCH c.estudiantes")
    List<Curso> findAllConEstudiantes();

    /**
     * Trae un curso específico con sus estudiantes en una sola consulta.
     */
    @Query("SELECT c FROM Curso c LEFT JOIN FETCH c.estudiantes WHERE c.id = :id")
    Optional<Curso> findByIdConEstudiantes(Long id);
}
