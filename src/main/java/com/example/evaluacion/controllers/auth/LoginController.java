package com.example.evaluacion.controllers.auth;

import com.example.evaluacion.entity.usuarios;
import com.example.evaluacion.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
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
    public String showLoginPage(@RequestParam(value = "redirect", required = false) String redirect, Model model) {
        if (redirect != null) {
            model.addAttribute("redirect", redirect);
        }
        return "login";
    }

    @PostMapping("/auth")
    public String processLogin(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam(value = "redirect", required = false) String redirect,
                               HttpSession session,
                               Model model) {
        usuarios u = usuarioService.obtenerUsuarioPorEmail(email);
        if (u != null && u.getPassword() != null && u.getPassword().equals(password)) {
            session.setAttribute("user", u);
            // Redirigir a la página de origen si existe
            if (redirect != null && !redirect.isBlank() && redirect.startsWith("/")) {
                return "redirect:" + redirect;
            }
            return "redirect:/index";
        }
        model.addAttribute("error", "Credenciales inválidas");
        return "login";
    }

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
                                  HttpSession session,
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
        u.setPassword(password);

        usuarioService.guardarUsuario(u);
        session.setAttribute("user", u);
        return "redirect:/index";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index";
    }

    @GetMapping({ "/form-crear", "/form-crear.html" })
    public String crearLibro() {
        return "redirect:/libros/crear";
    }
}
