package com.maira.livrosapi.api.controller;

import com.maira.livrosapi.api.ResourceUriHelper;
import com.maira.livrosapi.api.assembler.AutorInputDisassembler;
import com.maira.livrosapi.api.assembler.AutorModelAssembler;
import com.maira.livrosapi.api.model.AutorModel;
import com.maira.livrosapi.api.model.input.AutorInput;
import com.maira.livrosapi.api.openapi.controller.AutorControllerOpenApi;
import com.maira.livrosapi.core.security.CheckRoleSecurity;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.service.AutorService;
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
@RequestMapping(value = "/autores", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AutorController implements AutorControllerOpenApi {

	private final AutorService cadastroAutor;
	private final AutorModelAssembler autorModelAssembler;
	private final AutorInputDisassembler autorInputDisassembler;

	@CheckRoleSecurity.Autores.PodeConsultar
	@GetMapping
	public Page<AutorModel> listar(@RequestParam(required = false, defaultValue = "") String nome, Pageable pageable) {
		Page<Autor> autoresPage = cadastroAutor.listByNameContaining(nome, pageable);
		List<AutorModel> autoresModel = autorModelAssembler.toCollectionModel(autoresPage.getContent());
		return new PageImpl<>(autoresModel, pageable, autoresPage.getTotalElements());
	}

	
	@CheckRoleSecurity.Autores.PodeConsultar
	@GetMapping(value = "/{autorId}")
	public AutorModel buscar(@PathVariable Long autorId) {
		Autor autor = cadastroAutor.buscarOuFalhar(autorId);
		return autorModelAssembler.toModel(autor);
	}

	
	@CheckRoleSecurity.Autores.PodeCadastrarEditar
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public AutorModel adicionar(@RequestBody @Valid AutorInput autorInput) {
		Autor autor = autorInputDisassembler.toDomainObject(autorInput);
		autor = cadastroAutor.salvar(autor);
		AutorModel autorModel = autorModelAssembler.toModel(autor);
		
		ResourceUriHelper.addUriInResponseHeader(autorModel.getId());
		
		return autorModel;
	}
	
	
	@CheckRoleSecurity.Autores.PodeCadastrarEditar
	@PutMapping(value = "/{autorId}")
	public AutorModel atualizar(@PathVariable Long autorId, @RequestBody @Valid AutorInput autorInput) {
		Autor autorAtual = cadastroAutor.buscarOuFalhar(autorId);
		autorInputDisassembler.copyToDomainObject(autorInput, autorAtual);
		autorAtual = cadastroAutor.salvar(autorAtual);
		return autorModelAssembler.toModel(autorAtual);
	}
	

	@CheckRoleSecurity.Autores.PodeRemover
	@DeleteMapping(value = "/{autorId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long autorId) {
		cadastroAutor.excluir(autorId);
	}
}
