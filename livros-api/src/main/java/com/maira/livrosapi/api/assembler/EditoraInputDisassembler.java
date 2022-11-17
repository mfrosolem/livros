package com.maira.livrosapi.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maira.livrosapi.api.model.input.EditoraInput;
import com.maira.livrosapi.domain.model.Editora;

@Component
public class EditoraInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;

	public Editora toDomainObject(EditoraInput editoraInput) {
		return modelMapper.map(editoraInput, Editora.class);
	}
	
	public void copyToDomainObject(EditoraInput editoraInput, Editora editora) {
		modelMapper.map(editoraInput, editora);
	}
}
