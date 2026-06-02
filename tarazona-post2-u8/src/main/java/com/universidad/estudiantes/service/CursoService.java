package com.universidad.estudiantes.service;

import com.universidad.estudiantes.model.Curso;
import com.universidad.estudiantes.model.Estudiante;
import com.universidad.estudiantes.repository.CursoRepository;
import com.universidad.estudiantes.repository.EstudianteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio con la lógica de negocio para Curso.
 * Gestiona inscripciones y desinscripciones usando helper methods
 * para mantener la consistencia de la relación bidireccional.
 *
 * @author Andres Felipe Jimenez Ramirez
 */
@Service
public class CursoService {

    private final CursoRepository cursoRepo;
    private final EstudianteRepository estudianteRepo;

    // Inyección por constructor
    public CursoService(CursoRepository cursoRepo, EstudianteRepository estudianteRepo) {
        this.cursoRepo = cursoRepo;
        this.estudianteRepo = estudianteRepo;
    }

    /**
     * Lista todos los cursos con sus estudiantes (JOIN FETCH, sin N+1).
     */
    public List<Curso> listarTodos() {
        return cursoRepo.findAllConEstudiantes();
    }

    /**
     * Busca un curso por ID cargando sus estudiantes (JOIN FETCH).
     */
    public Curso buscarPorId(Long id) {
        return cursoRepo.findByIdConEstudiantes(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado: " + id));
    }

    /**
     * Guarda un curso nuevo o actualiza uno existente.
     */
    @Transactional
    public Curso guardar(Curso curso) {
        return cursoRepo.save(curso);
    }

    /**
     * Elimina un curso por ID.
     */
    @Transactional
    public void eliminar(Long id) {
        Curso curso = buscarPorId(id);
        // Limpiar relación bidireccional antes de eliminar
        for (Estudiante e : curso.getEstudiantes()) {
            e.getCursos().remove(curso);
        }
        curso.getEstudiantes().clear();
        cursoRepo.delete(curso);
    }

    /**
     * Inscribe un estudiante en un curso.
     * Usa el helper method agregarEstudiante() para sincronizar ambos lados.
     */
    @Transactional
    public void inscribirEstudiante(Long cursoId, Long estudianteId) {
        Curso curso = buscarPorId(cursoId);
        Estudiante estudiante = estudianteRepo.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado: " + estudianteId));

        // Verificar si ya está inscrito
        if (curso.getEstudiantes().contains(estudiante)) {
            throw new IllegalStateException("El estudiante ya está inscrito en este curso.");
        }

        curso.agregarEstudiante(estudiante); // sincroniza ambos lados
        cursoRepo.save(curso);
    }

    /**
     * Desinscribe un estudiante de un curso.
     * Usa el helper method quitarEstudiante() para sincronizar ambos lados.
     */
    @Transactional
    public void desinscribirEstudiante(Long cursoId, Long estudianteId) {
        Curso curso = buscarPorId(cursoId);
        Estudiante estudiante = estudianteRepo.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado: " + estudianteId));

        curso.quitarEstudiante(estudiante); // sincroniza ambos lados
        cursoRepo.save(curso);
    }
}
