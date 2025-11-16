package com.example.evaluacion.controllers.libros;

import com.example.evaluacion.entity.libros;
import com.example.evaluacion.service.LibrosService;
import com.example.evaluacion.service.UsuarioService;
import com.example.evaluacion.entity.usuarios;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/libros")
public class LibrosController {

    private final LibrosService librosService;

    public LibrosController(LibrosService librosService) {
        this.librosService = librosService;
    }

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({"/list", ""})
    public String listarLibros(Model model) {
        // Mostrar sólo los libros del usuario logueado
        return "redirect:/libros/user";
    }

    @GetMapping("/crear")
    public String crearForm(Model model, HttpSession session) {
        usuarios sessionUser = (usuarios) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("book", new libros());
        return "forms/form-crear";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute("book") libros book, HttpSession session, Model model) {
        usuarios sessionUser = (usuarios) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        System.out.println("[DEBUG] crear() invoked. session=" + (sessionUser!=null?sessionUser.getEmail():"null") + ", isbn=" + book.getIsbn() + ", titulo=" + book.getTitulo());
        // Normalizar y validar ISBN único
        String isbn = normalizeIsbn(book.getIsbn());
        book.setIsbn(isbn);
        // Validar longitud 13 dígitos
        if (isbn == null || isbn.length() != 13) {
            model.addAttribute("error", "El ISBN debe tener exactamente 13 caracteres numéricos después de normalizar (sin guiones ni espacios).");
            model.addAttribute("book", book);
            return "forms/form-crear";
        }
        if (isbn != null && !isbn.isBlank()) {
            libros existing = librosService.obtenerLibroPorId(isbn);
            if (existing != null) {
                model.addAttribute("error", "El ISBN ya existe. No se puede crear un libro con el mismo ISBN.");
                model.addAttribute("book", book);
                return "forms/form-crear";
            }
        }
        // Obtener usuario actual desde la BD para estar seguro
        usuarios u = usuarioService.obtenerUsuarioPorEmail(sessionUser.getEmail());
        if (u != null) {
            // Crear una nueva entidad para evitar problemas de estado de la entidad ligada al binding
            libros toSave = new libros();
            toSave.setIsbn(isbn);
            toSave.setTitulo(book.getTitulo());
            toSave.setAutor(book.getAutor());
            toSave.setCategoria(book.getCategoria());
            toSave.setPaginas(book.getPaginas());
            toSave.setEstado(book.getEstado());
            toSave.setFechaInicio(book.getFechaInicio());
            toSave.setFechaFin(book.getFechaFin());
            toSave.setResena(book.getResena());
            toSave.setRating(book.getRating());
            toSave.setOwner(u);
            librosService.guardarLibro(toSave);
        } else {
            // fallback: intentar guardar el objeto recibido (sin owner)
            librosService.guardarLibro(book);
        }
        return "redirect:/libros/user";
    }

    @GetMapping("/editar")
    public String editarForm(@RequestParam("isbn") String isbn, Model model, HttpSession session) {
        usuarios sessionUser = (usuarios) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        String normIsbn = normalizeIsbn(isbn);
        libros book = null;
        if (normIsbn != null) {
            book = librosService.obtenerLibroPorId(normIsbn);
        }
        // ISBN sin normalizar (alternativa)
        if (book == null) {
            book = librosService.obtenerLibroPorId(isbn);
        }
        if (book == null) {
            return "redirect:/libros/user";
        }
        if (book.getOwner() == null || !sessionUser.getEmail().equals(book.getOwner().getEmail())) {
            // intentar acceder a libro de otro usuario -> denegar
            return "redirect:/libros/user";
        }
        model.addAttribute("book", book);
        return "forms/form-editar";
    }

