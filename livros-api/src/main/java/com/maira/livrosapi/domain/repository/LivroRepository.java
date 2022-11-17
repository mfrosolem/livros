package com.maira.livrosapi.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maira.livrosapi.domain.model.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long>{
	
	Page<Livro> findByTituloContaining(String titulo, Pageable pageable);

}
