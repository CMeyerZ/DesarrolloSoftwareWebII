package com.example.evaluacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import java.time.LocalDate;

@Controller
public class LoginController {

    @GetMapping({"/login", "/login.html"})
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/auth")
    public String processLogin() {
        // Aquí iría la lógica de autenticación
        return "redirect:/index";
    }

    @GetMapping({"/registro", "/registro.html"})
    public String showRegistroPage() {
        return "registro";
    }

    @GetMapping({"/form-crear", "/form-crear.html"})
    public String crearLibro() {
        // Sirve la plantilla: src/main/resources/templates/form-crear.html
        return "form-crear";
    }

    @GetMapping({"/form-editar", "/form-editar.html"})
    public String editarLibro(Model model) {
        // Si no tienes backend aún, devolvemos un ejemplo en el modelo para que Thymeleaf no falle
        Book example = new Book(
                1L,
                "El Principito",
                "Antoine de Saint-Exupéry",
                5,
                "Ficción",
                96,
                "Un clásico para todas las edades",
                LocalDate.now().minusDays(10),
                LocalDate.now(),
                "leido",
                true,
                "clásico,infantil",
                "https://via.placeholder.com/300x420?text=Portada"
        );
        model.addAttribute("book", example);
        return "form-editar";
    }

}
