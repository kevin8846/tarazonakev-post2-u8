package com.universidad.estudiantes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA Estudiante.
 * Lado INVERSO de la relación @ManyToMany con Curso.
 *
 * @author Andres Felipe Jimenez Ramirez
 * Programación Web - Unidad 8 - Post-Contenido 2
 */
@Entity
@Table(name = "estudiantes")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser una dirección válida")
    @Column(name = "correo", nullable = false, unique = true, length = 150)
    private String correo;

    @NotBlank(message = "La carrera es obligatoria")
    @Size(min = 2, max = 100, message = "La carrera debe tener entre 2 y 100 caracteres")
    @Column(name = "carrera", nullable = false, length = 100)
    private String carrera;

    // ── Lado INVERSO de la relación ManyToMany ──
    // mappedBy apunta al campo "estudiantes" de la entidad Curso (lado propietario)
    // Este lado NO define @JoinTable — los cambios siempre se hacen desde Curso
    @ManyToMany(mappedBy = "estudiantes")
    private Set<Curso> cursos = new HashSet<>();

    // Constructor vacío requerido por JPA
    public Estudiante() {}

    public Estudiante(String nombre, String apellido, String correo, String carrera) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.carrera = carrera;
    }

    // ── Getters y Setters ──

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public Set<Curso> getCursos() { return cursos; }

    @Override
    public String toString() {
        return "Estudiante{id=" + id + ", nombre='" + nombre + "', apellido='" + apellido + "'}";
    }
}
