package com.maira.livrosapi.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.maira.livrosapi.api.ResourceUriHelper;
import com.maira.livrosapi.api.assembler.EditoraInputDisassembler;
import com.maira.livrosapi.api.assembler.EditoraModelAssembler;
import com.maira.livrosapi.api.model.EditoraModel;
import com.maira.livrosapi.api.model.input.EditoraInput;
import com.maira.livrosapi.api.openapi.controller.EditoraControllerOpenApi;
import com.maira.livrosapi.core.security.CheckRoleSecurity;
import com.maira.livrosapi.domain.model.Editora;
import com.maira.livrosapi.domain.repository.EditoraRepository;
import com.maira.livrosapi.domain.service.EditoraService;

import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/editoras", produces = MediaType.APPLICATION_JSON_VALUE)
public class EditoraController implements EditoraControllerOpenApi {

	@Autowired
	private EditoraRepository editoraRepository;

	@Autowired
	private EditoraService cadastroEditora;

	@Autowired
	private EditoraModelAssembler editoraModelAssembler;

	@Autowired
	private EditoraInputDisassembler editoraInputDisassembler;
	

	@CheckRoleSecurity.Editoras.PodeConsultar
	@GetMapping
	public Page<EditoraModel> listar(@RequestParam(required = false, defaultValue = "") String nome,
			Pageable pageable) {
		
		Page<Editora> editorasPage = editoraRepository.findByNomeContaining(nome, pageable);
		List<EditoraModel> editorasModel = editoraModelAssembler.toCollectionModel(editorasPage.getContent());
		Page<EditoraModel> editorasModelPage = new PageImpl<>(editorasModel, pageable, editorasPage.getTotalElements());
		
		return editorasModelPage;
	}
	
	
	@CheckRoleSecurity.Editoras.PodeConsultar
	@GetMapping("/{editoraId}")
	public EditoraModel buscar(@PathVariable Long editoraId) {
		
		Editora editora = cadastroEditora.buscarOuFalhar(editoraId);
		
		return editoraModelAssembler.toModel(editora);
	}

	
	@CheckRoleSecurity.Editoras.PodeCadastrarEditar
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public EditoraModel adicionar(@RequestBody @Valid EditoraInput editoraInput) {
		
		Editora editora = editoraInputDisassembler.toDomainObject(editoraInput);
		editora = cadastroEditora.salvar(editora);
		EditoraModel editoraModel = editoraModelAssembler.toModel(editora);
		
		ResourceUriHelper.addUriInResponseHeader(editoraModel.getId());
		
		return editoraModel;
	}

	@CheckRoleSecurity.Editoras.PodeCadastrarEditar
	@PutMapping("/{editoraId}")
	public EditoraModel atualizar(@PathVariable Long editoraId, @RequestBody @Valid EditoraInput editoraInput) {
		
		Editora editoraAtual = cadastroEditora.buscarOuFalhar(editoraId);
		editoraInputDisassembler.copyToDomainObject(editoraInput, editoraAtual);
		editoraAtual = cadastroEditora.salvar(editoraAtual);
		
		return editoraModelAssembler.toModel(editoraAtual);
	}

	
	@CheckRoleSecurity.Editoras.PodeRemover
	@DeleteMapping("/{editoraId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long editoraId) {
		
		cadastroEditora.excluir(editoraId);
	}

}
