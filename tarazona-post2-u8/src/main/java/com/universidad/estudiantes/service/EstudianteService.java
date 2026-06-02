package com.universidad.estudiantes.service;

import com.universidad.estudiantes.model.Estudiante;
import com.universidad.estudiantes.repository.EstudianteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio con la lógica de negocio para la entidad Estudiante.
 * Maneja las transacciones y validaciones de negocio adicionales.
 *
 * @author Andres Felipe Jimenez Ramirez
 */
@Service
public class EstudianteService {

    private final EstudianteRepository repo;

    // Inyección por constructor (buena práctica: sin @Autowired)
    public EstudianteService(EstudianteRepository repo) {
        this.repo = repo;
    }

    /**
     * Obtiene todos los estudiantes ordenados por apellido
     */
    public List<Estudiante> listarTodos() {
        return repo.findAll();
    }

    /**
     * Busca un estudiante por su ID.
     * Lanza RuntimeException si no existe (se puede personalizar con excepción propia)
     */
    public Estudiante buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Estudiante no encontrado con ID: " + id));
    }

    /**
     * Guarda un estudiante nuevo o actualiza uno existente.
     * Valida que el correo no esté duplicado en la base de datos.
     */
    @Transactional
    public Estudiante guardar(Estudiante estudiante) {
        // Validación de correo duplicado a nivel de servicio
        if (estudiante.getId() == null) {
            // Nuevo estudiante: verificar que el correo no exista
            if (repo.findByCorreo(estudiante.getCorreo()).isPresent()) {
                throw new IllegalArgumentException(
                        "Ya existe un estudiante con el correo: " + estudiante.getCorreo());
            }
        } else {
            // Editar estudiante: verificar que el correo no esté usado por otro
            if (repo.existsByCorreoAndIdNot(estudiante.getCorreo(), estudiante.getId())) {
                throw new IllegalArgumentException(
                        "El correo ya está en uso por otro estudiante.");
            }
        }
        return repo.save(estudiante);
    }

    /**
     * Elimina un estudiante por su ID.
     */
    @Transactional
    public void eliminar(Long id) {
        // Verificar que existe antes de eliminar
        buscarPorId(id);
        repo.deleteById(id);
    }

    /**
     * Busca estudiantes por carrera (sin distinción de mayúsculas)
     */
    public List<Estudiante> buscarPorCarrera(String carrera) {
        return repo.findByCarreraIgnoreCase(carrera);
    }

    /**
     * Búsqueda por nombre o apellido (parcial)
     */
    public List<Estudiante> buscar(String termino) {
        return repo.findByNombreContainingOrApellidoContaining(termino, termino);
    }
}
