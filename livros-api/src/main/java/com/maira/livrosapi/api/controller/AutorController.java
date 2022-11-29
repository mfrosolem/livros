package com.maira.livrosapi.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.maira.livrosapi.api.assembler.AutorInputDisassembler;
import com.maira.livrosapi.api.assembler.AutorModelAssembler;
import com.maira.livrosapi.api.model.AutorModel;
import com.maira.livrosapi.api.model.input.AutorInput;
import com.maira.livrosapi.domain.model.Autor;
import com.maira.livrosapi.domain.repository.AutorRepository;
import com.maira.livrosapi.domain.service.AutorService;

@RestController
@RequestMapping(value = "/autores")
@CrossOrigin("http://localhost:4200")
public class AutorController {

	@Autowired
	private AutorRepository autorRepository;

	@Autowired
	private AutorService cadastroAutor;

	@Autowired
	private AutorModelAssembler autorModelAssembler;

	@Autowired
	private AutorInputDisassembler autorInputDisassembler;

	@GetMapping
	public Page<AutorModel> listar(@RequestParam(required = false, defaultValue = "") String nome, Pageable pageable) {
		Page<Autor> autoresPage = autorRepository.findAll(pageable);
		List<AutorModel> autoresModel = autorModelAssembler.toCollectionModel(autoresPage.getContent());
		Page<AutorModel> autoresModelPage = new PageImpl<>(autoresModel, pageable, autoresPage.getTotalElements());
		return autoresModelPage;
	}

	@GetMapping(value = "/{autorId}")
	public AutorModel buscar(@PathVariable Long autorId) {
		Autor autor = cadastroAutor.buscarOuFalhar(autorId);
		return autorModelAssembler.toModel(autor);
	}

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public AutorModel adicionar(@RequestBody @Valid AutorInput autorInput) {
		Autor autor = autorInputDisassembler.toDomainObject(autorInput);
		autor = cadastroAutor.salvar(autor);
		return autorModelAssembler.toModel(autor);
	}

	@PutMapping(value = "/{autorId}")
	public AutorModel atualizar(@PathVariable Long autorId, @RequestBody @Valid AutorInput autorInput) {
		Autor autorAtual = cadastroAutor.buscarOuFalhar(autorId);
		autorInputDisassembler.copyToDomainObject(autorInput, autorAtual);
		autorAtual = cadastroAutor.salvar(autorAtual);
		return autorModelAssembler.toModel(autorAtual);
	}

	@DeleteMapping(value = "/{autorId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long autorId) {
		cadastroAutor.excluir(autorId);
	}
}
