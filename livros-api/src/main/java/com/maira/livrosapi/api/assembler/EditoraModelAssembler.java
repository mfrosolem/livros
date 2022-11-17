package com.maira.livrosapi.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maira.livrosapi.api.model.EditoraModel;
import com.maira.livrosapi.domain.model.Editora;

@Component
public class EditoraModelAssembler {

	@Autowired
	private ModelMapper modelMapper;

	public EditoraModel toModel(Editora editora) {
		return modelMapper.map(editora, EditoraModel.class);
	}
	
	public List<EditoraModel> toCollectionModel(List<Editora> editoras) {
		return editoras.stream()
				.map(editora -> toModel(editora))
				.collect(Collectors.toList());
	}

}
