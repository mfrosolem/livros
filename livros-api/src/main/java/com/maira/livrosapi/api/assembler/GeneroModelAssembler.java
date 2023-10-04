package com.maira.livrosapi.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.maira.livrosapi.api.model.GeneroModel;
import com.maira.livrosapi.domain.model.Genero;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GeneroModelAssembler {

	private final ModelMapper modelMapper;

	public GeneroModel toModel(Genero genero) {
		return modelMapper.map(genero, GeneroModel.class);
	}

	public List<GeneroModel> toCollectionModel(List<Genero> generos) {
		return generos.stream().map(genero -> toModel(genero)).collect(Collectors.toList());
	}
}
