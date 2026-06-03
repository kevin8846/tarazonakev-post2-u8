package com.universidad.estudiantes.controller;

import com.universidad.estudiantes.model.Estudiante;
import com.universidad.estudiantes.service.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/estudiantes")
public class EstudianteController {

    private final EstudianteService service;

    public EstudianteController(EstudianteService service) {
        this.service = service;
    }

    // =========================================
    // READ - Listar todos los estudiantes
    // =========================================
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("estudiantes", service.listarTodos());
        return "estudiantes/lista";
    }

    // =========================================
    // CREATE - Mostrar formulario nuevo
    // =========================================
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        model.addAttribute("titulo", "Nuevo Estudiante");
        return "estudiantes/formulario";
    }

    // =========================================
    // CREATE / UPDATE - Guardar (POST)
    // =========================================
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Estudiante estudiante,
                          BindingResult result,
                          Model model,
                          RedirectAttributes flash) {

        // Si hay errores de validación, volver al formulario
        if (result.hasErrors()) {
            model.addAttribute("titulo",
                    estudiante.getId() == null ? "Nuevo Estudiante" : "Editar Estudiante");
            return "estudiantes/formulario";
        }

        // Intentar guardar con validación de negocio (correo duplicado)
        try {
            service.guardar(estudiante);
            String msg = estudiante.getId() == null
                    ? "Estudiante creado exitosamente."
                    : "Estudiante actualizado exitosamente.";
            flash.addFlashAttribute("exito", msg);
        } catch (IllegalArgumentException e) {
            // Error de correo duplicado: volver al formulario con mensaje
            model.addAttribute("titulo",
                    estudiante.getId() == null ? "Nuevo Estudiante" : "Editar Estudiante");
            model.addAttribute("error", e.getMessage());
            return "estudiantes/formulario";
        }

        return "redirect:/estudiantes";
    }

    // =========================================
    // UPDATE - Mostrar formulario editar
    // =========================================
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("estudiante", service.buscarPorId(id));
        model.addAttribute("titulo", "Editar Estudiante");
        return "estudiantes/formulario";
    }

    // =========================================
    // DELETE - Confirmar eliminación (GET)
    // =========================================
    @GetMapping("/eliminar/{id}")
    public String confirmarEliminar(@PathVariable Long id, Model model) {
        model.addAttribute("estudiante", service.buscarPorId(id));
        return "estudiantes/confirmar-eliminar";
    }

    // =========================================
    // DELETE - Ejecutar eliminación (POST)
    // =========================================
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        service.eliminar(id);
        flash.addFlashAttribute("exito", "Estudiante eliminado correctamente.");
        return "redirect:/estudiantes";
    }

    // =========================================
    // Redirigir la raíz "/" a la lista
    // =========================================
    @GetMapping("/")
    public String inicio() {
        return "redirect:/estudiantes";
    }
}
