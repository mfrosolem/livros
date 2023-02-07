package com.maira.livrosapi.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maira.livrosapi.domain.model.FotoLivro;
import com.maira.livrosapi.domain.model.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long>, LivroRepositoryQueries {

	Page<Livro> findByTituloContaining(String titulo, Pageable pageable);

	@Query("select f from FotoLivro f join f.livro l where f.livro.id = :livroId")
	Optional<FotoLivro> findFotoById(Long livroId);

}
