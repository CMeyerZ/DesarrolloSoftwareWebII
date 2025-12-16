package com.example.evaluacion.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.evaluacion.entity.libros;

public interface ILibrosDao extends JpaRepository<libros, String> {

	// Buscar libros por el email del propietario
	java.util.List<libros> findByOwner_Email(String email);

}