package com.example.evaluacion.controllers.auth;

import com.example.evaluacion.entity.usuarios;
import com.example.evaluacion.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping({ "/login", "/login.html" })
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas");
        }
        return "login";
    }

    // Spring Security maneja la autenticación automáticamente en /auth

    @GetMapping({ "/registro", "/registro.html" })
    public String showRegistroPage() {
        return "registro";
    }

    @PostMapping("/registro")
    public String processRegistro(@RequestParam("firstName") String firstName,
                                  @RequestParam("lastName") String lastName,
                                  @RequestParam("email") String email,
                                  @RequestParam("password") String password,
                                  @RequestParam("confirmPassword") String confirmPassword,
                                  Model model) {
        // Mantener valores del formulario en caso de error
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("email", email);

        if (password == null || !password.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "registro";
        }

        usuarios existing = usuarioService.obtenerUsuarioPorEmail(email);
        if (existing != null) {
            model.addAttribute("error", "Ya existe un usuario registrado con ese correo.");
            return "registro";
        }

        usuarios u = new usuarios();
        u.setNombre(firstName);
        u.setApellido(lastName);
        u.setEmail(email);
        u.setPassword(password); // Sin encriptar para mantener compatibilidad

        usuarioService.guardarUsuario(u);
        // Redirigir al login para que el usuario inicie sesión
        return "redirect:/login";
    }

    @GetMapping({ "/form-crear", "/form-crear.html" })
    public String crearLibro() {
        return "redirect:/libros/crear";
    }
}
