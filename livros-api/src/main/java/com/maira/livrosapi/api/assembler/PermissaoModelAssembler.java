package com.maira.livrosapi.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maira.livrosapi.api.model.PermissaoModel;
import com.maira.livrosapi.domain.model.Permissao;

@Component
public class PermissaoModelAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	public PermissaoModel toModel(Permissao permissao) {
		return modelMapper.map(permissao, PermissaoModel.class);
	}
	
	public List<PermissaoModel> toCollectionModel(List<Permissao> permissoes) {
		return permissoes.stream()
				.map(permissao -> this.toModel(permissao))
				.collect(Collectors.toList());
	} 

}
