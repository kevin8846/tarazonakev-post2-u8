#  tarazona martinez kevin alejandro -post2-u8

**Programación Web — Unidad 8: Persistencia con JPA/Hibernate**  
**Post-Contenido 2 — Relación @ManyToMany entre Curso y Estudiante**  
**Autor:** tarazona martinez kevin alejandro  
**Universidad Francisco de Paula Santander — 2026**

---

## Descripción

Extiende el proyecto del Post-Contenido 1 agregando la entidad `Curso` y configurando una relación **bidireccional @ManyToMany** con `Estudiante`. Hibernate genera automáticamente la tabla de unión `curso_estudiante`. Se aplica **JOIN FETCH** en el repositorio para resolver el problema N+1.

---

## Diagrama ER de la relación @ManyToMany

```
+----------------+        +--------------------+        +----------------+
|   estudiantes  |        |  curso_estudiante  |        |     cursos     |
+----------------+        +--------------------+        +----------------+
| PK id          |<-------| FK estudiante_id   |        | PK id          |
|    nombre      |        | FK curso_id        |------->|    nombre      |
|    apellido    |        +--------------------+        |    creditos    |
|    correo      |                                      +----------------+
|    carrera     |
+----------------+

Cardinalidad: N:M
- Un estudiante puede inscribirse en muchos cursos
- Un curso puede tener muchos estudiantes inscritos
```

**Configuración JPA:**
- `Curso` → lado **PROPIETARIO** → define `@JoinTable(name = "curso_estudiante")`
- `Estudiante` → lado **INVERSO** → usa `@ManyToMany(mappedBy = "estudiantes")`

---

## Tecnologías

| Tecnología      | Versión  |
|-----------------|----------|
| Java            | 17       |
| Spring Boot     | 3.2.5    |
| Spring Data JPA | 3.2.5    |
| Hibernate       | 6.x      |
| MySQL           | 8.x      |
| Thymeleaf       | 3.1.x    |

---

## Estructura del proyecto

```
src/main/java/com/universidad/estudiantes/
├── EstudiantesApplication.java
├── controller/
│   ├── EstudianteController.java     ← CRUD estudiantes (del post1)
│   └── CursoController.java          ← CRUD cursos + inscripciones (nuevo)
├── model/
│   ├── Estudiante.java               ← Actualizado: lado inverso @ManyToMany
│   └── Curso.java                    ← Nuevo: lado propietario + helper methods
├── repository/
│   ├── EstudianteRepository.java
│   └── CursoRepository.java          ← Nuevo: JOIN FETCH para evitar N+1
└── service/
    ├── EstudianteService.java
    └── CursoService.java             ← Nuevo: inscribir/desinscribir
```

---

## Configuración MySQL

```bash
# MySQL ya debe estar corriendo (del post1)
# Misma base de datos: estudiantes_db
# Hibernate crea automáticamente las nuevas tablas al iniciar
```

## Ejecución

```bash
cd jimenez-post2-u8
./mvnw spring-boot:run
```

- Estudiantes: http://localhost:8080/estudiantes
- Cursos: http://localhost:8080/cursos

---

## Tablas generadas por Hibernate

```sql
-- Verificar en MySQL:
USE estudiantes_db;
SHOW TABLES;
-- estudiantes, cursos, curso_estudiante

DESCRIBE curso_estudiante;
-- curso_id    bigint  NO  MUL
-- estudiante_id bigint NO MUL

SELECT * FROM curso_estudiante;
-- Muestra las inscripciones activas
```

---




