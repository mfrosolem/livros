package com.maira.livrosapi.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maira.livrosapi.api.model.input.AutorInput;
import com.maira.livrosapi.domain.model.Autor;

@Component
public class AutorInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;

	public Autor toDomainObject(AutorInput autorInput) {
		return modelMapper.map(autorInput, Autor.class);
	}

	public void copyToDomainObject(AutorInput autorInput, Autor autor) {
		modelMapper.map(autorInput, autor);
	}

}
