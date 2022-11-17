package com.maira.livrosapi.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maira.livrosapi.api.model.input.LivroInput;
import com.maira.livrosapi.domain.model.Livro;

@Component
public class LivroInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;

	public Livro toDomainObject(LivroInput livroInput) {
		return modelMapper.map(livroInput, Livro.class);
	}

	public void copyToDomainObject(LivroInput livroInput, Livro livro) {
		modelMapper.map(livroInput, livro);
	}
}
