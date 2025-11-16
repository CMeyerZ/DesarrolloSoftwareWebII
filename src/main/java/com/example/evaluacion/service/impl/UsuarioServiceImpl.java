package com.example.evaluacion.service.impl;
import com.example.evaluacion.service.UsuarioService;
import com.example.evaluacion.entity.usuarios;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.ArrayList;
import com.example.evaluacion.dao.IUsuariosDao;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private IUsuariosDao usuarioDao;
    @Transactional
    public usuarios guardarUsuario(usuarios usuario) {
        return usuarioDao.save(usuario);
    }

    @Transactional
    public void eliminarUsuario(String email) {
        usuarioDao.deleteById(email);
    }

    @Transactional
    public usuarios obtenerUsuarioPorEmail(String email) {
        return usuarioDao.findById(email).orElse(null);
    }

    @Transactional
    public List<usuarios> obtenerTodosLosUsuarios() {
        List<usuarios> list = new ArrayList<>();
        usuarioDao.findAll().forEach(list::add);
        return list;
    }
}