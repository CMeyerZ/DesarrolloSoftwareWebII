package com.example.evaluacion.service;

import com.example.evaluacion.entity.libros;
import java.util.List;

public interface LibrosService {
    public libros guardarLibro(libros libro);

    public void eliminarLibro(String id);

    public libros obtenerLibroPorId(String id);

    public List<libros> obtenerTodosLosLibros();

    public List<libros> obtenerLibrosPorUsuario(String email);

}
