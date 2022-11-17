package com.maira.livrosapi.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maira.livrosapi.domain.model.Autor;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

	Page<Autor> findByNomeContaining(String nome, Pageable pageable);

}
