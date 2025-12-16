package com.example.evaluacion.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.evaluacion.entity.usuarios;

public interface IUsuariosDao extends JpaRepository<usuarios, String> {

}

