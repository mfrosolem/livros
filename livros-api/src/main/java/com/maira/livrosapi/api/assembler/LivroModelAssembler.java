package com.maira.livrosapi.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.maira.livrosapi.api.model.LivroModel;
import com.maira.livrosapi.domain.model.Livro;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LivroModelAssembler {

	private final ModelMapper modelMapper;

	public LivroModel toModel(Livro livro) {
		return modelMapper.map(livro, LivroModel.class);
	}

	public List<LivroModel> toCollectionModel(List<Livro> livros) {
		return livros.stream().map(livro -> toModel(livro)).collect(Collectors.toList());
	}
}
