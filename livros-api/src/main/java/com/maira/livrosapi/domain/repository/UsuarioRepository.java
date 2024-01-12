package com.maira.livrosapi.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.maira.livrosapi.domain.model.Usuario;

@Repository
public interface UsuarioRepository extends CustomJpaRepository<Usuario, Long>{

	Boolean existsByEmail(String email);
	Optional<Usuario> findByEmail(String email);
	
	Page<Usuario> findByNomeContaining(String nome, Pageable pageable);
}
