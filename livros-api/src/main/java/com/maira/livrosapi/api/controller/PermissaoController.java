package com.maira.livrosapi.api.controller;

import java.util.List;

import com.maira.livrosapi.api.ResourceUriHelper;
import com.maira.livrosapi.api.assembler.PermissaoInputDisassembler;
import com.maira.livrosapi.api.model.GeneroModel;
import com.maira.livrosapi.api.model.input.GeneroInput;
import com.maira.livrosapi.api.model.input.PermissaoInput;
import com.maira.livrosapi.core.security.CheckRoleSecurity;
import com.maira.livrosapi.domain.model.Genero;
import com.maira.livrosapi.domain.model.Permissao;
import com.maira.livrosapi.domain.service.PermissaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.maira.livrosapi.api.assembler.PermissaoModelAssembler;
import com.maira.livrosapi.api.model.PermissaoModel;
import com.maira.livrosapi.api.openapi.controller.PermissaoControllerOpenApi;
import com.maira.livrosapi.domain.repository.PermissaoRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/permissoes", produces = MediaType.APPLICATION_JSON_VALUE)
public class PermissaoController implements PermissaoControllerOpenApi{

	private final PermissaoService cadastroPermissao;
	private final PermissaoModelAssembler permissaoModelAssembler;
	private final PermissaoInputDisassembler permissaoInputDisassembler;

	public PermissaoController(PermissaoService cadastroPermissao, PermissaoModelAssembler permissaoModelAssembler,
							   PermissaoInputDisassembler permissaoInputDisassembler) {
		this.cadastroPermissao = cadastroPermissao;
		this.permissaoModelAssembler = permissaoModelAssembler;
		this.permissaoInputDisassembler = permissaoInputDisassembler;
	}

	@CheckRoleSecurity.UsuariosGruposPermissoes.PodeConsultar
	@GetMapping
	public Page<PermissaoModel> listar(@RequestParam(required = false, defaultValue = "") String nome,
									Pageable pageable) {
		Page<Permissao> pagePermissao = cadastroPermissao.listByNameContaining(nome, pageable);
		List<PermissaoModel> permissaosModel = permissaoModelAssembler.toCollectionModel(pagePermissao.getContent());
		return new PageImpl<>(permissaosModel, pageable, pagePermissao.getTotalElements());
	}


	@CheckRoleSecurity.UsuariosGruposPermissoes.PodeConsultar
	@GetMapping(value = "/{permissaoId}")
	public PermissaoModel buscar(@PathVariable Long permissaoId) {
		Permissao permissao = cadastroPermissao.buscarOuFalhar(permissaoId);
		return permissaoModelAssembler.toModel(permissao);
	}

	@CheckRoleSecurity.UsuariosGruposPermissoes.PodeCadastrarEditar
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public PermissaoModel adicionar(@RequestBody @Valid PermissaoInput permissaoInput) {
		Permissao permissao = permissaoInputDisassembler.toDomainObject(permissaoInput);
		permissao = cadastroPermissao.salvar(permissao);
		PermissaoModel permissaoModel =permissaoModelAssembler.toModel(permissao);

		ResourceUriHelper.addUriInResponseHeader(permissaoModel.getId());

		return permissaoModel;
	}


	@CheckRoleSecurity.UsuariosGruposPermissoes.PodeCadastrarEditar
	@PutMapping(value = "/{permissaoId}")
	public PermissaoModel atualizar(@PathVariable Long permissaoId, @RequestBody @Valid PermissaoInput permissaoInput) {
		Permissao permissaoAtual = cadastroPermissao.buscarOuFalhar(permissaoId);
		permissaoInputDisassembler.copyToDomainObject(permissaoInput, permissaoAtual);
		permissaoAtual = cadastroPermissao.salvar(permissaoAtual);
		return permissaoModelAssembler.toModel(permissaoAtual);
	}


	@CheckRoleSecurity.UsuariosGruposPermissoes.PodeCadastrarEditar
	@DeleteMapping(value = "/{permissaoId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long permissaoId) {
		cadastroPermissao.excluir(permissaoId);
	}

}
