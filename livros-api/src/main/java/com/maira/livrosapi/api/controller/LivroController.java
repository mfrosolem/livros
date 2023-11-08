package com.maira.livrosapi.api.controller;

import com.maira.livrosapi.api.ResourceUriHelper;
import com.maira.livrosapi.api.assembler.LivroInputDisassembler;
import com.maira.livrosapi.api.assembler.LivroModelAssembler;
import com.maira.livrosapi.api.model.LivroModel;
import com.maira.livrosapi.api.model.input.LivroInput;
import com.maira.livrosapi.api.openapi.controller.LivroControllerOpenApi;
import com.maira.livrosapi.core.security.CheckRoleSecurity;
import com.maira.livrosapi.domain.model.Livro;
import com.maira.livrosapi.domain.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/livros", produces = MediaType.APPLICATION_JSON_VALUE)

public class LivroController implements LivroControllerOpenApi {

	private final LivroService cadastroLivro;
	private final LivroModelAssembler livroModelAssembler;
	private final LivroInputDisassembler livroInputDisassembler;

	public LivroController(LivroService cadastroLivro, LivroModelAssembler livroModelAssembler,
						   LivroInputDisassembler livroInputDisassembler) {
		this.cadastroLivro = cadastroLivro;
		this.livroModelAssembler = livroModelAssembler;
		this.livroInputDisassembler = livroInputDisassembler;
	}

	@CheckRoleSecurity.Livros.PodeConsultar
	@GetMapping
	public Page<LivroModel> listar(@RequestParam(required = false, defaultValue = "") String titulo,
			Pageable pageable) {
		
		Page<Livro> livrosPage = cadastroLivro.listByTituloContaining(titulo, pageable);
		List<LivroModel> livrosModel = livroModelAssembler.toCollectionModel(livrosPage.getContent());
		return new PageImpl<>(livrosModel, pageable, livrosPage.getTotalElements());
	}

	@CheckRoleSecurity.Livros.PodeConsultar
	@GetMapping(value = "/{livroId}")
	public LivroModel buscar(@PathVariable Long livroId) {
		Livro livro = cadastroLivro.buscarOuFalhar(livroId);
		return livroModelAssembler.toModel(livro);
	}
	

	@CheckRoleSecurity.Livros.PodeCadastrarEditar
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public LivroModel adicionar(@RequestBody @Valid LivroInput livroInput) {
		Livro livro = livroInputDisassembler.toDomainObject(livroInput);
		livro = cadastroLivro.salvar(livro);
		LivroModel livroModel = livroModelAssembler.toModel(livro);
		
		ResourceUriHelper.addUriInResponseHeader(livroModel.getId());
		
		return livroModel;
	}

	
	@CheckRoleSecurity.Livros.PodeCadastrarEditar
	@PutMapping(value = "/{livroId}")
	public LivroModel atualizar(@PathVariable Long livroId, @RequestBody @Valid LivroInput livroInput) {
		Livro livroAtual = cadastroLivro.buscarOuFalhar(livroId);
		livroInputDisassembler.copyToDomainObject(livroInput, livroAtual);
		livroAtual = cadastroLivro.salvar(livroAtual);
		
		return livroModelAssembler.toModel(livroAtual);
	}

	
	@CheckRoleSecurity.Livros.PodeRemover
	@DeleteMapping(value = "/{livroId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long livroId) {
		cadastroLivro.excluir(livroId);
	}

}
