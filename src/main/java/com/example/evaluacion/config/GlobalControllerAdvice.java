package com.example.evaluacion.config;

import com.example.evaluacion.entity.usuarios;
import com.example.evaluacion.service.UsuarioService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UsuarioService usuarioService;

    public GlobalControllerAdvice(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ModelAttribute
    public void addAuthenticatedUser(Model model, Principal principal) {
        if (principal != null) {
            usuarios usuario = usuarioService.obtenerUsuarioPorEmail(principal.getName());
            if (usuario != null) {
                model.addAttribute("authenticatedUser", usuario);
            }
        }
    }
}
