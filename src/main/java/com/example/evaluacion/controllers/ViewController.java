package com.example.evaluacion.controllers;

import com.example.evaluacion.entity.libros;
import com.example.evaluacion.entity.usuarios;
import com.example.evaluacion.service.LibrosService;
import com.example.evaluacion.service.UsuarioService;
import java.util.List;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final LibrosService librosService;
    private final UsuarioService usuarioService;

    public ViewController(LibrosService librosService, UsuarioService usuarioService) {
        this.librosService = librosService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/index")
    public String showIndex(Model model, Principal principal) {
        if (principal != null) {
            String userEmail = principal.getName();
            usuarios usuario = usuarioService.obtenerUsuarioPorEmail(userEmail);
            model.addAttribute("usuario", usuario);
            
            List<libros> lista = librosService.obtenerLibrosPorUsuario(userEmail);
            int total = lista == null ? 0 : lista.size();
            long read = 0;
            long todo = 0;
            if (lista != null) {
                read = lista.stream()
                        .filter(l -> l.getEstado() != null && l.getEstado().equalsIgnoreCase("leido"))
                        .count();
                todo = lista.stream()
                        .filter(l -> l.getEstado() == null || !l.getEstado().equalsIgnoreCase("leido"))
                        .count();
            }
            model.addAttribute("totalCount", total);
            model.addAttribute("readCount", read);
            model.addAttribute("todoCount", todo);
        }
        return "index";
    }
}
