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

import com.maira.livrosapi.api.assembler.LivroInputDisassembler;
import com.maira.livrosapi.api.assembler.LivroModelAssembler;
import com.maira.livrosapi.api.model.LivroModel;
import com.maira.livrosapi.api.model.input.LivroInput;
import com.maira.livrosapi.domain.model.Livro;
import com.maira.livrosapi.domain.repository.LivroRepository;
import com.maira.livrosapi.domain.service.LivroService;

@RestController
@RequestMapping(value = "/livros")
@CrossOrigin("http://localhost:4200")
public class LivroController {

	@Autowired
	private LivroRepository livroRepository;

	@Autowired
	private LivroService cadastroLivro;

	@Autowired
	private LivroModelAssembler livroModelAssembler;

	@Autowired
	private LivroInputDisassembler livroInputDisassembler;

	@GetMapping
	public Page<LivroModel> listar(@RequestParam(required = false, defaultValue = "") String titulo,
			Pageable pageable) {
		Page<Livro> livrosPage = livroRepository.findByTituloContaining(titulo, pageable);
		List<LivroModel> livrosModel = livroModelAssembler.toCollectionModel(livrosPage.getContent());
		Page<LivroModel> livrosModelPage = new PageImpl<>(livrosModel, pageable, livrosPage.getTotalElements());
		return livrosModelPage;
	}

	@GetMapping(value = "/{livroId}")
	public LivroModel buscar(@PathVariable Long livroId) {
		Livro livro = cadastroLivro.buscarOuFalhar(livroId);
		return livroModelAssembler.toModel(livro);
	}

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public LivroModel adicionar(@RequestBody @Valid LivroInput livroInput) {
		Livro livro = livroInputDisassembler.toDomainObject(livroInput);
		livro = cadastroLivro.salvar(livro);
		return livroModelAssembler.toModel(livro);
	}

	@PutMapping(value = "/{livroId}")
	public LivroModel atualizar(@PathVariable Long livroId, @RequestBody @Valid LivroInput livroInput) {
		Livro livroAtual = cadastroLivro.buscarOuFalhar(livroId);
		livroInputDisassembler.copyToDomainObject(livroInput, livroAtual);
		livroAtual = cadastroLivro.salvar(livroAtual);
		return livroModelAssembler.toModel(livroAtual);
	}

	@DeleteMapping(value = "/{livroId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long livroId) {
		cadastroLivro.excluir(livroId);
	}

}
