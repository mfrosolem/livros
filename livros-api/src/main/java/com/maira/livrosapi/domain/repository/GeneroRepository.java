package com.maira.livrosapi.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maira.livrosapi.domain.model.Genero;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long> {
	
	Page<Genero> findByDescricaoContaining(String descricao, Pageable pageable);

}
