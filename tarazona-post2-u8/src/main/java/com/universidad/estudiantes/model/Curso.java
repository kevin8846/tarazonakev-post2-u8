package com.universidad.estudiantes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del curso es obligatorio")
    @Size(min = 2, max = 150, message = "El nombre debe tener entre 2 y 150 caracteres")
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Min(value = 1, message = "Los créditos deben ser al menos 1")
    @Max(value = 10, message = "Los créditos no pueden superar 10")
    @Column(name = "creditos")
    private int creditos;

    // ── Lado PROPIETARIO de la relación ManyToMany ──
    // Define la tabla de unión "curso_estudiante" con sus claves foráneas
    @ManyToMany
    @JoinTable(
            name = "curso_estudiante",
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "estudiante_id")
    )
    private Set<Estudiante> estudiantes = new HashSet<>();

    // ── Helper methods: sincronizan AMBOS lados de la relación bidireccional ──

    /**
     * Agrega un estudiante al curso y sincroniza el lado inverso.
     * Siempre usar este método en lugar de manipular el Set directamente.
     */
    public void agregarEstudiante(Estudiante e) {
        this.estudiantes.add(e);
        e.getCursos().add(this);
    }

    /**
     * Quita un estudiante del curso y sincroniza el lado inverso.
     */
    public void quitarEstudiante(Estudiante e) {
        this.estudiantes.remove(e);
        e.getCursos().remove(this);
    }

    // Constructor vacío requerido por JPA
    public Curso() {}

    public Curso(String nombre, int creditos) {
        this.nombre = nombre;
        this.creditos = creditos;
    }

    // ── Getters y Setters ──

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }

    public Set<Estudiante> getEstudiantes() { return estudiantes; }

    @Override
    public String toString() {
        return "Curso{id=" + id + ", nombre='" + nombre + "', creditos=" + creditos + "}";
    }
}
