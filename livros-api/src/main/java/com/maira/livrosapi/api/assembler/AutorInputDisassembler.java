package com.maira.livrosapi.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.maira.livrosapi.api.model.input.AutorInput;
import com.maira.livrosapi.domain.model.Autor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AutorInputDisassembler {


	private final ModelMapper modelMapper;

	public Autor toDomainObject(AutorInput autorInput) {
		return modelMapper.map(autorInput, Autor.class);
	}

	public void copyToDomainObject(AutorInput autorInput, Autor autor) {
		modelMapper.map(autorInput, autor);
	}

}
