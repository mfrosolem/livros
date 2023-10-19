package com.maira.livrosapi.api.controller;

import java.util.List;

import com.maira.livrosapi.core.security.CheckRoleSecurity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maira.livrosapi.api.assembler.PermissaoModelAssembler;
import com.maira.livrosapi.api.model.PermissaoModel;
import com.maira.livrosapi.api.openapi.controller.PermissaoControllerOpenApi;
import com.maira.livrosapi.domain.repository.PermissaoRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/permissoes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PermissaoController implements PermissaoControllerOpenApi{
	

	private final PermissaoRepository permissaoRepository;
	
	private final PermissaoModelAssembler permissaoModelAssembler;
	

	@CheckRoleSecurity.UsuariosGruposPermissoes.PodeConsultar
	@GetMapping
	@Override
	public List<PermissaoModel> listar() {		
		return permissaoModelAssembler.toCollectionModel(this.permissaoRepository.findAll());
	}

}
