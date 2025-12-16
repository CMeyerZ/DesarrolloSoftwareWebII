package com.example.evaluacion.controllers.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
import org.springframework.ui.Model;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import com.example.evaluacion.service.UsuarioService;
import com.example.evaluacion.entity.usuarios;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/profile")
    public String profile() {
        return "user/profile";
    }

    @GetMapping("/settings")
    public String settings() {
        return "user/settings";
    }

    // CRUD USUARIOS
    @GetMapping("/create")
    public String createUser() {
        return "user/create";
    }

    @PostMapping("/create")
    public String createUserPost(usuarios user) {
        // Guardar usuario y redirigir al listado
        usuarioService.guardarUsuario(user);
        return "redirect:/users/list";
    }

    @GetMapping("/read")
    public String readUserByEmail(@RequestParam(name = "email", required = false) String email, Model model) {
        if (email != null && !email.isBlank()) {
            usuarios u = usuarioService.obtenerUsuarioPorEmail(email);
            model.addAttribute("user", u);
        }
        return "user/profile";
    }

    @GetMapping("/list")
    public String listUsers(Model model) {
        List<usuarios> usuarios = usuarioService.obtenerTodosLosUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "user/list";
    }

    @GetMapping("/update")
    public String updateUserForm(@RequestParam(name = "email", required = false) String email, Model model) {
        if (email != null && !email.isBlank()) {
            usuarios u = usuarioService.obtenerUsuarioPorEmail(email);
            model.addAttribute("user", u);
        }
        return "user/update";
    }

    @GetMapping("/change-password")
    public String changePasswordForm(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        usuarios u = usuarioService.obtenerUsuarioPorEmail(principal.getName());
        model.addAttribute("user", u);
        return "user/update";
    }

    @PostMapping("/change-password")
    public String changePasswordPost(@RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Principal principal,
            Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        
        usuarios u = usuarioService.obtenerUsuarioPorEmail(principal.getName());
        if (u == null) {
            return "redirect:/login";
        }

        if (currentPassword == null || !currentPassword.equals(u.getPassword())) {
            model.addAttribute("error", "Contraseña actual incorrecta");
            model.addAttribute("user", u);
            return "user/update";
        }

        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            model.addAttribute("user", u);
            return "user/update";
        }

        u.setPassword(newPassword);
        usuarioService.guardarUsuario(u);

        // Redirigir al perfil del usuario
        String emailEnc = URLEncoder.encode(u.getEmail(), StandardCharsets.UTF_8);
        return "redirect:/users/read?email=" + emailEnc;
    }
}
