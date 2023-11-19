package com.maira.livrosapi.api.controller;

import com.maira.livrosapi.api.ResourceUriHelper;
import com.maira.livrosapi.api.assembler.GeneroInputDisassembler;
import com.maira.livrosapi.api.assembler.GeneroModelAssembler;
import com.maira.livrosapi.api.model.GeneroModel;
import com.maira.livrosapi.api.model.input.GeneroInput;
import com.maira.livrosapi.api.openapi.controller.GeneroControllerOpenApi;
import com.maira.livrosapi.core.security.CheckRoleSecurity;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.service.GeneroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/generos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class GeneroController implements GeneroControllerOpenApi {

	private final GeneroService cadastroGenero;
	private final GeneroModelAssembler generoModelAssembler;
	private final GeneroInputDisassembler generoInputDisassembler;


	@CheckRoleSecurity.Generos.PodeConsultar
	@GetMapping
	public Page<GeneroModel> listar(@RequestParam(required = false, defaultValue = "") String descricao,
									Pageable pageable) {
		Page<Genero> pageGenero = cadastroGenero.listByContaining(descricao, pageable);
		List<GeneroModel> generosModel = generoModelAssembler.toCollectionModel(pageGenero.getContent());
		return new PageImpl<>(generosModel, pageable, pageGenero.getTotalElements());
	}

	
	@CheckRoleSecurity.Generos.PodeConsultar
	@GetMapping(value = "/{generoId}")
	public GeneroModel buscar(@PathVariable Long generoId) {
		Genero genero = cadastroGenero.buscarOuFalhar(generoId);
		return generoModelAssembler.toModel(genero);
	}

	@CheckRoleSecurity.Generos.PodeCadastrarEditar
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public GeneroModel adicionar(@RequestBody @Valid GeneroInput generoInput) {
		Genero genero = generoInputDisassembler.toDomainObject(generoInput);
		genero = cadastroGenero.salvar(genero);
		GeneroModel generoModel =generoModelAssembler.toModel(genero);
		
		ResourceUriHelper.addUriInResponseHeader(generoModel.getId());
		
		return generoModel;
	}

	
	@CheckRoleSecurity.Generos.PodeCadastrarEditar
	@PutMapping(value = "/{generoId}")
	public GeneroModel atualizar(@PathVariable Long generoId, @RequestBody @Valid GeneroInput generoInput) {
		Genero generoAtual = cadastroGenero.buscarOuFalhar(generoId);
		generoInputDisassembler.copyToDomainObject(generoInput, generoAtual);
		generoAtual = cadastroGenero.salvar(generoAtual);
		return generoModelAssembler.toModel(generoAtual);
	}
	

	@CheckRoleSecurity.Generos.PodeRemover
	@DeleteMapping(value = "/{generoId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long generoId) {
		cadastroGenero.excluir(generoId);
	}

}
