package com.maira.livrosapi.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.maira.livrosapi.api.model.input.LivroInput;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.model.Editora;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.model.Livro;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LivroInputDisassembler {

	private final ModelMapper modelMapper;

	public Livro toDomainObject(LivroInput livroInput) {
		return modelMapper.map(livroInput, Livro.class);
	}

	public void copyToDomainObject(LivroInput livroInput, Livro livro) {
		
		// Para evitar org.hibernate.HibernateException: identifier of an instance of 
		// com.maira.livrosapi.domain.model.Autor/com.maira.livrosapi.domain.model.Editora/
		// com.maira.livrosapi.domain.model.Genero was altered from 1 to 2
		livro.setAutor(new Autor());
		livro.setEditora(new Editora());
		livro.setGenero(new Genero());
		
		modelMapper.map(livroInput, livro);
	}
}
