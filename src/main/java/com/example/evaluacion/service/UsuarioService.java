package com.example.evaluacion.service;

import com.example.evaluacion.entity.usuarios;
import java.util.List;

public interface UsuarioService {
   public void eliminarUsuario(String email);
    public usuarios guardarUsuario(usuarios usuario);
    public usuarios obtenerUsuarioPorEmail(String email);
    public List<usuarios> obtenerTodosLosUsuarios();
}