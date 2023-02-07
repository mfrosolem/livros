package com.maira.livrosapi.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maira.livrosapi.api.model.FotoLivroModel;
import com.maira.livrosapi.domain.model.FotoLivro;

@Component
public class FotoLivroModelAssembler {

	@Autowired
	private ModelMapper modelMapper;

	public FotoLivroModel toModel(FotoLivro fotoLivro) {
		return modelMapper.map(fotoLivro, FotoLivroModel.class);
	}

}
