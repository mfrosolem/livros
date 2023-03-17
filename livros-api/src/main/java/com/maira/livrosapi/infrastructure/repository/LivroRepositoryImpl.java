package com.maira.livrosapi.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.maira.livrosapi.domain.model.FotoLivro;
import com.maira.livrosapi.domain.repository.LivroRepositoryQueries;

@Repository
public class LivroRepositoryImpl implements LivroRepositoryQueries {
	
	@PersistenceContext
	private EntityManager manager;

	@Transactional
	@Override
	public FotoLivro save(FotoLivro fotoLivro) {
		return manager.merge(fotoLivro);
	}

	@Transactional
	@Override
	public void delete(FotoLivro fotoLivro) {
		manager.remove(fotoLivro);
		
	}

}
