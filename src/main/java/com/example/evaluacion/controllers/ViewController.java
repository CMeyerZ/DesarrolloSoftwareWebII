package com.example.evaluacion.controllers;

import com.example.evaluacion.entity.libros;
import com.example.evaluacion.entity.usuarios;
import com.example.evaluacion.service.LibrosService;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final LibrosService librosService;

    public ViewController(LibrosService librosService) {
        this.librosService = librosService;
    }

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/index")
    public String showIndex(Model model, HttpSession session) {
        usuarios sessionUser = (usuarios) session.getAttribute("user");
        if (sessionUser != null) {
            List<libros> lista = librosService.obtenerLibrosPorUsuario(sessionUser.getEmail());
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
