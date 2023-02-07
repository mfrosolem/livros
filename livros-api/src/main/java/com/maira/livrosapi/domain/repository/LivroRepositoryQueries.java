package com.maira.livrosapi.domain.repository;

import com.maira.livrosapi.domain.model.FotoLivro;

public interface LivroRepositoryQueries {
	
	FotoLivro save(FotoLivro fotoLivro);
	
	void delete(FotoLivro fotoLivro);

}
