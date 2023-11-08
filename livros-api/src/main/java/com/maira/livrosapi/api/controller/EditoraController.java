package com.maira.livrosapi.api.controller;

import com.maira.livrosapi.api.ResourceUriHelper;
import com.maira.livrosapi.api.assembler.EditoraInputDisassembler;
import com.maira.livrosapi.api.assembler.EditoraModelAssembler;
import com.maira.livrosapi.api.model.EditoraModel;
import com.maira.livrosapi.api.model.input.EditoraInput;
import com.maira.livrosapi.api.openapi.controller.EditoraControllerOpenApi;
import com.maira.livrosapi.core.security.CheckRoleSecurity;
import com.maira.livrosapi.domain.model.Editora;
import com.maira.livrosapi.domain.service.EditoraService;
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
@RequestMapping(value = "/editoras", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class EditoraController implements EditoraControllerOpenApi {

	private final EditoraService cadastroEditora;
	private final EditoraModelAssembler editoraModelAssembler;
	private final EditoraInputDisassembler editoraInputDisassembler;
	

	@CheckRoleSecurity.Editoras.PodeConsultar
	@GetMapping
	public Page<EditoraModel> listar(@RequestParam(required = false, defaultValue = "") String nome,
			Pageable pageable) {
		
		Page<Editora> editorasPage = cadastroEditora.listByNameContaining(nome, pageable);
		List<EditoraModel> editorasModel = editoraModelAssembler.toCollectionModel(editorasPage.getContent());
		return new PageImpl<>(editorasModel, pageable, editorasPage.getTotalElements());
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
