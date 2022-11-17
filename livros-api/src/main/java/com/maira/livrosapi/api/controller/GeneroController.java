package com.maira.livrosapi.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.maira.livrosapi.api.assembler.GeneroInputDisassembler;
import com.maira.livrosapi.api.assembler.GeneroModelAssembler;
import com.maira.livrosapi.api.model.GeneroModel;
import com.maira.livrosapi.api.model.input.GeneroInput;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.repository.GeneroRepository;
import com.maira.livrosapi.domain.service.GeneroService;

@RestController
@RequestMapping(value = "/generos")
public class GeneroController {

	@Autowired
	private GeneroRepository generoRepository;

	@Autowired
	private GeneroService cadastroGenero;

	@Autowired
	private GeneroModelAssembler generoModelAssembler;

	@Autowired
	private GeneroInputDisassembler generoInputDisassembler;

	@GetMapping
	public Page<GeneroModel> listar(@RequestParam(required = false, defaultValue = "") String descricao,
			Pageable pageable) {
		Page<Genero> pageGenero = generoRepository.findByDescricaoContaining(descricao, pageable);

		List<GeneroModel> generosModel = generoModelAssembler.toCollectionModel(pageGenero.getContent());

		Page<GeneroModel> pageGeneroModel = new PageImpl<>(generosModel, pageable, pageGenero.getTotalElements());

		return pageGeneroModel;
	}

	@GetMapping(value = "/{generoId}")
	public GeneroModel buscar(@RequestParam Long generoId) {
		Genero genero = cadastroGenero.buscarOuFalhar(generoId);
		return generoModelAssembler.toModel(genero);
	}

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public GeneroModel adicionar(@RequestBody @Valid GeneroInput generoInput) {
		Genero genero = generoInputDisassembler.toDomainObject(generoInput);
		genero = cadastroGenero.salvar(genero);
		return generoModelAssembler.toModel(genero);
	}

	@PutMapping(value = "/{generoId}")
	public GeneroModel atualizar(@PathVariable Long generoId, @RequestBody @Valid GeneroInput generoInput) {
		Genero generoAtual = cadastroGenero.buscarOuFalhar(generoId);
		generoInputDisassembler.copyToDomainObject(generoInput, generoAtual);
		generoAtual = cadastroGenero.salvar(generoAtual);
		return generoModelAssembler.toModel(generoAtual);
	}

	@DeleteMapping(value = "/{generoId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long generoId) {
		cadastroGenero.excluir(generoId);
	}

}
