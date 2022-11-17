package com.maira.livrosapi.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maira.livrosapi.api.model.input.GeneroInput;
import com.maira.livrosapi.domain.model.Genero;

@Component
public class GeneroInputDisassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Genero toDomainObject(GeneroInput generoInput) {
		return modelMapper.map(generoInput, Genero.class);
	}
	
	public void copyToDomainObject(GeneroInput generoInput, Genero genero) {
		modelMapper.map(generoInput, genero);
	}

}
