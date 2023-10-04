package com.maira.livrosapi.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.maira.livrosapi.api.model.FotoLivroModel;
import com.maira.livrosapi.domain.model.FotoLivro;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FotoLivroModelAssembler {

	private final ModelMapper modelMapper;

	public FotoLivroModel toModel(FotoLivro fotoLivro) {
		return modelMapper.map(fotoLivro, FotoLivroModel.class);
	}

}
