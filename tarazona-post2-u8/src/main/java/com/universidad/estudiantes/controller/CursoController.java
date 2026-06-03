package com.universidad.estudiantes.controller;

import com.universidad.estudiantes.model.Curso;
import com.universidad.estudiantes.service.CursoService;
import com.universidad.estudiantes.service.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;
    private final EstudianteService estudianteService;

    public CursoController(CursoService cursoService, EstudianteService estudianteService) {
        this.cursoService = cursoService;
        this.estudianteService = estudianteService;
    }

    // ── Listar cursos ──
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cursos", cursoService.listarTodos());
        return "cursos/lista";
    }

    // ── Formulario nuevo curso ──
    @GetMapping("/nuevo")
    public String mostrarNuevo(Model model) {
        model.addAttribute("curso", new Curso());
        model.addAttribute("titulo", "Nuevo Curso");
        return "cursos/formulario";
    }

    // ── Guardar curso (crear o editar) ──
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Curso curso,
                          BindingResult result,
                          Model model,
                          RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("titulo",
                    curso.getId() == null ? "Nuevo Curso" : "Editar Curso");
            return "cursos/formulario";
        }
        cursoService.guardar(curso);
        flash.addFlashAttribute("exito", "Curso guardado exitosamente.");
        return "redirect:/cursos";
    }

    // ── Formulario editar curso ──
    @GetMapping("/editar/{id}")
    public String mostrarEditar(@PathVariable Long id, Model model) {
        model.addAttribute("curso", cursoService.buscarPorId(id));
        model.addAttribute("titulo", "Editar Curso");
        return "cursos/formulario";
    }

    // ── Confirmar eliminación ──
    @GetMapping("/eliminar/{id}")
    public String confirmarEliminar(@PathVariable Long id, Model model) {
        model.addAttribute("curso", cursoService.buscarPorId(id));
        return "cursos/confirmar-eliminar";
    }

    // ── Ejecutar eliminación ──
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        cursoService.eliminar(id);
        flash.addFlashAttribute("exito", "Curso eliminado correctamente.");
        return "redirect:/cursos";
    }

    // ── Pantalla de inscripción: muestra curso + lista de todos los estudiantes ──
    @GetMapping("/{id}/inscribir")
    public String mostrarInscripcion(@PathVariable Long id, Model model) {
        model.addAttribute("curso", cursoService.buscarPorId(id));
        model.addAttribute("estudiantes", estudianteService.listarTodos());
        return "cursos/inscribir";
    }

    // ── Inscribir estudiante en curso ──
    @PostMapping("/{cursoId}/inscribir/{estudianteId}")
    public String inscribir(@PathVariable Long cursoId,
                            @PathVariable Long estudianteId,
                            RedirectAttributes flash) {
        try {
            cursoService.inscribirEstudiante(cursoId, estudianteId);
            flash.addFlashAttribute("exito", "Estudiante inscrito exitosamente.");
        } catch (IllegalStateException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cursos/" + cursoId + "/inscribir";
    }

    // ── Desinscribir estudiante de curso ──
    @PostMapping("/{cursoId}/desinscribir/{estudianteId}")
    public String desinscribir(@PathVariable Long cursoId,
                               @PathVariable Long estudianteId,
                               RedirectAttributes flash) {
        cursoService.desinscribirEstudiante(cursoId, estudianteId);
        flash.addFlashAttribute("exito", "Estudiante desinscrito correctamente.");
        return "redirect:/cursos/" + cursoId + "/inscribir";
    }
}
