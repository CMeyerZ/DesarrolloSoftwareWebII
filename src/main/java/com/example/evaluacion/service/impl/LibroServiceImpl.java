package com.example.evaluacion.service.impl;

import org.springframework.stereotype.Service;
import com.example.evaluacion.service.LibrosService;
import com.example.evaluacion.entity.libros;
import jakarta.transaction.Transactional;
import java.util.List;
import com.example.evaluacion.dao.ILibrosDao;

@Service
public class LibroServiceImpl implements LibrosService {
    private final ILibrosDao libroDao;

    public LibroServiceImpl(ILibrosDao libroDao) {
        this.libroDao = libroDao;
    }

    @Transactional
    @Override
    public libros guardarLibro(libros libro) {
        return libroDao.save(libro);
    }

    @Transactional
    @Override
    public void eliminarLibro(String isbn) {
        libroDao.deleteById(isbn);
    }

    @Transactional
    @Override
    public libros obtenerLibroPorId(String isbn) {
        return libroDao.findById(isbn).orElse(null);
    }

    @Transactional
    @Override
    public List<libros> obtenerTodosLosLibros() {
        return libroDao.findAll();
    }

    @Transactional
    @Override
    public List<libros> obtenerLibrosPorUsuario(String email) {
        return libroDao.findByOwner_Email(email);
    }

}