    @PostMapping("/editar")
    public String editar(libros book, @RequestParam("originalIsbn") String originalIsbn, HttpSession session, Model model) {
        usuarios sessionUser = (usuarios) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        // normalizar ISBNs
        String normOriginal = normalizeIsbn(originalIsbn);
        String newIsbn = normalizeIsbn(book.getIsbn());
        book.setIsbn(newIsbn);

        // Cargar libro existente por el ISBN original y comprobar dueño
        libros existing = null;
        if (normOriginal != null) {
            existing = librosService.obtenerLibroPorId(normOriginal);
        }
        if (existing == null) {
            existing = librosService.obtenerLibroPorId(originalIsbn);
        }
        if (existing == null) {
            return "redirect:/libros/list";
        }
        if (existing.getOwner() == null || !sessionUser.getEmail().equals(existing.getOwner().getEmail())) {
            return "redirect:/libros/list";
        }

        // Si cambiaron el ISBN, validar longitud y que no exista ya
        if (newIsbn != null && !newIsbn.isBlank() && !normOriginal.equals(newIsbn)) {
            if (newIsbn.length() != 13) {
                model.addAttribute("error", "El ISBN debe tener exactamente 13 caracteres numéricos después de normalizar (sin guiones ni espacios).");
                model.addAttribute("book", book);
                return "forms/form-editar";
            }
            libros conflict = librosService.obtenerLibroPorId(newIsbn);
            if (conflict != null) {
                model.addAttribute("error", "El ISBN ya existe. Elige otro ISBN.");
                model.addAttribute("book", book);
                return "forms/form-editar";
            }
            // Crear nuevo registro con el nuevo ISBN y eliminar el antiguo
            libros nuevo = new libros();
            nuevo.setIsbn(newIsbn);
            nuevo.setTitulo(book.getTitulo());
            nuevo.setAutor(book.getAutor());
            nuevo.setCategoria(book.getCategoria());
            nuevo.setPaginas(book.getPaginas());
            nuevo.setEstado(book.getEstado());
            nuevo.setFechaInicio(book.getFechaInicio());
            nuevo.setFechaFin(book.getFechaFin());
            nuevo.setResena(book.getResena());
            nuevo.setRating(book.getRating());
            // Conservar dueño
            nuevo.setOwner(existing.getOwner());
            librosService.guardarLibro(nuevo);
            // Intenta eliminar el registro antiguo utilizando los formatos normalizados y sin procesar
            if (normOriginal != null) {
                librosService.eliminarLibro(normOriginal);
            }
            librosService.eliminarLibro(originalIsbn);
            return "redirect:/libros/user";
        }

        // ISBN sin cambios -> actualizar la entidad existente
        existing.setTitulo(book.getTitulo());
        existing.setAutor(book.getAutor());
        existing.setCategoria(book.getCategoria());
        existing.setPaginas(book.getPaginas());
        existing.setEstado(book.getEstado());
        existing.setFechaInicio(book.getFechaInicio());
        existing.setFechaFin(book.getFechaFin());
        existing.setResena(book.getResena());
        existing.setRating(book.getRating());
        librosService.guardarLibro(existing);
        return "redirect:/libros/user";
    }

    @GetMapping("/user")
    public String listarPorUsuario(Model model, HttpSession session) {
        usuarios sessionUser = (usuarios) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        List<libros> lista = librosService.obtenerLibrosPorUsuario(sessionUser.getEmail());
        model.addAttribute("libros", lista);
        return "forms/list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("isbn") String isbn, HttpSession session) {
        usuarios sessionUser = (usuarios) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        String normIsbn = normalizeIsbn(isbn);
        libros book = null;
        if (normIsbn != null) {
            book = librosService.obtenerLibroPorId(normIsbn);
        }
        if (book == null) {
            book = librosService.obtenerLibroPorId(isbn);
        }
        if (book != null && book.getOwner() != null && sessionUser.getEmail().equals(book.getOwner().getEmail())) {
            // Eliminar por ISBN normalizado si es posible, de lo contrario por el valor sin procesar
            if (normIsbn != null) {
                librosService.eliminarLibro(normIsbn);
            } else {
                librosService.eliminarLibro(isbn);
            }
        }
        return "redirect:/libros/list";
    }

    // Normaliza un ISBN: elimina caracteres que no sean letras/dígitos y convierte a mayúsculas
    private String normalizeIsbn(String raw) {
        if (raw == null) return null;
        String cleaned = raw.replaceAll("[^A-Za-z0-9]", "");
        if (cleaned.isBlank()) return null;
        return cleaned.toUpperCase();
    }
}
